package com.pce.service;

import com.pce.domain.CurrentUser;

/**
 * Created by Leonardo Tarjadi on 7/10/2016.
 */
public interface PukUserService {

  boolean canCurrentUserCreateOrUpdatePuk(CurrentUser currentUser);
}
