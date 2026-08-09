package com.angel.backend.usersapp.backend_usersapp.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Implementa la interfaz de Spring Security para cargar usuarios desde la base de datos o desde una lógica propia.
// En este caso, se usa una validación simple para autenticar únicamente al usuario "admin".
@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Override
    // Este método se invoca cuando Spring Security intenta autenticar un usuario mediante su nombre.
    // Recibe el username y valida si existe en el sistema antes de devolver los datos del usuario.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Si el nombre de usuario no es "admin", se lanza una excepción indicando que no existe.
        if (!username.equals("admin")) {
            throw new UsernameNotFoundException(String.format("Username %s  no existe en el sistema!", username));
        }

        // Se crean los permisos/roles que tendrá el usuario autenticado.
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("Role_User"));

        // Se devuelve un objeto User de Spring Security con el username, contraseña, estado y roles.
        // Estos datos son usados por el framework para validar la autenticación y autorizar accesos.
        return new User(username,
                "12345",         // contraseña del usuario
                true,            // enabled: la cuenta está activa
                true,            // accountNonExpired: la cuenta no ha expirado
                true,            // credentialsNonExpired: las credenciales no han expirado
                true,            // accountNonLocked: la cuenta no está bloqueada
                authorities);   // roles y permisos asignados
    }

}