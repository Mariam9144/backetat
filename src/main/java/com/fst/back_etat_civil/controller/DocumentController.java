package com.fst.back_etat_civil.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import com.fst.back_etat_civil.dto.DocumentDto;
import com.fst.back_etat_civil.dto.DocumentDto;
import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.model.Document;
import com.fst.back_etat_civil.model.Profession;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.model.Document;
import com.fst.back_etat_civil.repository.CitoyenRepository;
import com.fst.back_etat_civil.repository.DocumentRepository;
import com.fst.back_etat_civil.service.DocumentService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/document")
public class DocumentController {
    @Autowired
    DocumentRepository documentRepository;
    
    @Autowired
    CitoyenRepository citoyenRepository;
    @Autowired
    DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<DocumentDto>> getAllDocuments() {
        List<DocumentDto> documents = documentService.getAllDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }
    
    

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id) {
        DocumentDto document = documentService.getDocumentById(id);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DocumentDto> createDocument(@RequestBody DocumentDto documentDto) {
    	
    	 try {
             Optional<Citoyen> citoyen=citoyenRepository.findById(documentDto.getCitoyen());
             
              Document document1= new Document();
              //MAPPING
              document1.setNom(documentDto.getNom());
              document1.setType(documentDto.getType());
              
              
              
              
               if(!citoyen.isPresent())
                   throw new ResponseStatusException(HttpStatus.NOT_FOUND,"VQF NOT FOUND");
               document1.setCitoyen(citoyen.get());
               document1.setNom(documentDto.getType()+"_"+citoyen.get().getNiciv());
              

              Document _document = documentRepository
                      .save(document1);
              documentDto.setId(_document.getId());

              return new ResponseEntity<>(documentDto, HttpStatus.CREATED);
          } catch (NullPointerException e) {
              return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
          }
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDto> updateDocument(@PathVariable Long id, @RequestBody DocumentDto documentDto) {
    	
        DocumentDto updatedDocument = documentService.updateDocument(id, documentDto);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
            
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    
    @GetMapping("/search")
    public ResponseEntity<Page<Document>> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Document> documents = documentService.searchDocuments(keyword, page, size);
        return ResponseEntity.ok(documents);
    }
}
