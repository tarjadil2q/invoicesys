package com.pce.service;

import java.util.concurrent.Future;

/**
 * Created by Leonardo Tarjadi on 3/01/2017.
 */
public interface EmailService {

  Future<Boolean> sendEmail(String to, String subject, String bodyText);
}
