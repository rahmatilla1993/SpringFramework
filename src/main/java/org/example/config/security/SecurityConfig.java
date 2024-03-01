package org.example.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          CustomAuthenticationFailureHandler customAuthenticationFailureHandler
    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/css/*", "/js/*","/auth/*")
                                .permitAll()
                                .anyRequest()
                                .fullyAuthenticated()
                );
        http
                .formLogin(form ->
                        form.loginPage("/auth/login")
                                .usernameParameter("uname")
                                .passwordParameter("pswd")
                                .loginProcessingUrl("/login/process")
                                .defaultSuccessUrl("/", true)
                                .failureHandler(customAuthenticationFailureHandler)
                );
        http
                .logout(logout ->
                        logout.logoutUrl("/auth/logout")
                                .deleteCookies("JSESSIONID")
                                .clearAuthentication(true)
                                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                );
        http
                .rememberMe(rem ->
                        rem.rememberMeParameter("rememberMe")
                                .rememberMeCookieName("rem-me")
                                .tokenValiditySeconds(10 * 60)
                                .key("secretKey")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
