package com.pce.controller;

import com.pce.domain.Puk;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukDto;
import com.pce.domain.dto.RoleDto;
import com.pce.service.PukService;
import com.pce.service.mapper.PukMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@RestController
@RequestMapping("/api/pce/puk")
@ExposesResourceFor(RoleDto.class)
public class PukController {

  private static final Logger LOG = LoggerFactory.getLogger(PukController.class);

  private PukService pukService;
  private PukMapper pukMapper;
  private EntityLinks entityLinks;

  @Autowired
  public PukController(PukService pukService, PukMapper pukMapper, EntityLinks entityLinks) {
    this.pukService = pukService;
    this.pukMapper = pukMapper;
    this.entityLinks = entityLinks;
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getRoleById(@PathVariable Long id) {
    Puk puk = pukService.getPukByPukId(id).orElseThrow(() -> new NoSuchElementException(String.format("Puk=%s not found", id)));
    PukDto pukDto = (PukDto) pukMapper.mapEntityIntoDto(puk);
    Link linkForPuk = entityLinks.linkToSingleResource(PukDto.class, pukDto.getPukId());
    Resource<DomainObjectDTO> userResource = new Resource<>(pukDto, linkForPuk);
    return new ResponseEntity<>(userResource, HttpStatus.OK);
  }
}
