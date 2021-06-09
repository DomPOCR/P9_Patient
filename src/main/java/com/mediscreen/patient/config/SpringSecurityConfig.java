package com.mediscreen.patient.config;

import com.mediscreen.patient.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private MyUserDetailsService myUserDetailsService;

    @Autowired
    public SpringSecurityConfig (MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService=myUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Disable CSRF (cross site request forgery)
        http.csrf().disable();

        // Autorisations avec rôles
        http.authorizeRequests()
                .antMatchers("/user/**").hasAuthority("ADMIN")
                .antMatchers("/*").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/logout/**").permitAll()
                .antMatchers("/login/**").permitAll()
            .and()
                .formLogin()  //login configuration
                .defaultSuccessUrl("/home",true)
            .and()
                .logout()    //logout configuration
                .logoutSuccessUrl("/login")
                .addLogoutHandler(new SecurityContextLogoutHandler())
            .and()
                .exceptionHandling() // exception handling
                .accessDeniedPage("/403");

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public HttpTraceRepository htttpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }
}
