package ru.netology.cloudstorage.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import ru.netology.cloudstorage.repo.CloudJwtSecurityRepo;
import ru.netology.cloudstorage.security.filters.CloudSecurityExceptionHandlerFilter;
import ru.netology.cloudstorage.security.filters.CloudTokenAuthenticationFilter;
import ru.netology.cloudstorage.security.CloudUserDetailsService;
import ru.netology.cloudstorage.security.jwt.JwtAuthServiceImpl;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class CloudSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CloudUserDetailsService userDetailsService;
    private final CloudJwtSecurityRepo jwtSecurityRepo;
    private final JwtAuthServiceImpl jwtAuthService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .logout().disable()
                .addFilterBefore(cloudSecurityHandler(), LogoutFilter.class)
                .addFilterBefore(cloudTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/login").permitAll()
                .and()
                .authorizeRequests().antMatchers("/logout").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated(); //failureHandler(cloudExceptionHandler);;;
    }

    @Bean
    public CloudTokenAuthenticationFilter cloudTokenFilter() {
        return new CloudTokenAuthenticationFilter(jwtSecurityRepo, userDetailsService, jwtAuthService);
    }

    @Bean
    public CloudSecurityExceptionHandlerFilter cloudSecurityHandler() {
        return new CloudSecurityExceptionHandlerFilter();
    }

    @Bean
    public FilterRegistrationBean<CloudTokenAuthenticationFilter> myFilterRegistrationBean(CloudTokenAuthenticationFilter filter) {
        FilterRegistrationBean<CloudTokenAuthenticationFilter> frb = new FilterRegistrationBean<>(filter, new ServletRegistrationBean[0]);
        frb.setEnabled(false);
        return frb;
    }


}