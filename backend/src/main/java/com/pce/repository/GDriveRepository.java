package com.pce.repository;

import com.pce.domain.GDriveFile;
import com.pce.domain.Pce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Leonardo Tarjadi on 29/12/2016.
 */
public interface GDriveRepository extends JpaRepository<GDriveFile, Long> {

  Page<GDriveFile> findByPce(Pce pce, Pageable pageRequest);
}
