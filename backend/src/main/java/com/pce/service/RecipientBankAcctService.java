package com.pce.service;

import com.pce.domain.Pce;
import com.pce.domain.RecipientBankAccount;
import com.pce.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public interface RecipientBankAcctService {

  Optional<RecipientBankAccount> findRecipientBankAccountById(long id);

  Optional<RecipientBankAccount> findRecipientBankAccountByPce(Pce pce);

  Page<RecipientBankAccount> findRecipientBankAccountByUser(User user, Pageable pageRequest);

  List<RecipientBankAccount> findRecipientBankAccountByAccountName(String name);

  Optional<RecipientBankAccount> findRecipientBankAccountByAccountNumberAndBsb(String accountNum, String bsb);

  RecipientBankAccount createOrUpdateRecipientBankAccount(RecipientBankAccount recipientBankAccount);

  Page<RecipientBankAccount> findAllRecipientBankAccount(Pageable pageRequest);
}
