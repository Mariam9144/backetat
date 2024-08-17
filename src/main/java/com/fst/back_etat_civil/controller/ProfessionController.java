package com.fst.back_etat_civil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.server.ResponseStatusException;

import com.fst.back_etat_civil.dto.ProfessionDto;
import com.fst.back_etat_civil.model.Profession;
import com.fst.back_etat_civil.model.Region;
import com.fst.back_etat_civil.repository.ProfessionRepository;
import com.fst.back_etat_civil.service.ProfessionService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/profession")
public class ProfessionController {

    @Autowired
    private ProfessionService professionService;
    @Autowired
    private ProfessionRepository professionRepository;

    @GetMapping
    public ResponseEntity<List<ProfessionDto>> getAllProfessions() {
        List<ProfessionDto> professions = professionService.getAllProfessions();
        return new ResponseEntity<>(professions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionDto> getProfessionById(@PathVariable Long id) {
        ProfessionDto profession = professionService.getProfessionById(id);
        return new ResponseEntity<>(profession, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProfessionDto> createProfession(@RequestBody ProfessionDto professionDto) {
    	if (professionRepository.existsByLibelleIgnoreCase(professionDto.getLibelle())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Cette profession existe déjà");
        }else {
        ProfessionDto createdProfession = professionService.createProfession(professionDto);
        return new ResponseEntity<>(createdProfession, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionDto> updateProfession(@PathVariable Long id, @RequestBody ProfessionDto professionDto) {
    	if (professionRepository.existsByLibelleIgnoreCase(professionDto.getLibelle())
    			&& !professionRepository.findById(id).get().getLibelle().equals(professionDto.getLibelle())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Cette profession existe déjà");
        }else {
        ProfessionDto updatedProfession = professionService.updateProfession(id, professionDto);
        return new ResponseEntity<>(updatedProfession, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfession(@PathVariable Long id) {
        professionService.deleteProfession(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Profession>> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Profession> professions = professionService.searchProfessions(keyword, page, size);
        return ResponseEntity.ok(professions);
    }
}

