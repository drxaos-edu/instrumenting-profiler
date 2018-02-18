package com.example.demo.service;

import com.example.demo.domain.main.User;
import com.example.demo.domain.main.UserRepository;
import com.example.demo.exception.ServiceWrongArgsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.BasePasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService, SaltSource {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BasePasswordEncoder passwordEncoder;

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void setUserPassword(String username, String password) {
        User user = getUser(username);
        if (user == null) {
            throw new ServiceWrongArgsException("unknown user: " + username);
        }
        String hash = passwordEncoder.encodePassword(password, username);
        user.setPassword(hash);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    @Override
    public Object getSalt(UserDetails user) {
        return user.getUsername();
    }
}
