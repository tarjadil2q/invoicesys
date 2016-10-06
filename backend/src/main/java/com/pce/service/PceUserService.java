package com.pce.service;

import com.pce.domain.CurrentUser;

/**
 * Created by Leonardo Tarjadi on 23/09/2016.
 */
public interface PceUserService {

  boolean canCurrentUserCreateOrUpdatePce(CurrentUser currentUser);
}
