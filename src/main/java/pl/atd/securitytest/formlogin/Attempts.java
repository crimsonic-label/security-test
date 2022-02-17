package pl.atd.securitytest.formlogin;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * To limit login attempts in our application we can use Spring Security’s isAccountNonLocked property
 * Store attempts in database
 */
@Entity
@Data
public class Attempts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private int attempts;
}
