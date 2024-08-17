package com.fst.back_etat_civil.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fst.back_etat_civil.controller.JasperController;
import com.fst.back_etat_civil.dto.DocumentDto;
import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.model.Document;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.repository.CitoyenRepository;
import com.fst.back_etat_civil.repository.DocumentRepository;

import lombok.Data;

@Data
@Service
public class DocumentService {

	@Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private CitoyenRepository citoyenRepository;
    
   

    public List<DocumentDto> getAllDocuments() {
        List<Document> documents = documentRepository.findAll();
        List<DocumentDto> documentDtos = new ArrayList<>();

        for (Document document : documents) {
            DocumentDto documentDto = new DocumentDto();
            documentDto.setId(document.getId());
            documentDto.setType(document.getType());
            documentDto.setNom(document.getNom());
            

            // Vérifier si region est null avant d'accéder à son ID
            if (document.getCitoyen() != null) {
            	documentDto.setCitoyen(document.getCitoyen().getId());
            } else {
                // Gérer le cas où region est null, par exemple en définissant un ID par défaut
            	documentDto.setCitoyen(1); // Remplacez -1 par une valeur appropriée
            }

            documentDtos.add(documentDto);
        }

        return documentDtos;
    }
    
    public List<DocumentDto> getDocumentsByCitoyen(long id) {
    	Citoyen commune=citoyenRepository.findById(id).get();
        List<Document> documents = documentRepository.findByCitoyen(commune);
        List<DocumentDto> documentDtos = new ArrayList<>();

        for (Document document : documents) {
            DocumentDto documentDto = new DocumentDto();
            documentDto.setId(document.getId());
            
            documentDto.setNom(document.getNom());
            documentDto.setType(document.getType());

         // Vérifier si cercle est null avant d'accéder à son ID
            if (document.getCitoyen() != null) {
                documentDto.setCitoyen(document.getCitoyen().getId());
            } else {
                // Gérer le cas où cercle est null, par exemple en définissant un ID par défaut
                documentDto.setCitoyen(1); // Remplacez -1 par une valeur appropriée
            }

            documentDtos.add(documentDto);
        }

        return documentDtos;
    }

    public DocumentDto getDocumentById(long id) {
        Optional<Document> documentData = documentRepository.findById(id);
        if (documentData.isPresent()) {
            Document document = documentData.get();
            DocumentDto documentDto = new DocumentDto();
            documentDto.setId(document.getId());
            documentDto.setNom(document.getNom());
            documentDto.setType(document.getType());
            


            // Vérifier si cercle est null avant d'accéder à son ID
            if (document.getCitoyen() != null) {
            	documentDto.setCitoyen(document.getCitoyen().getId());
            } else {
                // Gérer le cas où cercle est null, par exemple en définissant un ID par défaut
            	documentDto.setCitoyen(1); // Remplacez -1 par une valeur appropriée
            }

            return documentDto;
        } else {
            // Gérer le cas où aucune commune avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document non trouvée avec l'ID : " + id);
        }
    }

    public DocumentDto createDocument(DocumentDto documentDto) throws IOException {
        Document document = mapToModel(documentDto);
        Citoyen citoyen=citoyenRepository.findById(documentDto.getCitoyen()).get();
        document.setCitoyen(citoyen);
        document.setNom(documentDto.getType()+"_"+citoyen.getNiciv());
        document = documentRepository.save(document);
        
        return mapToDto(document);
    }
    

    public DocumentDto updateDocument(long id, DocumentDto documentDto) {
        Optional<Document> documentData = documentRepository.findById(id);
        if (documentData.isPresent()) {
            Document document = documentData.get();
            // Mettre à jour les champs de l'commune avec les valeurs fournies dans updatedCitoyenDto
            document.setNom(documentDto.getNom());
            document.setType(documentDto.getType());
            

            // Récupérer la Citoyen correspondant à l'ID fourni dans documentDto
            Optional<Citoyen> citoyenData = citoyenRepository.findById(documentDto.getCitoyen());
            if (citoyenData.isPresent()) {
            	document.setCitoyen(citoyenData.get());
            } else {
                // Gérer le cas où aucun Cercle correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cercle non trouvé avec l'ID : " + documentDto.getCitoyen());
            }

            // Enregistrer les modifications dans la base de données
            Document updatedDocument = documentRepository.save(document);

            // Convertir l'commune mise à jour en CitoyenDto et la retourner
            return mapToDto(updatedDocument);
        } else {
            // Gérer le cas où aucune commune avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document non trouvée avec l'ID : " + id);
        }
    }

    public void deleteDocument(long id) {
        documentRepository.deleteById(id);
    }

    // Méthodes utilitaires de mappage entre les entités et les DTO

    private DocumentDto mapToDto(Document document) {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(document.getId());
        documentDto.setNom(document.getNom());
        documentDto.setType(document.getType());
        
        return documentDto;
    }
    
    public Page<Document> searchDocuments(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return documentRepository.searchByKeywordInAllColumns(searchTerm, pageable);
    }

    private List<DocumentDto> mapToDtoList(List<Document> documents) {
        return documents.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Document mapToModel(DocumentDto documentDto) {
        Document document = new Document();
        document.setNom(documentDto.getNom());
        document.setType(documentDto.getType());
        
        return document;
    }
    
    
}
