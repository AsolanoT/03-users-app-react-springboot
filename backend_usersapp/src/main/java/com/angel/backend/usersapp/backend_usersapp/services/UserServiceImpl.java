package com.angel.backend.usersapp.backend_usersapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.angel.backend.usersapp.backend_usersapp.models.dto.UserDto;
import com.angel.backend.usersapp.backend_usersapp.models.dto.mapper.DtoMapperUser;
import com.angel.backend.usersapp.backend_usersapp.models.entities.Role;
import com.angel.backend.usersapp.backend_usersapp.models.entities.User;
import com.angel.backend.usersapp.backend_usersapp.models.request.UserRequest;
import com.angel.backend.usersapp.backend_usersapp.repositories.RoleRepository;
import com.angel.backend.usersapp.backend_usersapp.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = (List<User>) repository.findAll();

        return users
                .stream()
                .map(u -> DtoMapperUser.builder().setUser(u).build())
        .collect(Collectors.toList());      
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        return repository.findById(id).map(u -> DtoMapperUser
                .builder()
                .setUser(u)
                .build()
            );
    }

    @Override
    @Transactional
    public UserDto save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> o = roleRepository.findByName("ROLE_USER");

        List<Role> roles = new ArrayList<>();
        
        if (o.isPresent()) {
            roles.add(o.orElseThrow());
        }
        user.setRoles(roles);

        return DtoMapperUser.builder().setUser(repository.save(user)).build();
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<UserDto> update(UserRequest user, Long id) {

        Optional<User> o = repository.findById(id);
        User userOptional = null;

        if(o.isPresent()) {
            User userDb = o.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOptional = repository.save(userDb);
        }

        return Optional.ofNullable(DtoMapperUser.builder().setUser(userOptional).build());
    }

}
