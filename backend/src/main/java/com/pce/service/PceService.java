package com.pce.service;

import com.pce.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public interface PceService {
  Page<Pce> getAllAvailablePce(Pageable pageRequest);

  Page<Pce> getAvailablePceByPukId(long pukId, Pageable pageRequest);


  Pce createOrUpdatePce(Pce pce);

  PceItem createOrUpdatePceItem(Pce pce, PceItem pceItem);

  Optional<Pce> getPceByPceId(long id);


  Optional<PceItem> getPceItemByPceIdAndPceItemId(long pceId, long PceItemId);

  boolean approvePce(Pce pce, CurrentUser currentUser);

  boolean rejectPce(Pce pce, CurrentUser currentUser);

  Role getNextApproverOrRejecterRole(Set<User> currentApprovers, List<Role> listOfValidRole);

  Page<PceItem> getPceItemsByPce(Pce pce, Pageable pageRequest);

  Page<Pce> findAllPceToBeApproved(CurrentUser currentUser, Pageable pageRequest);

  boolean deletePceItemAndPce(long pceId, long pceItemId);

  boolean deletePce(long pceId);

}
