package br.edu.ifba.xpnewsbackend.tests;

import br.edu.ifba.xpnewsbackend.user.repository.UserProjection;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserProjectionImpl implements UserProjection {

    @EqualsAndHashCode.Include
    private final Long id;
    private final String fullName;
    private final String email;
    private final String username;
    private final String role;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getRole() {
        return role;
    }

}