package br.edu.ifba.xpnewsbackend.user.entity;

import br.edu.ifba.xpnewsbackend.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class User extends PersistenceEntity {

    private String fullName;
    private String username;
    private String email;
    private String password;
    private Role role = Role.ROLE_CLIENT;

    public enum Role {
        ROLE_ADMIN, ROLE_CLIENT
    }
}
