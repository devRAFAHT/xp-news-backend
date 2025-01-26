package br.edu.ifba.xpnewsbackend.user.controller;

import br.edu.ifba.xpnewsbackend.infrastructure.dto.PageableDto;
import br.edu.ifba.xpnewsbackend.infrastructure.mapper.PageableMapper;
import br.edu.ifba.xpnewsbackend.user.dto.UserCreateDto;
import br.edu.ifba.xpnewsbackend.user.dto.UserResponseDto;
import br.edu.ifba.xpnewsbackend.user.dto.UserUpdatePasswordDto;
import br.edu.ifba.xpnewsbackend.user.entity.User;
import br.edu.ifba.xpnewsbackend.user.mapper.UserMapper;
import br.edu.ifba.xpnewsbackend.user.repository.UserProjection;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("xp-news/users")
@RequiredArgsConstructor
public class UserController {

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto dto){
        User user = UserMapper.toUser(dto);
        user.setId(1L);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @GetMapping("/find-all")
    public ResponseEntity<PageableDto> findAll(Pageable pageable){
        Page<UserProjection> users = null;
        return ResponseEntity.ok(PageableMapper.toDto(users));
    }

    @GetMapping(value = "find-by-id", params = "id")
    public ResponseEntity<UserResponseDto> findById(@RequestParam ("id") Long id){
        User user = new User();
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PutMapping(value = "update", params = "id")
    public ResponseEntity<Void> updatePassword(@RequestParam ("id") Long id, @Valid @RequestBody UserUpdatePasswordDto dto){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "delete", params = "id")
    public ResponseEntity<Void> delete(@RequestParam ("id") Long id){
        return ResponseEntity.noContent().build();
    }

}
