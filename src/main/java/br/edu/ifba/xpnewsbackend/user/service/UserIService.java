package br.edu.ifba.xpnewsbackend.user.service;

import br.edu.ifba.xpnewsbackend.infrastructure.dto.PageableDto;
import br.edu.ifba.xpnewsbackend.user.dto.UserResponseDto;
import br.edu.ifba.xpnewsbackend.user.entity.User;
import br.edu.ifba.xpnewsbackend.user.repository.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserIService {

    User create(User user);
    Page<UserProjection> findAll(Pageable pageable);
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    void updatePassword(Long id, String currentPassword, String newPassword, String confirmationPassword);
    void delete(Long id);
    PageableDto findAllWithClient();

}
