package com.pce.service;

import com.pce.domain.RecipientBankAccount;
import com.pce.repository.RecipientBankAcctRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public class RecipientBankAccountServiceImpl implements RecipientBankAcctService {

  @Autowired
  private RecipientBankAcctRepository recipientBankAcctRepository;


  @Override
  public Optional<RecipientBankAccount> findRecipientBankAccountById(long id) {
    return Optional.ofNullable(recipientBankAcctRepository.findOne(id));
  }

  @Override
  public List<RecipientBankAccount> findRecipientBankAccountByAccountName(String acctName) {
    return recipientBankAcctRepository.findByAcctNameIgnoreCase(acctName);
  }

  @Override
  public Optional<RecipientBankAccount> findRecipientBankAccountByAccountNumberAndBsb(String accountNum, String bsb) {
    return recipientBankAcctRepository.findByAccountNumberAndBsb(accountNum, bsb);
  }

  @Override
  public RecipientBankAccount createOrUpdateRecipientBankAccount(RecipientBankAccount recipientBankAccount) {
    return recipientBankAcctRepository.save(recipientBankAccount);
  }

  @Override
  public Page<RecipientBankAccount> findAllRecipientBankAccount(Pageable pageRequest) {
    return recipientBankAcctRepository.findAll(pageRequest);
  }
}
