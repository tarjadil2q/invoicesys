package com.pce.service;

import com.pce.domain.PceApprovalRole;
import com.pce.domain.Role;
import com.pce.repository.PceApprovalRoleRepository;
import com.pce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
@Service
public class PceApprovalRoleServiceImpl implements PceApprovalRoleService {

  @Autowired
  private PceApprovalRoleRepository pceApprovalRoleRepository;

  @Autowired
  private RoleRepository roleRepository;


  @Override
  public Optional<PceApprovalRole> findPceApprovalRoleById(long id) {
    return Optional.ofNullable(pceApprovalRoleRepository.findOne(id));
  }

  @Override
  public Page<PceApprovalRole> findAllAvailableApprovalRole(Pageable pageRequest) {
    return pceApprovalRoleRepository.findAll(pageRequest);
  }

  @Override
  public Optional<PceApprovalRole> findPceApprovalRoleByApprovalRoleSequenceAndId(long approvalRoleId, int roleSequenceNum) {
    Optional<PceApprovalRole> byApprovalRoleSequence = pceApprovalRoleRepository.findByApprovalRoleSequenceAndRoleId(roleSequenceNum,
            approvalRoleId);
    return byApprovalRoleSequence;
  }

  @Transactional
  @Override
  public PceApprovalRole createOrUpdatePceApprovalRole(PceApprovalRole pceApprovalRole) {
    Role role = roleRepository.findOne(pceApprovalRole.getRoleId());
    pceApprovalRole.setPceApprovalRole(role);
    pceApprovalRole.setRoleId(0);
    return pceApprovalRoleRepository.save(pceApprovalRole);
  }


  @Override
  public List<PceApprovalRole> findAllAvailableApprovalRoleOrderBySequenceNoAsc() {
    return pceApprovalRoleRepository.findAllByOrderByApprovalRoleSequenceAsc();
  }
}
