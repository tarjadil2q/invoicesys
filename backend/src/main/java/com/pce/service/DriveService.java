package com.pce.service;

import com.pce.domain.GDriveFile;
import com.pce.domain.Pce;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 29/12/2016.
 */
public interface DriveService {

  GDriveFile uploadFile(Pce pce, MultipartFile uploadFile);

  Optional<GDriveFile> getDriveFileById(long id);

  Resource loadFileAsResource(long fileId);

  boolean deleteDriveFile(long databaseDriveFileId);

  Page<GDriveFile> getDriveFilesByPce(Pce pce, Pageable pageRequest);


}
