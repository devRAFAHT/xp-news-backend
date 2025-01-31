package br.edu.ifba.xpnewsbackend.user.repository;

import br.edu.ifba.xpnewsbackend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("select u from User u")
    Page<UserProjection> findAllPageable(Pageable pageable);

}
