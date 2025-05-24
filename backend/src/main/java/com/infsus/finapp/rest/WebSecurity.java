package com.infsus.finapp.rest;

import com.infsus.finapp.service.PersonService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = false)
public class WebSecurity {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Autowired
    private PersonService akService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(corsFilter(), CorsFilter.class);
        http.cors(withDefaults());
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/api/login")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/register")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/checkTransactions")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/account")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/currency")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/budget")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/category")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/transaction")).permitAll()
                .anyRequest().authenticated());
        http.formLogin(configurer -> {
            configurer.successHandler((request, response, authentication) -> {
                        response.setStatus(HttpStatus.NO_CONTENT.value());
                        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
                        String ime;
                        GrantedAuthority uloga;

                        ime = akService.findByEmail(email.toLowerCase()).getName();
                        uloga = ((UserDetails) authentication.getPrincipal()).getAuthorities().stream().findFirst().orElse(null);
                        response.addHeader("X-Name", ime);
                        response.addHeader("X-Role", uloga.getAuthority());
                    })
                    .failureHandler(new CustomAuthenticationFailureHandler());
        });

        http.exceptionHandling(configurer -> {
            final RequestMatcher matcher = new NegatedRequestMatcher(
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML));
            configurer
                    .defaultAuthenticationEntryPointFor((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }, matcher);
        });
        http.logout(configurer -> configurer
                .logoutUrl("/api/logout").deleteCookies("JSESSIONID").invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.NO_CONTENT.value());
                }));
        http.httpBasic(withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

}
