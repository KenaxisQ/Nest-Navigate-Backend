package com.arista.nestnavigator.user.entity;

import com.arista.nestnavigator.user.utils.UserRole;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(name="uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private int properties_listed;

    @Column(nullable = false)
    private int properties_listing_limit;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
//
//    @Column(nullable = true)
//    private String profilePic;

    public User(String firstName, String lastName, String email, String phone, String username, String password) {
      setFirstname(firstName);
      setLastname(lastName);
      setEmail(email);
      setPhone(phone);
      setUsername(username);
      setPassword(password);
    }

    @PrePersist
    protected void onCreate() {
        initializeDefaults();
    }

    private void initializeDefaults() {
        this.createdAt = LocalDateTime.now();
        this.properties_listing_limit = 5;
        this.properties_listed = 0;
        this.role = UserRole.USER;
        this.isActive = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void incrementPropertiesListed() {
        setProperties_listed(getProperties_listed()+1);
    }
}

//User user = new User();
//user.setName("John Doe");
//user.setUserName("johndoe");
//user.setPassword("password123");
//user.setCreatedAt(LocalDateTime.now().toString());
