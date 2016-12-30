package com.pce.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pce.domain.GDriveFile;
import com.pce.domain.Pce;
import com.pce.domain.dto.GDriveFileDto;
import com.pce.exception.GoogleCredentialsException;
import com.pce.exception.InvalidGoogleFileException;
import com.pce.repository.GDriveRepository;
import com.pce.service.authentication.GDriveAuthentication;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 29/12/2016.
 */
@Service
public class GDriveServiceImpl implements DriveService {

  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private static final Logger logger = LoggerFactory.getLogger(GDriveServiceImpl.class);
  public static final String ROOT_PARENT_INVOICE_FOLDER = "GKY-SYD-PCE-Invoice";
  public static final String GOOGLE_FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";

  @Autowired
  private GDriveAuthentication gDriveAuthentication;


  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private GDriveRepository gDriveRepository;

  @Transactional
  @Override
  public GDriveFile uploadFile(Pce pce, MultipartFile uploadFile) {
    try {
      byte[] uploadedFileBytes = uploadFile.getBytes();
      InputStreamContent mediaContent = new InputStreamContent(uploadFile.getContentType(),
              new BufferedInputStream(new ByteArrayInputStream(uploadedFileBytes)));
      mediaContent.setLength(uploadedFileBytes.length);
      Drive drive = getGDrive();

      File fileMetadata = new File();
      fileMetadata.setName(pce.getPceNo() + " - " + pce.getPceYear() + " - " + "invoice - " + uploadFile.getOriginalFilename());

      File parentFolder = checkAndCreateFolder(drive, ROOT_PARENT_INVOICE_FOLDER, "root", "GKY PCE Invoice folder");
      setPermission(drive, parentFolder.getId());
      String parentFolderId = parentFolder.getId();

      File yearFolder = checkAndCreateFolder(drive, String.valueOf(pce.getPceYear()), parentFolderId, "Year group subfolder for PCE Invoice");
      File pceFolder = checkAndCreateFolder(drive, pce.getAssociatedPuk().getPukNo(), yearFolder.getId(), "Puk No group subfolder for PCE Invoice");

      fileMetadata.setParents(Lists.newArrayList(parentFolderId, yearFolder.getId(), pceFolder.getId()));

      Drive.Files.Create request = drive.files().create(fileMetadata, mediaContent)
              .setFields("id, parents, webViewLink, name, thumbnailLink, webContentLink, iconLink, mimeType");
      File executedFile = request.execute();


      GDriveFileDto gDriveFileDto = new GDriveFileDto();
      gDriveFileDto.setFileId(executedFile.getId());
      gDriveFileDto.setFileName(executedFile.getName());
      gDriveFileDto.setWebContentLink(executedFile.getWebContentLink());
      gDriveFileDto.setWebViewLink(executedFile.getWebViewLink());
      gDriveFileDto.setThumbnailLink(executedFile.getThumbnailLink());
      gDriveFileDto.setIconLink(executedFile.getIconLink());
      gDriveFileDto.setMimeType(executedFile.getMimeType());

      GDriveFile gDriveFile = modelMapper.map(gDriveFileDto, GDriveFile.class);
      gDriveFile.setPce(pce);
      return gDriveRepository.save(gDriveFile);


    } catch (IOException e) {
      logger.error("Error when interacting with google drive api", e);
      throw new InvalidGoogleFileException("Exception occurs while interacting with Google Drive API ", e);
    }
  }


  @Override
  public Optional<GDriveFile> getDriveFileById(long id) {
    return Optional.ofNullable(gDriveRepository.findOne(id));
  }

  @Override
  public Resource loadFileAsResource(long fileId) {

    try {
      Optional<GDriveFile> driveFileById = getDriveFileById(fileId);
      if (!driveFileById.isPresent()) {
        throw new IllegalArgumentException("Unable to get drive file from repository with ID " + fileId);
      }
      GDriveFile gDriveFile = driveFileById.get();
      String mimeType = gDriveFile.getMimeType();
      return new InputStreamResource(getGDrive().files().export(gDriveFile.getFileId(), mimeType).executeMediaAsInputStream());
    } catch (IOException e) {
      logger.error("Unable to load file with id " + fileId, e);
      throw new InvalidGoogleFileException("Unable to load file with id " + fileId, e);
    }
  }

