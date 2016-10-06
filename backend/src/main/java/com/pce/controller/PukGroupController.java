package com.pce.controller;

import com.google.common.collect.Lists;
import com.pce.domain.CurrentUser;
import com.pce.domain.PukGroup;
import com.pce.domain.User;
import com.pce.domain.dto.*;
import com.pce.service.PukGroupService;
import com.pce.service.mapper.PukGroupMapper;
import com.pce.util.ControllerHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Leonardo Tarjadi on 21/08/2016.
 */
@RestController
@RequestMapping("/api/v1/pce/pukgroup")
@ExposesResourceFor(PukGroupDto.class)
public class PukGroupController {

  private static final Logger LOG = LoggerFactory.getLogger(PukGroupController.class);

  public static final String PUK_GROUP_URL_PATH = "/pukgroup";

  private PukGroupService pukGroupService;
  private PukGroupMapper pukGroupMapper;
  private EntityLinks entityLinks;
  private ModelMapper modelMapper;
  private PagedResourcesAssembler assembler;
  private IdentifiableResourceAssemblerSupport identifiableResourceAssemblerSupport;

  @Autowired
  public PukGroupController(PukGroupService pukGroupService,
                            PukGroupMapper pukGroupMapper,
                            EntityLinks entityLinks,
                            ModelMapper modelMapper,
                            PagedResourcesAssembler assembler) {
    this.pukGroupService = pukGroupService;
    this.pukGroupMapper = pukGroupMapper;
    this.entityLinks = entityLinks;
    this.modelMapper = modelMapper;
    this.assembler = assembler;
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
    pukGroupDto.add(linkTo(PukGroupController.class).slash(pukGroup.getPukGroupId()).withRel(PUK_GROUP_URL_PATH).withSelfRel());
    return ControllerHelper.getResponseEntityWithoutBody(pukGroupDto, HttpStatus.CREATED);
  }


  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> getPukGroupById(@PathVariable Long id) {
    PukGroup pukGroup = pukGroupService.getPukGroupById(id).orElseThrow(() -> new NoSuchElementException(String.format("Puk Group=%s not found", id)));
    PukGroupDto pukGroupDto = (PukGroupDto) pukGroupMapper.mapEntityIntoDto(pukGroup);
    Link linkForPUkGroup = entityLinks.linkToSingleResource(PukGroupDto.class, pukGroupDto.getPukGroupId());
    Resource<DomainObjectDTO> userResource = new Resource<>(pukGroupDto, linkForPUkGroup);
    return new ResponseEntity<>(userResource, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getAllPukGroups(Pageable pageRequest) {
    Page<PukGroup> allPukGroups = pukGroupService.getAllAvailablePukGroup(pageRequest);
    Page<Resource<PukGroupDto>> newPaged = allPukGroups.map(source -> mappedPukGroup(source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }

  @RequestMapping(value = "/currentuser", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resources<Resource<PukGroupDto>>> getAllPukGroupsByCurrentUser(Authentication authentication) {

    List<PukGroup> allPukGroups = pukGroupService.getAllAvailablePukGroupForCurrentUser((CurrentUser) authentication.getPrincipal());
    if (CollectionUtils.isEmpty(allPukGroups)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    List<Resource<PukGroupDto>> newPukGroupList = allPukGroups.stream().map(source -> mappedPukGroup(source)).collect(Collectors.toList());
    Link link = linkTo(methodOn(PukGroupController.class).getAllPukGroupsByCurrentUser(authentication)).withSelfRel();
    Resources<Resource<PukGroupDto>> resources = new Resources<>(newPukGroupList, link);
    return new ResponseEntity<>(resources, HttpStatus.OK);
  }


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePukGroup(@PathVariable("id") long id,
                                                              @RequestBody @Valid PukGroupDto pukGroupDto) {
    Optional<PukGroup> existingPukGroup = pukGroupService.getPukGroupById(id);
    https:
//exampledriven.wordpress.com/2015/11/13/spring-hateoas-example/
    if (!existingPukGroup.isPresent()) {
      return new ResponseEntity(new Resource<>(new ApiError(HttpStatus.NOT_FOUND,
              "Puk Group to be updated not found, please check id is correct ", "Puk Group id is not found")), HttpStatus.NOT_FOUND);
    }

    PukGroup mappedPukGroup = modelMapper.map(pukGroupDto, PukGroup.class);
    mappedPukGroup.setPukGroupId(id);
    PukGroup updatedPukGroup = pukGroupService.createOrUpdatePukGroup(mappedPukGroup);
    pukGroupDto.add(linkTo(PukGroupController.class).slash(updatedPukGroup.getPukGroupId()).withRel(PUK_GROUP_URL_PATH).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pukGroupDto, HttpStatus.OK);
  }

  @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  HttpEntity<PagedResources<DomainObjectDTO>> getPukGroupByUserId(@PathVariable("userId") long id,
                                                                  Pageable pageRequest) {
    User user = new User();
    user.setId(id);
    Page<PukGroup> pukGroups = pukGroupService.getPukGroupsByUser(pageRequest, user);
    Page<Resource<PukGroupDto>> newPaged = pukGroups.map(source -> mappedPukGroup(source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);

  }

  private Resource<PukGroupDto> mappedPukGroup(PukGroup pukGroup) {
    Link selfLink = linkTo(methodOn(PukGroupController.class).getPukGroupById(pukGroup.getPukGroupId())).withSelfRel();
    PukGroupDto pukGroupDto = modelMapper.map(pukGroup, PukGroupDto.class);
    Link allUsers = entityLinks.linkToCollectionResource(UserDto.class).withRel("all-users");
    Link pukGroupUserLink = linkTo(methodOn(UserController.class).getUsersByPukGroup(pukGroup.getPukGroupId(), new PageRequest(0, 20))).withRel("user");
    Link allPuks = entityLinks.linkToCollectionResource(PukDto.class).withRel("all-puks");
    Link pukByPukGroupLink = linkTo(methodOn(PukController.class).getPuksByPukGroup(pukGroup.getPukGroupId(), new PageRequest(0, 20))).withRel("puk");
    return new Resource<>(pukGroupDto, selfLink, pukGroupUserLink, allUsers, pukByPukGroupLink, allPuks);

  }

}
