package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.PukGroup;
import com.pce.domain.dto.ApiError;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukGroupDto;
import com.pce.service.PukGroupService;
import com.pce.service.mapper.PukGroupMapper;
import com.pce.util.ControllerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 21/08/2016.
 */
@RestController
@RequestMapping("/api/pce/pukgroup")
@ExposesResourceFor(PukGroupDto.class)
public class PukGroupController {

  private static final Logger LOG = LoggerFactory.getLogger(PukGroupController.class);

  public static final String PUK_GROUP_URL_PATH = "/pukgroup";

  private PukGroupService pukGroupService;
  private PukGroupMapper pukGroupMapper;
  private EntityLinks entityLinks;

  @Autowired
  public PukGroupController(PukGroupService pukGroupService,
                            PukGroupMapper pukGroupMapper,
                            EntityLinks entityLinks) {
    this.pukGroupService = pukGroupService;
    this.pukGroupMapper = pukGroupMapper;
    this.entityLinks = entityLinks;
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> createPukGroup(@RequestBody @Valid PukGroupDto pukGroupDto) {

    PukGroup pukGroup = pukGroupMapper.mapDtoIntoEntity(pukGroupDto);
    List<PukGroup> pukGroupFound = pukGroupService.findPukGroupByPukGroupNameIgnoreCase(pukGroupDto.getPukGroupName());
    if (!CollectionUtils.isEmpty(pukGroupFound)) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.CONFLICT,
              "Puk Group already exists, please enter different Puk Group Name", Lists.newArrayList("Puk Group already exists"))), HttpStatus.CONFLICT);
    }

    pukGroupService.createOrUpdatePukGroup(pukGroup);
    pukGroupDto.add(ControllerLinkBuilder.linkTo(PukGroupController.class).slash(pukGroup.getPukGroupId()).withRel(PUK_GROUP_URL_PATH).withSelfRel());
    return ControllerHelper.getResponseEntityWithoutBody(pukGroupDto, HttpStatus.CREATED);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getPukGroupById(@PathVariable Long id) {
    PukGroup pukGroup = pukGroupService.getPukGroupById(id).orElseThrow(() -> new NoSuchElementException(String.format("Puk Group=%s not found", id)));
    PukGroupDto pukGroupDto = (PukGroupDto) pukGroupMapper.mapEntityIntoDto(pukGroup);
    Link linkForPUkGroup = entityLinks.linkToSingleResource(PukGroupDto.class, pukGroupDto.getPukGroupId());
    Resource<DomainObjectDTO> userResource = new Resource<>(pukGroupDto, linkForPUkGroup);
    return new ResponseEntity<>(userResource, HttpStatus.OK);
  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPukGroups(Pageable pageRequest, PagedResourcesAssembler assembler) {
    Page<PukGroup> allPukGroups = pukGroupService.getAllAvailablePukGroup(pageRequest);
    Page<DomainObjectDTO> pukGroupDtos = pukGroupMapper.mapEntityPageIntoDTOPage(pageRequest, allPukGroups);
    return new ResponseEntity<>(assembler.toResource(pukGroupDtos), HttpStatus.OK);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePukGroup(@PathVariable("id") long id,
                                                              @RequestBody @Valid PukGroupDto pukGroupDto) {
    Optional<PukGroup> existingPukGroup = pukGroupService.getPukGroupById(id);

    if (!existingPukGroup.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.NOT_FOUND,
              "Puk Group to be updated not found, please check id is correct ", "Puk Group id is not found")), HttpStatus.NOT_FOUND);
    }
    PukGroup toBeUpdatePukGroup = existingPukGroup.get();
    PukGroup mappedPukGroup = new PukGroup();
    mappedPukGroup.setPukGroupId(id);
    //PukGroup mappedPukGroup = pukGroupMapper.mapDtoIntoEntity(pukGroupDto);

    PukGroup updatedPukGroup = pukGroupService.createOrUpdatePukGroup(mappedPukGroup);
    pukGroupDto.add(ControllerLinkBuilder.linkTo(PukGroupController.class).slash(updatedPukGroup.getPukGroupId()).withRel(PUK_GROUP_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pukGroupDto, HttpStatus.OK);
  }


}
