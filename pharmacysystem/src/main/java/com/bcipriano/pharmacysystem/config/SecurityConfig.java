package com.bcipriano.pharmacysystem.config;

import com.bcipriano.pharmacysystem.security.JwtAuthFilter;
import com.bcipriano.pharmacysystem.security.JwtService;
import com.bcipriano.pharmacysystem.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/discounts/**")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/employees/**")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/losses/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/lots/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/merchandises/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/purchases/**")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/returns/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/sales/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/saleItems/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v1/suppliers/**")
                .hasRole("ADMIN")
                .antMatchers("/api/v1/clients/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/usuarios/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}