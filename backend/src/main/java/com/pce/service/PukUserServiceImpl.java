package com.pce.service;

import com.pce.domain.CurrentUser;
import org.springframework.stereotype.Service;

/**
 * Created by Leonardo Tarjadi on 7/10/2016.
 */
@Service
public class PukUserServiceImpl implements PukUserService {

  @Override
  public boolean canCurrentUserCreateOrUpdatePuk(CurrentUser currentUser) {
    return currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equalsIgnoreCase("Comittee Head") || role.getRoleName().equalsIgnoreCase("Admin"));
  }
}
