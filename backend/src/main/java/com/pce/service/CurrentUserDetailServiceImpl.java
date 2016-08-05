package com.pce.service;

import com.pce.domain.CurrentUser;
import com.pce.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 8/02/2016.
 */
@Service
public class CurrentUserDetailServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public CurrentUserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @Override
    public CurrentUser loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUserByEmail = userService.getUserByEmail(email);
        if (!optionalUserByEmail.isPresent()){
            throw new UsernameNotFoundException(String.format("User with email=%s was not found", email));
        }
        return new CurrentUser(optionalUserByEmail.get());
    }
}
