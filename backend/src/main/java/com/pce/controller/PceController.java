package com.pce.controller;

import com.pce.domain.dto.PceDto;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Leonardo Tarjadi on 5/09/2016.
 */
@RestController
@RequestMapping("/api/v1/pce/pce")
@ExposesResourceFor(PceDto.class)
public class PceController {
}
