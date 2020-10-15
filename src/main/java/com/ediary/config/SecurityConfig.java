package com.ediary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests(authorize -> {
            authorize.mvcMatchers("/h2-console/**").permitAll();
        })
                .httpBasic()
                .and().csrf().ignoringAntMatchers("/h2-console/**", "/api/**");

        http.headers().frameOptions().sameOrigin();

    }
}
