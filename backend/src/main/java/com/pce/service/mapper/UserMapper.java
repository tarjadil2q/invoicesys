package com.pce.service.mapper;

import com.google.common.base.Preconditions;
import com.pce.domain.User;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.RoleDto;
import com.pce.domain.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Leonardo Tarjadi on 8/08/2016.
 */
@Component
public class UserMapper implements EntityToDTOMapper<User> {

  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public DomainObjectDTO mapEntityIntoDTO(User userEntity) {
    List<RoleDto> roleDtos = userEntity.getRoles().stream().map(role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toList());

    UserDto userDto = modelMapper.map(userEntity, UserDto.class);
    userDto.setRoles(roleDtos);
    return userDto;
  }

  public User mapDtoIntoEntity(UserDto userDto) {
    User user = modelMapper.map(userDto, User.class);
    user.setPasswordHash(bCryptPasswordEncoder.encode(userDto.getPassword()));
    return user;
  }

  @Override
  public List<DomainObjectDTO> mapEntitiesIntoDTO(Iterable<User> entities) {
    Preconditions.checkArgument(entities != null, "User entities cannot be null");
    List<DomainObjectDTO> userObjectDtos = new ArrayList<>();
    entities.forEach(user -> userObjectDtos.add(mapEntityIntoDTO(user)));
    return userObjectDtos;
  }

  @Override
  public Page<DomainObjectDTO> mapEntityPageIntoDTOPage(Pageable pageRequest, Page<User> pageEntity) {
    List<DomainObjectDTO> roleObjectDTOs = mapEntitiesIntoDTO(pageEntity.getContent());
    return new PageImpl<>(roleObjectDTOs, pageRequest, pageEntity.getTotalElements());
  }
}
