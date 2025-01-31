package br.edu.ifba.xpnewsbackend.user.controller;

import br.edu.ifba.xpnewsbackend.infrastructure.dto.PageableDto;
import br.edu.ifba.xpnewsbackend.infrastructure.mapper.PageableMapper;
import br.edu.ifba.xpnewsbackend.user.dto.UserCreateDto;
import br.edu.ifba.xpnewsbackend.user.dto.UserResponseDto;
import br.edu.ifba.xpnewsbackend.user.dto.UserUpdatePasswordDto;
import br.edu.ifba.xpnewsbackend.user.entity.User;
import br.edu.ifba.xpnewsbackend.user.mapper.UserMapper;
import br.edu.ifba.xpnewsbackend.user.repository.UserProjection;
import br.edu.ifba.xpnewsbackend.user.service.UserIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("xp-news/users")
@RequiredArgsConstructor
public class UserController {

    private final UserIService service;

    /**
     * Cria um novo usuário no sistema.
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto dto){
        User user = service.create(UserMapper.toUser(dto));
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    /**
     * Retorna uma lista paginada de usuários.
     */
    @GetMapping(value = "/find-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableDto> findAll(Pageable pageable){
        Page<UserProjection> users = service.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(users));
    }

    /**
     * Busca um usuário pelo ID.
     */
    @GetMapping(value = "find-by-id", params = "id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> findById(@RequestParam ("id") Long id){
        User user = service.findById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    /**
     * Busca um usuário pelo nome de usuário.
     */
    @GetMapping(value = "find-by-username", params = "username", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> findByUsername(@RequestParam("username") String username) {
        User user = service.findByUsername(username);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    /**
     * Busca um usuário pelo e-mail.
     */
    @GetMapping(value = "find-by-email", params = "email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> findByEmail(@RequestParam("email") String email) {
        User user = service.findByEmail(email);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    /**
     * Atualiza a senha de um usuário.
     */
    @PutMapping(value = "update", params = "id", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePassword(@RequestParam ("id") Long id, @Valid @RequestBody UserUpdatePasswordDto dto){
        service.updatePassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmationPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * Deleta um usuário do sistema.
     */
    @DeleteMapping(value = "delete", params = "id", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@RequestParam ("id") Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}