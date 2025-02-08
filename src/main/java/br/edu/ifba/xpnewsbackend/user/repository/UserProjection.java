package br.edu.ifba.xpnewsbackend.user.repository;

public interface UserProjection {

    Long getId();
    String getFullName();
    String getEmail();
    String getUsername();
    String getRole();
}
