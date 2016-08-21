package com.pce.service;

import com.pce.repository.PukItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Leonardo Tarjadi on 21/08/2016.
 */
@Service
public class PukItemServiceImpl implements PukItemService {

  @Autowired
  private PukItemRepository pukItemRepository;
}
