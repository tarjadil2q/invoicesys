package com.pce.service.mapper;

import com.pce.controller.*;
import com.pce.domain.Pce;
import com.pce.domain.dto.PceDto;
import com.pce.domain.dto.PukDto;
import com.pce.domain.dto.RecipientBankAccountDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Leonardo Tarjadi on 9/10/2016.
 */
@Service
public class PceMapper {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private EntityLinks entityLinks;

  public Resource<PceDto> mappedPce(Pce pce) {
    long pceId = pce.getPceId();
    Link selfLink = linkTo(methodOn(PceController.class).getPceById(pceId)).withSelfRel();
    PceDto pceDto = modelMapper.map(pce, PceDto.class);
    Link pukLink = linkTo(methodOn(PukController.class).getPukByPceId(pceId)).withRel("puk");
    Link allPuk = entityLinks.linkToCollectionResource(PukDto.class).withRel("all-puks");
    Link pceItems = linkTo(methodOn(PceItemController.class).getPceItemsByPceId(pceId, new PageRequest(0, 20))).withRel("pce-item");
    Link recipient = linkTo(methodOn(RecipientBankAcctController.class).getRecipientByPceId(pceId)).withRel("recipient");
    Link allRecipient = entityLinks.linkToCollectionResource(RecipientBankAccountDto.class).withRel("all-recipient");
    Link approvers = linkTo(methodOn(UserController.class).getApproversByPceId(pceId)).withRel("current-approvers");
    return new Resource<>(pceDto, selfLink, pukLink, allPuk, pceItems, recipient, allRecipient, approvers);
  }


}
