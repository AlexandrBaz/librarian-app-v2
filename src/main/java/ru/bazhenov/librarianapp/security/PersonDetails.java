package ru.bazhenov.librarianapp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.bazhenov.librarianapp.models.Person;

import java.util.Collection;
import java.util.Collections;

public record PersonDetails(Person person) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(person.getPersonRole().toString()));
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.person.getIsBanned();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.person.getPassIsExpired();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
