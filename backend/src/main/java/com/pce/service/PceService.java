package com.pce.service;

import com.pce.domain.Pce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public interface PceService {
  Page<Pce> getAllAvailablePce(Pageable pageRequest);

  Pce createOrUpdatePce(Pce pce);

}
