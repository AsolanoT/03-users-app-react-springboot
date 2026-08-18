package com.angel.backend.usersapp.backend_usersapp.auth.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.angel.backend.usersapp.backend_usersapp.models.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.angel.backend.usersapp.backend_usersapp.auth.TokenJwtConfig.*;

// Este filtro extiende UsernamePasswordAuthenticationFilter para interceptar
// la petición POST /login y automatizar el flujo de autenticación por username/password.
// Cuando el cliente envía credenciales a /login, Spring llama a attemptAuthentication,
// valida esas credenciales con AuthenticationManager y luego ejecuta los métodos
// successfulAuthentication o unsuccessfulAuthentication según el resultado.
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // Este método se ejecuta cuando llega una petición de login.
    // Lee las credenciales del cuerpo de la solicitud (JSON con username y password),
    // crea un token de autenticación y lo envía al AuthenticationManager para validar.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        User user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();

            //Me sirve para revisar en la consola de spring, que datos se estan corriendo
            // logger.info("Username desde request InputStream (raw) " + username);
            // logger.info("Password desde request InputStream (raw) " + password);
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    // Este método se ejecuta cuando la autenticación del usuario ha sido exitosa.
    // Aquí se genera el token JWT, se añade al header Authorization y se responde
    // al cliente con un JSON que incluye el token y un mensaje de bienvenida.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal())
                .getUsername();

        Collection <? extends GrantedAuthority> roles = authResult.getAuthorities();
        boolean isAdmin = roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        // Construye los datos adicionales que se almacenarán dentro del payload del JWT.
        // Solo nos interesan los nombres de los roles (String), no el objeto GrantedAuthority
        List<String> authorityNames = roles.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        // Construye los datos adicionales que se almacenarán dentro del payload del JWT.
        Claims claims = Jwts.claims()
            .add("authorities", authorityNames)
            .add("isAdmin", isAdmin)
            .add("username", username)
            .build();
        
        // Genera un JWT con el nombre de usuario como sujeto, la fecha de emisión
        // y una fecha de expiración de una hora.
        String token = Jwts.builder()
                .claims(claims)
                .subject(username)
                .signWith(SECRET_KEY)
                .issuedAt(new Date())
                .expiration (new Date(System.currentTimeMillis() + 3600000))
                .compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("message", String.format("Hola %s, has iniciado sesion con exito!", username));
        body.put("username", username);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");

    }

    // Este método se ejecuta cuando la autenticación falla, por ejemplo
    // si el username o password son incorrectos. Aquí se devuelve una respuesta 401
    // con un mensaje de error para indicar que la autenticación no fue válida.
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        Map<String, Object> body = new HashMap<>();
        body.put("message",  "Error en la autenticacion username o password incorrecto!");
        body.put( "error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus( 401);
        response.setContentType("appliaction/json");
    }

}
