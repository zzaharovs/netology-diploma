package ru.netology.cloudstorage.configuration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.netology.cloudstorage.security.CloudTokenAuthenticationFilter;
import ru.netology.cloudstorage.security.CloudUserDetailsService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class CloudSecurityConfig extends WebSecurityConfigurerAdapter{

    private final CloudUserDetailsService userDetailsService;
    private final CloudTokenAuthenticationFilter tokenAuthenticationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilter(tokenAuthenticationFilter)
                .authorizeRequests().antMatchers("/login").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public FilterRegistrationBean<CloudTokenAuthenticationFilter> myFilterRegistrationBean(CloudTokenAuthenticationFilter filter) {
        FilterRegistrationBean<CloudTokenAuthenticationFilter> frb = new FilterRegistrationBean<>(filter, new ServletRegistrationBean[0]);
        frb.setEnabled(false);
        return frb;
    }



}