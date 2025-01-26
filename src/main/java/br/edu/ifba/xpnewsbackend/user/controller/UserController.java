package br.edu.ifba.xpnewsbackend.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("xp-news/users")
@RequiredArgsConstructor
public class UserController {

    @PostMapping("/create")
    public ResponseEntity<Void> create(String dto){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<String>> findAll(Pageable pageable){
        List<String> usuarios = new ArrayList<>();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping(value = "find-by-id", params = "id")
    public ResponseEntity<String> findById(@RequestParam ("id") Long id){
        return ResponseEntity.ok("João");
    }

    @PutMapping(value = "update", params = "id")
    public ResponseEntity<Void> update(@RequestParam ("id") Long id){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "delete", params = "id")
    public ResponseEntity<Void> delete(@RequestParam ("id") Long id){
        return ResponseEntity.noContent().build();
    }

}
