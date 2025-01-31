package br.edu.ifba.xpnewsbackend.user.entity;

import br.edu.ifba.xpnewsbackend.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class User extends PersistenceEntity {

    @Column(nullable = false, length = 50)
    private String fullName;
    @Column(nullable = false, unique = true, length = 30)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 50)
    @Enumerated(value = EnumType.STRING)
    private Role role = Role.ROLE_CLIENT;

    public enum Role {
        ROLE_ADMIN, ROLE_CLIENT
    }
}
