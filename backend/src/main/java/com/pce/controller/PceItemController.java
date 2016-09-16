package com.pce.controller;

import com.pce.domain.Pce;
import com.pce.domain.PceItem;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PceItemDto;
import com.pce.service.PceService;
import com.pce.util.ControllerHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

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


  private static final String PCE_ITEM_URL_PATH = "/pceitem";


  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{pceId}/pceitem/{pceItemId}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> updatePceItemForParticularPce(@PathVariable("pceId") long pceId,
                                                                             @PathVariable("pceItemId") long pceItemId,
                                                                             @RequestBody @Valid PceItemDto pceItemDto, Errors errors) {


    Pce pce = pceService.getPceByPceId(pceId).orElseThrow(() -> new NoSuchElementException(String.format("Pce=%s not found", pceId)));
    PceItem pceItem = pceService.getPukItemByPukItemId(pceItemId).orElseThrow(() -> new NoSuchElementException(String.format("PceItem=%s not found", pceItemId)));

    PceItem mappedPceItem = modelMapper.map(pceItemDto, PceItem.class);
    mappedPceItem.setPceItemId(pceItem.getPceItemId());
    pceService.createOrUpdatePceItem(pce, mappedPceItem);
    pceItemDto.add(ControllerLinkBuilder.linkTo(PceItemController.class).slash(pceId).slash(PCE_ITEM_URL_PATH).slash(pceItemId).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pceItemDto, HttpStatus.OK);

  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{pceId}/pceitem", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> createPceItemForParticularPce(@PathVariable("pceId") long pceId,
                                                                             @RequestBody @Valid PceItemDto pceItemDto, Errors errors) {
    Pce pce = pceService.getPceByPceId(pceId).orElseThrow(() -> new NoSuchElementException(String.format("Pce=%s not found", pceId)));


    PceItem mappedPceItem = modelMapper.map(pceItemDto, PceItem.class);
    PceItem updatedPceItem = pceService.createOrUpdatePceItem(pce, mappedPceItem);


    pceItemDto.add(ControllerLinkBuilder.linkTo(PceItemController.class).slash(pceId).slash(PCE_ITEM_URL_PATH).slash(updatedPceItem.getPceItemId()).withSelfRel());

    return ControllerHelper.getResponseEntityWithoutBody(pceItemDto, HttpStatus.CREATED);

  }

  @PreAuthorize("@currentUserServiceImpl.isCurrentUserAdmin(principal)")
  @RequestMapping(value = "/{pceId}/pceitem/{pceItemId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> retrievePceItemForPce(@PathVariable("pceId") long pceId,
                                                                     @PathVariable("pceItemId") long pceItemId) {
    PceItem pceItem = pceService.getPceItemByPceIdAndPceItemId(pceId, pceItemId).orElseThrow(() -> new NoSuchElementException(String.format("PceItem=%s not found", pceItemId)));
    PceItemDto pceItemDto = modelMapper.map(pceItem, PceItemDto.class);
    pceItemDto.add(ControllerLinkBuilder.linkTo(PceItemController.class).slash(pceId).slash(PCE_ITEM_URL_PATH).slash(pceItemId).withSelfRel());

    return ControllerHelper.getResponseEntity(pceItemDto);
  }

}
