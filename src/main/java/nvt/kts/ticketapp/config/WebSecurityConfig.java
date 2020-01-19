package nvt.kts.ticketapp.config;

import nvt.kts.ticketapp.security.TokenUtils;
import nvt.kts.ticketapp.security.auth.RestAuthenticationEntryPoint;
import nvt.kts.ticketapp.security.auth.TokenAuthenticationFilter;
import nvt.kts.ticketapp.service.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService jwtUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder((passwordEncoder()));
    }

    @Autowired
    private SecurityConfigurer securityConfigurer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // TODO delete this and uncomment code below
        //http.authorizeRequests().antMatchers("/").permitAll();

        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/event/show-events").permitAll()
                .antMatchers("/api/event/{id}").permitAll()
                .anyRequest().authenticated().and();
        http.apply(securityConfigurer);
        http.csrf().disable();

    }

    @Override
    public void configure(WebSecurity web) {
//        TODO delete this and uncomment code below
//        web.ignoring().antMatchers(HttpMethod.POST, "/**");
//        web.ignoring().antMatchers(HttpMethod.GET, "/**");
//        web.ignoring().antMatchers(HttpMethod.PUT, "/**");

        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        web.ignoring().antMatchers(HttpMethod.POST, "auth/login");
        web.ignoring().antMatchers(HttpMethod.POST, "auth/register");
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico",
                "/**/*.html", "/**/*.css", "/**/*.js");
    }
}
