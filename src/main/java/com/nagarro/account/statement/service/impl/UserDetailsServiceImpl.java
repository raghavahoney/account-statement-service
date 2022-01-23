package com.nagarro.account.statement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return new User("admin", "$2a$12$OtMSX9IvsndHyvusqyzeQecyBc452Pu6HIlCb0qmO4RLgh0eRifcS",
                    getAuthorities("ROLE_ADMIN"));
        }
        else if ("user".equals(username)) {
            return new User("user", "$2a$12$gnTNzlRuA7miUhtZ7tUwfeujm8Ib02HK9ChNEHzANbFL31/7J53Zy",
                    getAuthorities("ROLE_USER"));
        }else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Collection<GrantedAuthority> getAuthorities(String role){
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
}
