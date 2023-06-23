package ru.bazhenov.librarianapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bazhenov.librarianapp.models.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findUserByUserName(String userName);
    Optional<UserProfile> findUserByEmail(String email);
}
