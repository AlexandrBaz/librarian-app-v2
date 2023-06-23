package ru.bazhenov.librarianapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.models.UserProfile;
import ru.bazhenov.librarianapp.repositories.UserProfileRepository;

import java.util.Optional;

@Service
public class UserProfileDetailsService implements UserDetailsService {
    private final UserProfileRepository userProfileRepository;
    @Autowired
    public UserProfileDetailsService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userNane) throws UsernameNotFoundException {
        Optional<UserProfile> userProfile = userProfileRepository.findUserByUserName(userNane);
        if(userProfile.isEmpty()){
            System.out.println("User not found!");
            throw new UsernameNotFoundException("User not found!");
        }
        else {
            return User.builder()
                    .username(userProfile.get().getUserName())
                    .password(userProfile.get().getPassword())
                    .roles(userProfile.get().getUserProfileRole().toString().replaceAll("ROLE_", ""))
                    .build();
        }
    }
}
