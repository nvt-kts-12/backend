package nvt.kts.ticketapp;

import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.security.auth.TokenAuthenticationFilter;
import nvt.kts.ticketapp.security.auth.TokenBasedAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.Base64Utils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@Profile({"test-conf"})
public class SpringSecurityTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {

        User user = new User("username", "password", Collections.singletonList(new Authority("ROLE_REGISTERED")));

        User admin = new User("admin", "password", Collections.singletonList(new Authority("ROLE_ADMIN")));

        return new InMemoryUserDetailsManager(Arrays.asList(user, admin));
    }

    @Bean
    public TokenAuthenticationFilter securityFilter() {
        return new TokenAuthenticationFilter() {
            @Override
            public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

                String token =
                        ((HttpServletRequest)request).getHeader("Authorization");
                if (token != null) {
                    String encodedCredentials = token.substring(6);
                    String decodedCredentials = new String(
                            Base64Utils.decodeFromString(encodedCredentials)
                    );
                    String[] credentials = decodedCredentials.split(":");

                    String username = credentials[0];
                    String password = credentials[1];

                    UserDetails userDetails =
                            userDetailsService().loadUserByUsername(username);

                    if (userDetails.getPassword().equals(password)) {
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                        authentication.setToken(token);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
                chain.doFilter(request, response);
            }
        };
    }
}

