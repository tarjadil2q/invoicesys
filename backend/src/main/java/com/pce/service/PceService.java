package com.pce.service;

import com.pce.domain.CurrentUser;
import com.pce.domain.Pce;
import com.pce.domain.PceItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public interface PceService {
  Page<Pce> getAllAvailablePce(Pageable pageRequest);

  Pce createOrUpdatePce(Pce pce);

  PceItem createOrUpdatePceItem(Pce pce, PceItem pceItem);

  Optional<Pce> getPceByPceId(long id);

  Optional<PceItem> getPceItemByPceIdAndPceItemId(long pceId, long PceItemId);

  boolean approvePce(Pce pce, CurrentUser currentUser);

}
