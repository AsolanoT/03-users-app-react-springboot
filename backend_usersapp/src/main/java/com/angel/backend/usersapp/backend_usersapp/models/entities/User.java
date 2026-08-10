package com.angel.backend.usersapp.backend_usersapp.models.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_username", columnNames = {"username"}),
        @UniqueConstraint(name = "UK_email", columnNames = {"email"})
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(unique = true, name = "username", nullable = false)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Email(message = "Email should be valid")
    @Column(unique = true, name = "email", nullable = false)
    private String email;

    @ManyToMany
    @JoinTable(
        name = "users_roles", 
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name="role_id"),
        uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "role_id"})})
    private List<Role> roles;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    } 

}
