package com.angel.backend.usersapp.backend_usersapp.models.dto.mapper;

import com.angel.backend.usersapp.backend_usersapp.models.dto.UserDto;
import com.angel.backend.usersapp.backend_usersapp.models.entities.User;

public class DtoMapperUser {

    // Instancia estática del mapper para el patrón Builder
    // private static DtoMapperUser mapper;

    // Objeto User a ser mapeado
    private User user;

    // Constructor privado para implementar el patrón Builder
    private DtoMapperUser() {
    }

    /**
     * Crea una nueva instancia del mapper
     * @return una nueva instancia de DtoMapperUser
     */
    public static DtoMapperUser builder() {
        return new DtoMapperUser();
    }

    /**
     * Establece el objeto User a ser mapeado
     * @param user el objeto User a ser convertido a UserDto
     * @return la instancia del mapper para encadenamiento de métodos
     */
    public DtoMapperUser setUser (User user) {
        this.user = user;
        return this;
    }

    /**
     * Construye y retorna un objeto UserDto a partir del User establecido
     * @return un objeto UserDto con los datos del User
     * @throws RuntimeException si el User no ha sido establecido
     */
    public UserDto build() {

        if (user == null) {
            throw new RuntimeException("Debe pasar el entity user!");
        }

        return new UserDto(this.user.getId(), user.getUsername(), user.getEmail());

    }
}
