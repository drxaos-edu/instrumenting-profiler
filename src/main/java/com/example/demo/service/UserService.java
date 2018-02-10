package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.domain.UserRepository;
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

    public User getUser(String login) {
        return userRepository.findByLogin(login);
    }


    @Transactional
    public void setUserPassword(String login, String password) {
        User user = getUser(login);
        if (user == null) {
            throw new ServiceWrongArgsException("unknown user: " + login);
        }
        String hash = passwordEncoder.encodePassword(password, login);
        user.setPassword(hash);
        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUser(username);
    }

    @Override
    public Object getSalt(UserDetails user) {
        return user.getUsername();
    }
}
