package br.edu.ifba.xpnewsbackend.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("xp-news/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/find-all")
    public ResponseEntity<List<String>> findAll(Pageable pageable){
        List<String> usuarios = new ArrayList<>();
        return ResponseEntity.ok(usuarios);
    }

}
