package com.angel.backend.usersapp.backend_usersapp.models.dto;

public class UserDto {
    
    private Long id;
    private String username;
    private String email;

    /**
     * Constructor sin argumentos (por defecto)
     * Inicializa un objeto dto vacío sin asignar valores a los atributos
     * Ejemplo: dto usuario = new dto();
     */
    public UserDto() {
    }

    /**
     * Constructor con argumentos
     * Inicializa un objeto dto asignando valores a todos los atributos (id, username, email)
     * Ejemplo: dto usuario = new dto(1L, "angel", "angel@email.com");
     */
    public UserDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
    
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


}
