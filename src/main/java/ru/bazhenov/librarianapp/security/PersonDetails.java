package ru.bazhenov.librarianapp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.bazhenov.librarianapp.models.UserProfile;

import java.util.Collection;
import java.util.Collections;

public record PersonDetails(UserProfile userProfile) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(userProfile.getUserProfileRole().toString()));
    }

    @Override
    public String getPassword() {
        return this.userProfile.getPassword();
    }

    @Override
    public String getUsername() {
        System.out.println(this.userProfile.getUserName() + "Это из базы");
        return this.userProfile.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.userProfile.getIsBanned();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.userProfile.getPassIsExpired();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
