package com.pce.util;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * Created by Leonardo Tarjadi on 21/08/2016.
 */
public class ControllerHelper {

  public static ResponseEntity getResponseEntityWithoutBody(ResourceSupport resourceDto, HttpStatus httpStatus) {
    Link resourceLink = resourceDto.getLink("self");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(URI.create(resourceLink.getHref()));
    return new ResponseEntity<>(null, httpHeaders, httpStatus);
  }

  public static ResponseEntity getResponseEntity(ResourceSupport resourceDto) {
    Resource<ResourceSupport> pukResource = new Resource<>(resourceDto);
    return new ResponseEntity<>(pukResource, HttpStatus.OK);
  }
}
