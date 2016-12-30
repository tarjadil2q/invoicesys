package com.pce.service.authentication;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.common.collect.Lists;
import com.pce.exception.GoogleCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 28/12/2016.
 */
@Service
public class GDriveAuthentication {
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final Logger logger = LoggerFactory.getLogger(GDriveAuthentication.class);

  public static Credential getCredential() throws IOException {
    GoogleCredential credential = GoogleCredential.fromStream(GDriveAuthentication.class.getResourceAsStream("/pce.json"))
            .createScoped(Lists.newArrayList(DriveScopes.DRIVE,
                    DriveScopes.DRIVE_FILE,
                    DriveScopes.DRIVE_READONLY,
                    DriveScopes.DRIVE_METADATA,
                    DriveScopes.DRIVE_METADATA_READONLY));
    return credential;
  }

  public static Drive getGDrive() throws IOException {
    Credential credential = getCredential();
    return new Drive.Builder(getHttpTransport(), JSON_FACTORY, credential).setApplicationName("GKY-PCE-App").build();

  }

  private static HttpTransport getHttpTransport() {
    try {
      return GoogleNetHttpTransport.newTrustedTransport();
    } catch (Exception e) {
      logger.error("Exception when initializing http transport", e);
      throw new GoogleCredentialsException("Exception when initializing http transport ", e);
    }

  }

  public static void main(String[] args) throws Exception {
    Drive drive = getGDrive();

   /* File fileMetadata = new File();
    fileMetadata.setName("GKY-SYD-PCE-Invoice");
    fileMetadata.setDescription("GKY Sydney PCE Invoices Parent Folder");
    fileMetadata.setMimeType("application/vnd.google-apps.folder");

    File folder = drive.files().create(fileMetadata)
            .setFields("id")
            .execute();
    System.out.println("Folder ID: " + folder.getId());*/
    FileList result = drive.files().list().setPageSize(10).setFields("nextPageToken, files(id, name, mimeType, webViewLink)")
            .execute();
    List<File> files = result.getFiles();
    setPermission(drive, files.get(0).getId());

    if (CollectionUtils.isEmpty(files)) {
      logger.warn("No files found");
    }
    for (com.google.api.services.drive.model.File file : files) {
      logger.debug("file " + file.getName() + "mime type: " + file.getMimeType() + "id: " + file.getId() + " web link: " + file.getWebViewLink());
    }
    ClassLoader classLoader = GDriveAuthentication.class.getClassLoader();
    java.io.File someFile = new java.io.File(classLoader.getResource("GeMA Vol7 No5.pdf").getFile());

    InputStreamContent mediaContent =
            new InputStreamContent(null,
                    new BufferedInputStream(new FileInputStream(someFile)));
    mediaContent.setLength(someFile.length());
    File fileMetadata = new File();
    fileMetadata.setName("testFile");

    Drive.Files.List parentFolderRequest = drive.files().list().setQ("mimeType='application/vnd.google-apps.folder' and trashed=false and fullText contains 'GKY-SYD-PCE-Invoice'");
    FileList parentFolderList = parentFolderRequest.execute();
    if (org.springframework.util.CollectionUtils.isEmpty(parentFolderList)) {
      System.out.println("cannot find parent folder");
    }
    /*Drive.Files.Delete  deleteRequest = drive.files().delete("0B911etLyV-SJNmg3UkU3OFVITWM");
    deleteRequest.execute();*/
    File parentFolder = parentFolderList.getFiles().get(0);
    String parentFolderId = parentFolder.getId();
    fileMetadata.setParents(Collections.singletonList(parentFolderId));
    Drive.Files.Create request = drive.files().create(fileMetadata, mediaContent)
            .setFields("id, parents, webViewLink, name, thumbnailLink");
    File executedFile = request.execute();
    System.out.println("Created file " + "id: " + executedFile.getId() + "name: " + executedFile.getName() + "weblink: " + executedFile.getWebViewLink() + "thumbnailLink: " + executedFile.getThumbnailLink());
  }


  private static void setPermission(Drive drive, String fileId) {
    JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
      @Override
      public void onFailure(GoogleJsonError e,
                            HttpHeaders responseHeaders)
              throws IOException {
        // Handle error
        System.err.println(e.getMessage());
      }

      @Override
      public void onSuccess(Permission permission,
                            HttpHeaders responseHeaders)
              throws IOException {
        System.out.println("Permission ID: " + permission.getId());
      }
    };
    BatchRequest batch = drive.batch();
    Permission userPermission = new Permission()
            .setType("anyone")
            .setRole("reader");
    try {
      drive.permissions().create(fileId, userPermission)
              .setFields("id")
              .queue(batch, callback);


      batch.execute();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
