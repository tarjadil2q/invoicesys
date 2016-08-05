package com.pce.service;

import com.pce.domain.CurrentUser;

/**
 * Created by Leonardo Tarjadi on 8/02/2016.
 */
public interface CurrentUserService {
    boolean canAccessUser(CurrentUser currentUser, Long userId);
}
