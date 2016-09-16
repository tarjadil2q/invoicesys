package com.pce.repository;

import com.pce.domain.PceApprovalRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public interface PceApprovalRoleRepository extends JpaRepository<PceApprovalRole, Long> {

  Optional<PceApprovalRole> findByApprovalRoleSequenceAndRoleId(int roleSequenceNumber,
                                                                long approvalRoleId);
}
