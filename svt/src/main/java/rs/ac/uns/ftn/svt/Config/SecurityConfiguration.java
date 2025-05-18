package rs.ac.uns.ftn.svt.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    public final static String ROLE_ADMIN = "ROLE_ADMIN";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/check-email").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/api/facilities/manager/{userId}/facility/{facilityId}/reviews").permitAll()
                        //.requestMatchers(HttpMethod.POST, "/api/comments").permitAll()


                        // .requestMatchers(HttpMethod.POST, "/api/v1/users/approve/{id}").permitAll()//.hasRole("ADMIN")   // Ova ruta zahteva ADMIN rolu
                        //.requestMatchers(HttpMethod.GET, "/api/v1/facilities/rate").permitAll()

                        // .requestMatchers(HttpMethod.POST, "/api/v1/users/approve/{id}").permitAll()//.hasRole("ADMIN")  // Ova ruta zahteva ADMIN rolu
                        //.requestMatchers(HttpMethod.POST, "/api/v1/users/reject/{id}").permitAll()//.hasRole("ADMIN")   // Ova ruta zahteva ADMIN rolu
                       // .requestMatchers(HttpMethod.GET, "/api/v1/users/requests").permitAll()
                       // .requestMatchers(HttpMethod.POST, "/api/v1/users/change-password/{userId}").permitAll()
                       // .requestMatchers(HttpMethod.PUT, "/api/v1/users/update/{userId}").permitAll()


                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
