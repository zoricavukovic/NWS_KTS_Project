package com.example.serbUber.config;

import com.example.serbUber.config.jwt.JwtAuthorizationFilter;
import com.example.serbUber.service.user.CustomUserDetailsService;
import com.example.serbUber.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    public SpringConfig(CustomUserDetailsService customUserDetailsService, UserService userService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), userService))
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS,"/**")
            .permitAll()
            .antMatchers("/auth/**")
            .permitAll()
            .antMatchers("/admins/**")
            .permitAll()
            .antMatchers("/regularUsers/register")
            .permitAll()
            .antMatchers("/drivers/register")
            .permitAll()
            .antMatchers("/regularUsers")
            .permitAll()
            .antMatchers("/drivers")
            .permitAll()
            .antMatchers("/verify")
            .permitAll()
            .antMatchers("/users/**")
            .permitAll()
            .antMatchers("/vehicle-type-infos")
            .permitAll()
            .antMatchers("/verify/send-code-again")
            .permitAll()
            .antMatchers("/drivings/{email}")
            .permitAll()
            .antMatchers("/drivings/details/{id}")
            .permitAll()
            .antMatchers("/drivers/rating/{email}")
            .permitAll()
            .antMatchers("/vehicles/rating/{id}")
            .permitAll()
            .antMatchers("/emails/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic();
    }
    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}