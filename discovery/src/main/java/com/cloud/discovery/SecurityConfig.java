package com.cloud.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {

        try {
            auth.inMemoryAuthentication()
                    .withUser("discUser")
                    .password("{noop}discPassword")
                    .roles("SYSTEM");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void configure(HttpSecurity http) {
        try {
            http
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
                    .requestMatchers().antMatchers("/eureka/**").and()
                    .authorizeRequests()
                    .antMatchers("/eureka/**").hasRole("SYSTEM")
                    .anyRequest().denyAll().and()
                    .httpBasic().and()
                    .csrf().disable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
