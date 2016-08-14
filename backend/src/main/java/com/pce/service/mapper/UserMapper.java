package com.pce.service.mapper;

import com.pce.domain.User;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.RoleDto;
import com.pce.domain.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Leonardo Tarjadi on 8/08/2016.
 */
@Component
public class UserMapper extends AbstractEntityToDTOMapper<User, DomainObjectDTO> implements EntityToDTOMapper {

  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public DomainObjectDTO mapEntityIntoDto(User userEntity) {
    List<RoleDto> roleDtos = userEntity.getRoles().stream().map(role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toList());

    UserDto userDto = modelMapper.map(userEntity, UserDto.class);
    userDto.setRoles(roleDtos);
    return userDto;
  }

  @Override
  public User mapDtoIntoEntity(DomainObjectDTO userDto) {
    User user = modelMapper.map(userDto, User.class);
    user.setPasswordHash(bCryptPasswordEncoder.encode(((UserDto) userDto).getPassword()));
    return user;
  }


}
