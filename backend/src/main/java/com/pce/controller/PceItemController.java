package com.pce.controller;

import com.pce.domain.dto.PceItemDto;
import com.pce.service.PceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Leonardo Tarjadi on 7/09/2016.
 */
@RestController
@RequestMapping("/api/v1/pce")
@ExposesResourceFor(PceItemDto.class)
public class PceItemController {

  @Autowired
  private PceService pceService;


}
