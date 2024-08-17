package com.fst.back_etat_civil.controller;

import java.util.List;
import java.util.Optional;

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

import com.fst.back_etat_civil.dto.CondamnationDto;
import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.model.Condamnation;
import com.fst.back_etat_civil.repository.CitoyenRepository;
import com.fst.back_etat_civil.repository.CondamnationRepository;
import com.fst.back_etat_civil.service.CondamnationService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/condamnation")
public class CondamnationController {
    @Autowired
    CondamnationRepository condamnationRepository;
    
    @Autowired
    CitoyenRepository citoyenRepository;
    @Autowired
    CondamnationService condamnationService;

    @GetMapping
    public ResponseEntity<List<CondamnationDto>> getAllCondamnations() {
        List<CondamnationDto> condamnations = condamnationService.getAllCondamnations();
        return new ResponseEntity<>(condamnations, HttpStatus.OK);
    }
    
    

    @GetMapping("/{id}")
    public ResponseEntity<CondamnationDto> getCondamnationById(@PathVariable Long id) {
        CondamnationDto condamnation = condamnationService.getCondamnationById(id);
        return new ResponseEntity<>(condamnation, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CondamnationDto> createCondamnation(@RequestBody CondamnationDto condamnationDto) {
    	
    	 try {
             Optional<Citoyen> citoyen=citoyenRepository.findById(condamnationDto.getCitoyen());
             
              Condamnation condamnation1= new Condamnation();
              //MAPPING
              condamnation1.setNatureDelitCrime(condamnationDto.getNatureDelitCrime());
              condamnation1.setJuridiction(condamnationDto.getJuridiction());
              condamnation1.setQuantum(condamnationDto.getQuantum());
              condamnation1.setDateCondamnation(condamnationDto.getDateCondamnation());
              condamnation1.setDateDelitCrime(condamnationDto.getDateDelitCrime());
              condamnation1.setDateDetention(condamnationDto.getDateDetention());
              
              
              
              
               if(!citoyen.isPresent())
                   throw new ResponseStatusException(HttpStatus.NOT_FOUND,"VQF NOT FOUND");
               condamnation1.setCitoyen(citoyen.get());
             

              Condamnation _condamnation = condamnationRepository
                      .save(condamnation1);
              condamnationDto.setId(_condamnation.getId());

              return new ResponseEntity<>(condamnationDto, HttpStatus.CREATED);
          } catch (NullPointerException e) {
              return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
          }
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<CondamnationDto> updateCondamnation(@PathVariable Long id, @RequestBody CondamnationDto condamnationDto) {
    	
        CondamnationDto updatedCondamnation = condamnationService.updateCondamnation(id, condamnationDto);
        return new ResponseEntity<>(updatedCondamnation, HttpStatus.OK);
            
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCondamnation(@PathVariable Long id) {
        condamnationService.deleteCondamnation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    
    @GetMapping("/search")
    public ResponseEntity<Page<Condamnation>> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Condamnation> condamnations = condamnationService.searchCondamnations(keyword, page, size);
        return ResponseEntity.ok(condamnations);
    }
}
