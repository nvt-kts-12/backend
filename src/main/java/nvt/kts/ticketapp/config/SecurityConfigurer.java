package nvt.kts.ticketapp.config;

import nvt.kts.ticketapp.security.auth.TokenAuthenticationFilter;
import nvt.kts.ticketapp.security.auth.TokenBasedAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfigurer extends
        SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private TokenAuthenticationFilter securityFilter;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(
                securityFilter, BasicAuthenticationFilter.class
        );
    }
}