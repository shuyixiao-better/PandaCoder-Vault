package com.pandacoder.vault.security;

import com.pandacoder.vault.entity.User;
import com.pandacoder.vault.util.RoleUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Spring Security UserDetails实现
 */
@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String id;
    private String deviceId;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean enabled;

    public static UserDetailsImpl build(User user) {
        Collection<GrantedAuthority> authorities = RoleUtil.stringToRoles(user.getRoles()).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                String.valueOf(user.getId()),
                user.getDeviceId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getEnabled()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return enabled;
    }
}

