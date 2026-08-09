package com.angel.backend.usersapp.backend_usersapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.angel.backend.usersapp.backend_usersapp.models.entities.User;
import com.angel.backend.usersapp.backend_usersapp.models.request.UserRequest;
import com.angel.backend.usersapp.backend_usersapp.services.UserService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = "*") // Permite solicitudes desde cualquier origen (útil para desarrollo y pruebas)
public class UserController {
    
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // Obtiene y devuelve la lista completa de usuarios registrados.
    @GetMapping("/users")
    public List<User> list() {
        return service.findAll();
    }

    // Busca un usuario específico por su ID y devuelve la información si existe.
    @GetMapping("/users/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Optional<User> userOptional = service.findById(id);

        if(userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    // Crea un nuevo usuario con los datos enviados en el cuerpo de la petición.
    @PostMapping("/users")
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        
        if(result.hasErrors()) {
            return validation(result);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }
    
    // Actualiza los datos de un usuario ya existente, identificado por su ID.
    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest user, BindingResult result, @PathVariable Long id) {
        
        if(result.hasErrors()) {
            return validation(result);
        }
        
        Optional<User> o = service.update(user, id);
        
        if(o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // Elimina un usuario por su ID cuando este existe en la base de datos.
    @DeleteMapping("/users/{id}") 
    public ResponseEntity<?> remove(@PathVariable Long id) {
        Optional<User> o = service.findById(id);
        if(o.isPresent()) {
            service.remove(id);
            return ResponseEntity.noContent().build(); //204
        }
        return ResponseEntity.notFound().build();  
    }
    
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
