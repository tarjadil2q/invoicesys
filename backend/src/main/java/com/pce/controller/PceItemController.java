package com.pce.controller;

import com.pce.domain.Pce;
import com.pce.domain.PceItem;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PceDto;
import com.pce.domain.dto.PceItemDto;
import com.pce.service.PceService;
import com.pce.util.ControllerHelper;
import org.modelmapper.ModelMapper;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Leonardo Tarjadi on 7/09/2016.
 */
@RestController
@RequestMapping("/api/v1/pce")
@ExposesResourceFor(PceItemDto.class)
public class PceItemController {

  @Autowired
  private PceService pceService;
  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PagedResourcesAssembler assembler;

  @Autowired
  private EntityLinks entityLinks;


  private static final String PCE_ITEM_URL_PATH = "/pceitem";

  @PreAuthorize("@pceUserServiceImpl.canCurrentUserCreateOrUpdatePce(principal)")
  @RequestMapping(value = "/{pceId}/pceitem/{pceItemId}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePceItemForParticularPce(@PathVariable("pceId") long pceId,
                                                                             @PathVariable("pceItemId") long pceItemId,
                                                                             @RequestBody @Valid PceItemDto pceItemDto, Errors errors) {

    Pce pce = pceService.getPceByPceId(pceId).orElseThrow(() -> new NoSuchElementException(String.format("Pce=%s not found", pceId)));
    PceItem pceItem = pceService.getPceItemByPceIdAndPceItemId(pceId, pceItemId).orElseThrow(() -> new NoSuchElementException(String.format("PceItem=%s not found", pceItemId)));

    PceItem mappedPceItem = modelMapper.map(pceItemDto, PceItem.class);
    mappedPceItem.setPceItemId(pceItem.getPceItemId());
    pceService.createOrUpdatePceItem(pce, mappedPceItem);
    pceItemDto.add(ControllerLinkBuilder.linkTo(PceItemController.class).slash(pceId).slash(PCE_ITEM_URL_PATH).slash(pceItemId).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pceItemDto, HttpStatus.OK);

  }

  @PreAuthorize("@pceUserServiceImpl.canCurrentUserCreateOrUpdatePce(principal)")
  @RequestMapping(value = "/{pceId}/pceitem", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> createPceItemForParticularPce(@PathVariable("pceId") long pceId,
                                                                             @RequestBody @Valid PceItemDto pceItemDto, Errors errors) {
    Pce pce = pceService.getPceByPceId(pceId).orElseThrow(() -> new NoSuchElementException(String.format("Pce=%s not found", pceId)));


    PceItem mappedPceItem = modelMapper.map(pceItemDto, PceItem.class);
    PceItem updatedPceItem = pceService.createOrUpdatePceItem(pce, mappedPceItem);


    pceItemDto.add(ControllerLinkBuilder.linkTo(PceItemController.class).slash(pceId).slash(PCE_ITEM_URL_PATH).slash(updatedPceItem.getPceItemId()).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pceItemDto, HttpStatus.CREATED);

  }


  @RequestMapping(value = "/{pceId}/pceitem/{pceItemId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> retrievePceItemForPce(@PathVariable("pceId") long pceId,
                                                                     @PathVariable("pceItemId") long pceItemId) {
    PceItem pceItem = pceService.getPceItemByPceIdAndPceItemId(pceId, pceItemId).orElseThrow(() -> new NoSuchElementException(String.format("PceItem=%s not found", pceItemId)));
    PceItemDto pceItemDto = modelMapper.map(pceItem, PceItemDto.class);
    pceItemDto.add(ControllerLinkBuilder.linkTo(PceItemController.class).slash(pceId).slash(PCE_ITEM_URL_PATH).slash(pceItemId).withSelfRel());

    return ControllerHelper.getResponseEntity(pceItemDto);
  }

  @RequestMapping(value = "/pceitem/pce/{pceId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<DomainObjectDTO>> getPceItemsByPceId(@PathVariable("pceId") long pceId, Pageable pageRequest) {
    Pce pce = new Pce();
    pce.setPceId(pceId);
    Page<PceItem> allPceItems = pceService.getPceItemsByPce(pce, pageRequest);
    Page<Resource<PceItemDto>> newPaged = allPceItems.map(source -> mappedPceItem(pce, source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }

  private Resource<PceItemDto> mappedPceItem(Pce pce, PceItem pceItem) {
    Link selfLink = linkTo(methodOn(PceItemController.class).retrievePceItemForPce(pce.getPceId(), pceItem.getPceItemId())).withSelfRel();
    PceItemDto pceItemDto = modelMapper.map(pceItem, PceItemDto.class);
    Link pceLink = linkTo(methodOn(PceController.class).getPceById(pce.getPceId())).withRel("pce");
    Link allPces = entityLinks.linkToCollectionResource(PceDto.class).withRel("all-pces");

    return new Resource<>(pceItemDto, selfLink, pceLink, allPces);
  }

}
