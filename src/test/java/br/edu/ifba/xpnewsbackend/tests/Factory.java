package br.edu.ifba.xpnewsbackend.tests;

import br.edu.ifba.xpnewsbackend.user.dto.UserCreateDto;
import br.edu.ifba.xpnewsbackend.user.dto.UserResponseDto;
import br.edu.ifba.xpnewsbackend.user.entity.User;
import br.edu.ifba.xpnewsbackend.user.repository.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

public class Factory {

    public static User createUser(){
        User user = new User("Rafael Andrade", "rafa12", "rafa@gmail.com", "senhaSegura123", User.Role.ROLE_CLIENT);
        user.setId(1L);
        return user;
    }

    public static User createExistingUser(){
        User user = new User("Carlos Oliveira", "carlos123", "carlos@gmail.com", "senhaSegura456", User.Role.ROLE_CLIENT);
        user.setId(2L);
        return user;
    }

    public static Page<UserProjection> createUserProjectionPage(Pageable pageable) {
        UserProjection user1 = new UserProjectionImpl(1L, "Rafael Andrade", "rafa12");
        UserProjection user2 = new UserProjectionImpl(2L, "Maria Silva", "mariaS");
        List<UserProjection> users = Arrays.asList(user1, user2);
        return new PageImpl<>(users, pageable, users.size());
    }

    public static UserResponseDto createUserResponseDto(){
        UserResponseDto dto = new UserResponseDto(1L, "Rafael Andrade", "rafa12", "CLIENT");
        return dto;
    }

    public static UserCreateDto createUserCreateDto(){
        UserCreateDto dto = new UserCreateDto("Rafael Andrade", "rafa@gmail.com", "rafa12", "senhaSegura123");
        return dto;
    }

}
