package com.pce.repository;

import com.pce.domain.Pce;
import com.pce.domain.RecipientBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public interface RecipientBankAcctRepository extends JpaRepository<RecipientBankAccount, Long> {
  List<RecipientBankAccount> findByAcctNameIgnoreCase(String acctName);

  Optional<RecipientBankAccount> findByAcctNumberAndBsb(String acctNum, String bsb);

  Optional<RecipientBankAccount> findByAssociatedPces(Pce pce);

}
