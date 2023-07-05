package ru.bazhenov.librarianapp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.bazhenov.librarianapp.dto.PersonDto;

import java.util.Collection;
import java.util.Collections;

public record PersonDetails(PersonDto personDto) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(personDto.getPersonRole().toString()));
    }

    @Override
    public String getPassword() {
        return this.personDto.getPassword();
    }

    @Override
    public String getUsername() {
        return this.personDto.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.personDto.getIsBanned();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.personDto.getPassIsExpired();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
