package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.Puk;
import com.pce.domain.PukGroup;
import com.pce.domain.dto.ApiError;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukDto;
import com.pce.domain.dto.RoleDto;
import com.pce.service.PukGroupService;
import com.pce.service.PukService;
import com.pce.service.mapper.PukMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@RestController
@RequestMapping("/api/pce/puk")
@ExposesResourceFor(RoleDto.class)
public class PukController {

  private static final Logger LOG = LoggerFactory.getLogger(PukController.class);

  private PukService pukService;
  private PukGroupService pukGroupService;
  private PukMapper pukMapper;
  private EntityLinks entityLinks;
  private static final String PUK_URL_PATH = "/puk";

  @Autowired
  public PukController(PukService pukService, PukMapper pukMapper, EntityLinks entityLinks,
                       PukGroupService pukGroupService) {
    this.pukService = pukService;
    this.pukMapper = pukMapper;
    this.entityLinks = entityLinks;
    this.pukGroupService = pukGroupService;
  }

  @PreAuthorize("@currentUserServiceImpl.thisRoleCanAccess(RoleConstant.ADMIN.getName, principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getPukById(@PathVariable Long id,
                                                          Authentication authentication) {

    Puk puk = pukService.getPukByPukId(id).orElseThrow(() -> new NoSuchElementException(String.format("Puk=%s not found", id)));
    PukDto pukDto = (PukDto) pukMapper.mapEntityIntoDto(puk);
    Link linkForPuk = entityLinks.linkToSingleResource(PukDto.class, pukDto.getPukId());
    Resource<DomainObjectDTO> userResource = new Resource<>(pukDto, linkForPuk);
    return new ResponseEntity<>(userResource, HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.thisRoleCanAccess(RoleConstant.ADMIN.getName, principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPuks(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<Puk> allPuks = pukService.getAllAvailablePuk(pageRequest);
    Page<DomainObjectDTO> pukDtos = pukMapper.mapEntityPageIntoDTOPage(pageRequest, allPuks);
    return new ResponseEntity<>(assembler.toResource(pukDtos), HttpStatus.OK);
  }


  @PreAuthorize("@currentUserServiceImpl.thisRoleCanAccess(RoleConstant.ADMIN.getName, principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createPuk(@RequestBody @Valid PukDto pukDto) {

    Puk puk = pukMapper.mapDtoIntoEntity(pukDto);
    long pukGroupId = pukDto.getPukGroup().getId();
    PukGroup pukGroupById = pukGroupService.getPukGroupById(pukGroupId).orElseThrow(() -> new NoSuchElementException(String.format("Puk = %s not found", pukGroupId)));


    Optional<Puk> pukFound = pukService.getPukByPukNoIgnoreCase(pukDto.getPukNo());
    if (!pukFound.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.CONFLICT,
              "Puk already exists, please enter different Puk", Lists.newArrayList("Puk already exists"))), HttpStatus.CONFLICT);
    }

    pukService.createOrUpdatePuk(puk);
    pukDto.add(ControllerLinkBuilder.linkTo(PukController.class).slash(puk.getId()).withRel(PUK_URL_PATH).withSelfRel());

    Link resourceLink = pukDto.getLink("self");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(URI.create(resourceLink.getHref()));
    return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
  }


  @PreAuthorize("@currentUserServiceImpl.thisRoleCanAccess(RoleConstant.ADMIN.getName, principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePuk(@PathVariable("id") long id,
                                                         @RequestBody @Valid PukDto pukDto) {
    Optional<Puk> currentPuk = pukService.getPukByPukId(id);

    if (!currentPuk.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.NOT_FOUND,
              "Role to be updated not found, please check id is correct ", "Role id is not found")), HttpStatus.NOT_FOUND);
    }

    Puk pukToBeUpdate = currentPuk.get();
    //TODO Set all properties

    Puk updatedRole = pukService.createOrUpdatePuk(pukToBeUpdate);
    pukDto.add(ControllerLinkBuilder.linkTo(PukController.class).slash(updatedRole.getId()).withRel(PUK_URL_PATH).withSelfRel());

    Link resourceLink = pukDto.getLink("self");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(URI.create(resourceLink.getHref()));
    return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
  }
}
