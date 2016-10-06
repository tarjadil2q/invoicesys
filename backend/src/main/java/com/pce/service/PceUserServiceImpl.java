package com.pce.service;

import com.pce.domain.CurrentUser;
import org.springframework.stereotype.Service;

/**
 * Created by Leonardo Tarjadi on 23/09/2016.
 */
@Service
public class PceUserServiceImpl implements PceUserService {

  @Override
  public boolean canCurrentUserCreateOrUpdatePce(CurrentUser currentUser) {
    return currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().equalsIgnoreCase("Officer") ||
            role.getRoleName().equalsIgnoreCase("Comittee Head") || role.getRoleName().equalsIgnoreCase("Admin"));
  }
}
