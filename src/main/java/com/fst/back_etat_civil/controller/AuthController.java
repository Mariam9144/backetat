package com.fst.back_etat_civil.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Cacheable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.springframework.web.server.ResponseStatusException;

import com.fst.back_etat_civil.dto.VqfDto;
import com.fst.back_etat_civil.model.ERole;
import com.fst.back_etat_civil.model.Region;
import com.fst.back_etat_civil.model.Role;
import com.fst.back_etat_civil.model.User;
import com.fst.back_etat_civil.repository.RoleRepository;
import com.fst.back_etat_civil.repository.UserRepository;
import com.fst.back_etat_civil.request.LoginRequest;
import com.fst.back_etat_civil.request.PasswordRequest;
import com.fst.back_etat_civil.request.SignupRequest;
import com.fst.back_etat_civil.response.JwtResponse;
import com.fst.back_etat_civil.security.jwt.JwtUtils;
import com.fst.back_etat_civil.service.UserService;
import com.fst.back_etat_civil.services.UserDetailsImpl;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    @ApiOperation(value = "Add Project")
    
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        System.out.println(authentication.getPrincipal());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<Role> roles=userRepository.findByUsername(loginRequest.getUsername()).get().getRoles();

        /*List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());*/

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
    
    @PostMapping("/signup")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce nom utilisateur existe déjà");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email existe déjà");
        }

        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok("L'utilisateur est enregistré avec succès");
    }


	
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String username) {
        try {
            List<User> users;

            if (username != null) {
                Optional<User> userOptional = userRepository.findByUsername(username);
                if (userOptional.isPresent()) {
                    users = Collections.singletonList(userOptional.get());
                } else {
                    users = Collections.emptyList();
                }
            } else {
                users = userRepository.findAll();
            }

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable("id") long id) {
        Optional<User> usersData = userRepository.findById(id);

        if (usersData.isPresent()) {
            return new ResponseEntity<>(usersData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/userByName/{username}")
    public ResponseEntity<User> getUsersByUsername(@PathVariable("username") String username) {
        Optional<User> usersData = userRepository.findByUsername(username);

        if (usersData.isPresent()) {
            return new ResponseEntity<>(usersData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //////////////////////////

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUsers(@PathVariable("id") long id, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setUsername(user.getUsername());
            _user.setEmail(user.getEmail());
            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/editPassword/{id}")
    public ResponseEntity<User> updatePassword(@PathVariable("id") long id, @RequestBody PasswordRequest passwordRequest) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            User _user = userData.get();
           if(passwordRequest.getConfirmation().equals(passwordRequest.getNewPassword()) &&
        		   encoder.matches(passwordRequest.getPassword(), _user.getPassword())) {
        	   _user.setPassword(encoder.encode(passwordRequest.getNewPassword()));
            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);}
           else {
        	   if(!passwordRequest.getConfirmation().equals(passwordRequest.getNewPassword()) &&
        		   encoder.matches(passwordRequest.getPassword(), _user.getPassword())) {
        		   throw new ResponseStatusException(HttpStatus.CONFLICT, "Mot de passe et confirmation differents !!!");
        	   }
        	   if(passwordRequest.getConfirmation().equals(passwordRequest.getNewPassword()) &&
            		   !encoder.matches(passwordRequest.getPassword(), _user.getPassword())) {
            		   throw new ResponseStatusException(HttpStatus.CONFLICT, "L'ancien mot de passe incorrect !!!");
            	   }
        	   if(!passwordRequest.getConfirmation().equals(passwordRequest.getNewPassword()) &&
            		   !encoder.matches(passwordRequest.getPassword(), _user.getPassword())) {
        		   throw new ResponseStatusException(HttpStatus.CONFLICT, "L'ancien mot de passe incorrect et nouveau Mot de passe  different de la confirmation !!!");
        	   }
        	   return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        	   }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/users/{id}")
    @ApiOperation(value = "Delete Project")

    public ResponseEntity<HttpStatus> deleteUsers(@PathVariable("id") long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @GetMapping("/search")
    public ResponseEntity<Page<User>> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<User> users = userService.searchUsers(keyword, page, size);
        return ResponseEntity.ok(users);
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
    
    
    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userId, @RequestParam ERole roleName) {
        Optional<User> optionalUser = userService.getUser(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();

        // Recherche du rôle dans les rôles de l'utilisateur
        Optional<Role> optionalRole = user.getRoles().stream()
                .filter(role -> role.getName() == roleName)
                .findFirst();

        if (!optionalRole.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found for this user");
        }

        Role role = optionalRole.get();

        // Supprimer le rôle de la liste des rôles de l'utilisateur
        user.getRoles().remove(role);

        // Sauvegardez l'utilisateur mis à jour dans la base de données
        userService.saveUser(user);

        return ResponseEntity.ok(user);
    }

    
    @PostMapping("/addRole")
    public ResponseEntity<Role> createRole(@RequestParam ERole eRole) {
    	
        Role role = new Role();
        role.setName(eRole);
        roleRepository.save(role);
        return new ResponseEntity<>(role, HttpStatus.CREATED);
        
    }

    
/*
    @DeleteMapping("/userss/{usersId}/roles/{roleId}")
    public ResponseEntity<HttpStatus> deleteRoleFromUsers(@PathVariable(value = "usersId") Long usersId, @PathVariable(value = "roleId") Long roleId) throws ResourceNotFoundException {
        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Users with id = " + usersId));

        users.removeRole(roleId);
        usersRepository.save(users);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    */

    //////////////////////////////////
    /*
    @PutMapping("/usernameupdate/{id}")
    public ResponseEntity updateUsername(@PathVariable("id") long id,@Valid @RequestBody UsernameUpdateRequest usernameUpdateRequest) {
        Optional user = usersRepository.findById(id);
        if (usersRepository.existsByUsername(usernameUpdateRequest.getUsername())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        else{
            user.setusername(usernameUpdateRequest.getusername())
            return ResponseEntity
                    .body(new MessageResponse("Username is updated!"));
        }
    }*/


}