  public boolean deleteDriveFile(long databaseDriveFileId) {
    try {
      Drive drive = getGDrive();
      Optional<GDriveFile> optionalGDriveFile = getDriveFileById(databaseDriveFileId);
      if (!optionalGDriveFile.isPresent()) {
        throw new IllegalArgumentException("Unable to get drive file from repository with ID " + databaseDriveFileId);
      }
      GDriveFile driveFile = optionalGDriveFile.get();
      String gDriveFileId = driveFile.getFileId();
      logger.debug("Deleting file with drive id " + gDriveFileId + " from google drive");
      drive.files().delete(gDriveFileId).execute();
      logger.debug("Deleting gDrive database record with id " + databaseDriveFileId);
      gDriveRepository.delete(databaseDriveFileId);
      return true;

    } catch (IOException e) {
      logger.error("Error when interacting with google drive api ", e);
      throw new InvalidGoogleFileException("Exception occurs while interacting with Google Drive API ", e);
    }

  }

  @Override
  public Page<GDriveFile> getDriveFilesByPce(Pce pce, Pageable pageRequest) {
    Preconditions.checkArgument(pce != null, "Pce cannot be null");
    return gDriveRepository.findByPce(pce, pageRequest);
  }


  private File checkAndCreateFolder(Drive drive, String folderName, String parentFolderId, String folderDescription) throws IOException {
    Preconditions.checkArgument(StringUtils.isNotEmpty(folderName), "Folder name cannot be null or empty");
    Drive.Files.List parentFolderRequest = drive.files().list().setQ("mimeType='application/vnd.google-apps.folder' and trashed=false  and '" + parentFolderId + "' in parents");
    FileList folderList = parentFolderRequest.execute();
    List<File> files = folderList.getFiles();
    if (org.springframework.util.CollectionUtils.isEmpty(files)) {
      return createFolder(drive, folderName, folderDescription, parentFolderId);
    }
    for (File file : files) {
      if (String.valueOf(folderName).equalsIgnoreCase(file.getName())) {
        return file;
      }
    }
    throw new InvalidGoogleFileException("Unable to find subfolder " + folderName);
  }

  private File createFolder(Drive drive, String folderName, String folderDescription, String parentIds) throws IOException {
    File fileMetadata = new File();
    fileMetadata.setName(folderName);
    fileMetadata.setParents(Collections.singletonList(parentIds));
    fileMetadata.setDescription(folderDescription);
    fileMetadata.setMimeType(GOOGLE_FOLDER_MIME_TYPE);

    return drive.files().create(fileMetadata)
            .setFields("id")
            .execute();

  }

  private Drive getGDrive() throws IOException {
    Credential credential = gDriveAuthentication.getCredential();
    return new Drive.Builder(getHttpTransport(), JSON_FACTORY, credential).setApplicationName("GKY-PCE-App").build();

  }

  private HttpTransport getHttpTransport() {
    try {
      return GoogleNetHttpTransport.newTrustedTransport();
    } catch (Exception e) {
      logger.error("Exception when initializing http transport", e);
      throw new GoogleCredentialsException("Exception when initializing http transport ", e);
    }

  }


  private void setPermission(Drive drive, String fileId) {
    JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
      @Override
      public void onFailure(GoogleJsonError e,
                            HttpHeaders responseHeaders)
              throws IOException {
        logger.error("Unable to set permission ", e);
        throw new GoogleJsonResponseException(new HttpResponseException.Builder(400, "Error setting permission", responseHeaders), e);
      }

      @Override
      public void onSuccess(Permission permission,
                            HttpHeaders responseHeaders)
              throws IOException {
        logger.info("Successfully setting permission with  ID: " + permission.getId());
      }
    };
    BatchRequest batch = drive.batch();
    Permission userPermission = new Permission()
            .setType("anyone")
            .setRole("reader");
    try {
      PermissionList permissionList = drive.permissions().list(fileId).execute();
      List<Permission> permissions = permissionList.getPermissions();
      if (CollectionUtils.isEmpty(permissions) || !isAnyoneReaderPermission(permissions)) {
        drive.permissions().create(fileId, userPermission)
                .setFields("id")
                .queue(batch, callback);
        batch.execute();
      }

    } catch (IOException e) {
      throw new InvalidGoogleFileException("Unable to get file with id " + fileId + " from GDrive", e);
    }
  }

  private boolean isAnyoneReaderPermission(List<Permission> permissions) {
    return permissions.stream().anyMatch(permission -> "anyone".equals(permission.getType()) && "reader".equals(permission.getRole()));
  }

}
