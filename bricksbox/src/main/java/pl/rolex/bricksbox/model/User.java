package pl.rolex.bricksbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.rolex.bricksbox.security.model.Role;

import java.util.*;
import java.util.stream.Collectors;

import static pl.rolex.bricksbox.security.model.Role.ADMIN;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userName;
    private String password;
    private List<String> sets;
    private List<Piece> additionalPieces;
    private Map<String, Boolean> buildPlans;

    private boolean enabled;
    private List<Role> roles;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.sets = new ArrayList<>();
        this.additionalPieces = new ArrayList<>();
        this.buildPlans = new HashMap<>();
        this.enabled = true;
        this.roles = Collections.singletonList(ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
}
