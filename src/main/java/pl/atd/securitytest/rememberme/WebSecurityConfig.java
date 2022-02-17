package pl.atd.securitytest.rememberme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * for managing users
     * @return in memory users details service
     */
    @Bean
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("user")
                        .password(passwordEncoder().encode("12345")).roles("USER")
                        .build());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // disable Cross-Site Request Forgery protection just for demonstration
                .csrf().disable()
                // all requests to be authenticated
                .authorizeRequests().anyRequest().access("hasRole('ROLE_USER')")
                .and()
                // generate login form
                .formLogin().permitAll()
                .and()
                // adds remember me checkbox to login form, generating remember-me cookie when checked
                // the cookie stores user's identity - detect the cookie in future to automate the login
                // As a result, the user can access the application again without logging in again.
                .rememberMe()
                .and()
                // logout functionality - invalidate session, clears remember-me cookie,
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login").deleteCookies("remember-me");
    }
}
