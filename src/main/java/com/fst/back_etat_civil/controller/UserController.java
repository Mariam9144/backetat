package com.fst.back_etat_civil.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fst.back_etat_civil.model.ERole;
import com.fst.back_etat_civil.model.Role;
import com.fst.back_etat_civil.model.User;
import com.fst.back_etat_civil.repository.RoleRepository;
import com.fst.back_etat_civil.repository.UserRepository;
import com.fst.back_etat_civil.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String nom) {
        try {
            List<User>users = new ArrayList<User>();

            if (nom == null)
                userRepository.findAll().forEach(users::add);
            //else
            //usersRepository.findByNomContaining(nom).forEach(userss::add);

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable("id") Long id) {
        Optional<User> userData =userRepository.findById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<User> createUsers(@RequestBody User user) {
        try {
            User _users =userRepository
                    .save(user);
            return new ResponseEntity<>(_users, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        Optional<User>usersData =userRepository.findById(id);

        if (usersData.isPresent()) {
            User _users =usersData.get();


            return new ResponseEntity<>(userRepository.save(_users), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        try {
            userRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    @PostMapping("/{userId}/roles")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long userId, @RequestParam ERole roleName) {
        Optional<User> optionalUser = userService.getUser(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if (!optionalRole.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
        }

        User user = optionalUser.get();
        Role role = optionalRole.get();

        user.getRoles().add(role);
        userService.saveUser(user);

        return ResponseEntity.ok(user);
    }


}
