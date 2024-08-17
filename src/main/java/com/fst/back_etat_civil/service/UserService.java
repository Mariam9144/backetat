package com.fst.back_etat_civil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fst.back_etat_civil.model.Region;
import com.fst.back_etat_civil.model.User;
import com.fst.back_etat_civil.repository.UserRepository;

import lombok.Data;

@Service
@Data
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUser(final Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }
    
    public Page<User> searchUsers(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.ASC, "username");
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.searchByKeywordInAllColumns(searchTerm, pageable);
    }
}
