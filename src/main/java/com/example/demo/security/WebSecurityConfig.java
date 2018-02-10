package com.example.demo.security;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.BasePasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home", "/error", "/img/**", "/css/**", "/js/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().hasRole("USER")
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, UserService userService) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setSaltSource(userService);
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        auth.authenticationProvider(provider);
    }

    @Bean
    public BasePasswordEncoder passwordEncoder() {
        return new ShaPasswordEncoder(256);
    }

    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }
}