package pl.atd.securitytest.formlogin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Strong hashing algorithm to encode stored passwords
     *
     * @return password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // not protected endpoints
                .antMatchers("/register**").permitAll()
                // comment to access h2-console
                // authenticate for other endpoints
                .anyRequest().authenticated()
                .and()
                // implementation of login form - login url
                .formLogin()
                // login page form
                .loginPage("/login").permitAll()
                .and()
                .logout().invalidateHttpSession(true).clearAuthentication(true).permitAll()
                // to use h2-console
                .and()
                .headers().frameOptions().disable();
    }
}
