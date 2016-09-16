package com.pce.service;

import com.pce.domain.PceApprovalRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public interface PceApprovalRoleService {

  Optional<PceApprovalRole> findPceApprovalRoleById(long id);

  Page<PceApprovalRole> findAllAvailableApprovalRole(Pageable pageRequest);

  Optional<PceApprovalRole> findPceApprovalRoleByApprovalRoleSequenceAndId(long approvalRoleId, int roleSequenceNum);

  PceApprovalRole createOrUpdatePceApprovalRole(PceApprovalRole pceApprovalRole);

}
