package com.pce.controller;

import com.pce.domain.Pce;
import com.pce.domain.PceApprovalRole;
import com.pce.domain.Role;
import com.pce.domain.User;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PceDto;
import com.pce.exception.InvalidMailException;
import com.pce.service.EmailService;
import com.pce.service.PceApprovalRoleService;
import com.pce.service.PceService;
import com.pce.service.UserService;
import com.pce.util.ControllerHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Leonardo Tarjadi on 3/01/2017.
 */
@RestController
@RequestMapping("/api/v1/pce/mail")
public class GMailController {

  private static final Logger logger = LoggerFactory.getLogger(GMailController.class);
  @Autowired
  private EmailService emailService;

  @Autowired
  private PceService pceService;

  @Autowired
  private PceApprovalRoleService pceApprovalRoleService;

  @Autowired
  private UserService userService;

  @Autowired
  private ModelMapper modelMapper;


  @PreAuthorize("@pceUserServiceImpl.isCurrentUserOfficerOrAdmin(principal)")
  @RequestMapping(value = "/bypce/{pceId}", method = RequestMethod.PUT)
  public HttpEntity<Resource<DomainObjectDTO>> emailApproval(@PathVariable("pceId") long pceId) {

    Pce pce = pceService.getPceByPceId(pceId).orElseThrow(() -> new NoSuchElementException(String.format("Pce=%s not found", pceId)));

    if (!CollectionUtils.isEmpty(pce.getApprovers())) {
      throw new InvalidMailException("Unable to manually sent email for committe head approval as there is already approval for this PCE");
    }

    List<PceApprovalRole> validApprovalRoles = pceApprovalRoleService.findAllAvailableApprovalRoleOrderBySequenceNoAsc();
    List<Role> approvalRoles = validApprovalRoles.stream().map(pceRole -> pceRole.getPceApprovalRole()).collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(approvalRoles)) {
      Optional<User> userByRole = userService.getUserByRole(approvalRoles.get(0));
      if (userByRole.isPresent()) {
        Future<Boolean> messageFuture = emailService.sendEmail(userByRole.get().getEmail(), "GKY Sydney PCE waiting for approval", "There is PCE waiting for your approval. " +
                "Please go to this " + "<a href=" + linkTo(PceApprovalController.class).slash("/currentuser").toString() + ">" + "link" + "</a>");
        while (!messageFuture.isDone()) {
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            logger.error("Thread got interrupted while waiting for email to be sent");
            throw new InvalidMailException("Unable to sent email for PCE " + pceId);
          }
        }
        if (messageFuture.isDone()) {
          PceDto pceDto = modelMapper.map(pce, PceDto.class);
          pceDto.add(linkTo(methodOn(GMailController.class).emailApproval(pceId)).withSelfRel());

          return ControllerHelper.getResponseEntityWithoutBody(pceDto, HttpStatus.OK);
        }
      }

    }
    throw new InvalidMailException("Unable to sent email for PCE " + pceId);

  }


}
