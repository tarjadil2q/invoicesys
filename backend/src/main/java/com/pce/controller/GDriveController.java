package com.pce.controller;

import com.pce.domain.GDriveFile;
import com.pce.domain.Pce;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.GDriveFileDto;
import com.pce.domain.dto.PceDto;
import com.pce.service.DriveService;
import com.pce.service.PceService;
import com.pce.util.ControllerHelper;
import com.pce.validation.validator.ValidationErrorBuilder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Leonardo Tarjadi on 28/12/2016.
 */
@RestController
@RequestMapping("/api/v1/pce/attachment")
public class GDriveController {

  @Autowired
  private PceService pceService;

  @Autowired
  private DriveService driveService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private EntityLinks entityLinks;

  @Autowired
  private PagedResourcesAssembler assembler;


  private static final Logger logger = LoggerFactory.getLogger(GDriveController.class);

  @PreAuthorize("@pceUserServiceImpl.canCurrentUserCreateOrUpdatePce(principal)")
  @RequestMapping(value = "/bypce/{pceId}", method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> uploadFile(@PathVariable("pceId") long pceId,
                                                          @RequestParam("uploadfile") MultipartFile uploadfile) {
    Optional<Pce> pceByPceId = pceService.getPceByPceId(pceId);

    if (!pceByPceId.isPresent()) {
      throw new IllegalArgumentException("Cannot find PCE with id " + pceId + " please ensure that pce with that id is exist");
    }
    GDriveFile gDriveFile = driveService.uploadFile(pceByPceId.get(), uploadfile);
    if (gDriveFile != null) {
      GDriveFileDto gDriveFileDto = modelMapper.map(gDriveFile, GDriveFileDto.class);
      gDriveFileDto.add(ControllerLinkBuilder.linkTo(GDriveController.class).slash(gDriveFile.getgDriveFileId()).withSelfRel());
      return ControllerHelper.getResponseEntityWithoutBody(gDriveFileDto, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

  }

  /*@RequestMapping(value = "/download/{driveFileId}", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@PathVariable("driveFileId") long driveFileId) {
    org.springframework.core.io.Resource file = driveService.loadFileAsResource(driveFileId);
    return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
  }*/

  /*@PreAuthorize("@pceUserServiceImpl.canCurrentUserCreateOrUpdatePce(principal)")
  @RequestMapping(value = "/multiple/bypce/{pceId}", method = RequestMethod.POST)
  public HttpEntity<Resource<DomainObjectDTO>> uploadFiles(@PathVariable("pceId") long pceId,
                                                           @RequestParam("uploadfiles") MultipartFile[] uploadfiles) {
    for (MultipartFile multipartFile : uploadfiles){

    }
    return null;
  }*/


  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<GDriveFileDto>> getGDriveFileById(@PathVariable Long id) {
    logger.debug("Retrieving GDrive File by ID " + id);
    GDriveFile gDriveFile = driveService.getDriveFileById(id).orElseThrow(() -> new NoSuchElementException(String.format("G Drive file=%s not found", id)));
    return new ResponseEntity<>(mappedGDriveFile(gDriveFile.getPce(), gDriveFile), HttpStatus.OK);
  }

  @RequestMapping(value = "/bypce/{pceId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public HttpEntity<PagedResources<GDriveFileDto>> getGDriveFilesByPceId(@PathVariable("pceId") Long pceId
          , Pageable pageRequest) {
    logger.debug("Retrieving list of GDrive files by pce id " + pceId);
    Pce pce = new Pce();
    pce.setPceId(pceId);
    Page<GDriveFile> allDriveFiles = driveService.getDriveFilesByPce(pce, pageRequest);
    Page<Resource<GDriveFileDto>> newPaged = allDriveFiles.map(source -> mappedGDriveFile(pce, source));
    return new ResponseEntity<>(assembler.toResource(newPaged), HttpStatus.OK);
  }

  @PreAuthorize("@pceUserServiceImpl.canCurrentUserCreateOrUpdatePce(principal)")
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json; charset=UTF-8")
  public HttpEntity<Resource<DomainObjectDTO>> deleteGDriveFile(@PathVariable("id") long id) {
    boolean deleted = driveService.deleteDriveFile(id);
    if (deleted) {
      return ControllerHelper.getResponseEntityWithoutBody(HttpStatus.OK);
    }
    return ValidationErrorBuilder.fromUserCreatedError("Unable to delete GDrive file with ID" + id + " Please make sure ID is valid");
  }

  private Resource<GDriveFileDto> mappedGDriveFile(Pce pce, GDriveFile gDriveFile) {
    long gDriveFileId = gDriveFile.getgDriveFileId();
    Link selfLink = linkTo(methodOn(GDriveController.class).getGDriveFileById(gDriveFileId)).withSelfRel();
    GDriveFileDto gDriveFileDto = modelMapper.map(gDriveFile, GDriveFileDto.class);
    Link pceLink = linkTo(methodOn(PceController.class).getPceById(pce.getPceId())).withRel("pce");
    Link allPces = entityLinks.linkToCollectionResource(PceDto.class).withRel("all-pces");
    return new Resource<>(gDriveFileDto, selfLink, pceLink, allPces);
  }
}
