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
import com.fst.back_etat_civil.dto.CondamnationDto;
import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.model.Condamnation;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.repository.CitoyenRepository;
import com.fst.back_etat_civil.repository.CondamnationRepository;

import lombok.Data;

@Data
@Service
public class CondamnationService {

	@Autowired
    private CondamnationRepository condamnationRepository;
    
    @Autowired
    private CitoyenRepository citoyenRepository;
    
   

    public List<CondamnationDto> getAllCondamnations() {
        List<Condamnation> condamnations = condamnationRepository.findAll();
        List<CondamnationDto> condamnationDtos = new ArrayList<>();

        for (Condamnation condamnation : condamnations) {
            CondamnationDto condamnationDto = new CondamnationDto();
            condamnationDto.setId(condamnation.getId());
            condamnationDto.setJuridiction(condamnation.getJuridiction());
            condamnationDto.setNatureDelitCrime(condamnation.getNatureDelitCrime());
            condamnationDto.setQuantum(condamnation.getQuantum());
            condamnationDto.setDateCondamnation(condamnation.getDateCondamnation());
            condamnationDto.setDateDelitCrime(condamnation.getDateDelitCrime());
            condamnationDto.setDateDetention(condamnation.getDateDetention());
            

            // Vérifier si region est null avant d'accéder à son ID
            if (condamnation.getCitoyen() != null) {
            	condamnationDto.setCitoyen(condamnation.getCitoyen().getId());
            } else {
                // Gérer le cas où region est null, par exemple en définissant un ID par défaut
            	condamnationDto.setCitoyen(1); // Remplacez -1 par une valeur appropriée
            }

            condamnationDtos.add(condamnationDto);
        }

        return condamnationDtos;
    }
    
    public List<CondamnationDto> getCondamnationsByCitoyen(long id) {
    	Citoyen commune=citoyenRepository.findById(id).get();
        List<Condamnation> condamnations = condamnationRepository.findByCitoyen(commune);
        List<CondamnationDto> condamnationDtos = new ArrayList<>();

        for (Condamnation condamnation : condamnations) {
            CondamnationDto condamnationDto = new CondamnationDto();
            condamnationDto.setId(condamnation.getId());
            condamnationDto.setJuridiction(condamnation.getJuridiction());
            condamnationDto.setNatureDelitCrime(condamnation.getNatureDelitCrime());
            condamnationDto.setDateCondamnation(condamnation.getDateCondamnation());
            condamnationDto.setQuantum(condamnation.getQuantum());
            condamnationDto.setDateDelitCrime(condamnation.getDateDelitCrime());
            condamnationDto.setDateDetention(condamnation.getDateDetention());

         // Vérifier si cercle est null avant d'accéder à son ID
            if (condamnation.getCitoyen() != null) {
                condamnationDto.setCitoyen(condamnation.getCitoyen().getId());
            } else {
                // Gérer le cas où cercle est null, par exemple en définissant un ID par défaut
                condamnationDto.setCitoyen(1); // Remplacez -1 par une valeur appropriée
            }

            condamnationDtos.add(condamnationDto);
        }

        return condamnationDtos;
    }

    public CondamnationDto getCondamnationById(long id) {
        Optional<Condamnation> condamnationData = condamnationRepository.findById(id);
        if (condamnationData.isPresent()) {
            Condamnation condamnation = condamnationData.get();
            CondamnationDto condamnationDto = new CondamnationDto();
            condamnationDto.setId(condamnation.getId());
            condamnationDto.setJuridiction(condamnation.getJuridiction());
            condamnationDto.setNatureDelitCrime(condamnation.getNatureDelitCrime());
            condamnationDto.setQuantum(condamnation.getQuantum());
            condamnationDto.setDateCondamnation(condamnation.getDateCondamnation());
            condamnationDto.setDateDelitCrime(condamnation.getDateDelitCrime());
            condamnationDto.setDateDetention(condamnation.getDateDetention());
            


            // Vérifier si cercle est null avant d'accéder à son ID
            if (condamnation.getCitoyen() != null) {
            	condamnationDto.setCitoyen(condamnation.getCitoyen().getId());
            } else {
                // Gérer le cas où cercle est null, par exemple en définissant un ID par défaut
            	condamnationDto.setCitoyen(1); // Remplacez -1 par une valeur appropriée
            }

            return condamnationDto;
        } else {
            // Gérer le cas où aucune commune avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Condamnation non trouvée avec l'ID : " + id);
        }
    }

    public CondamnationDto createCondamnation(CondamnationDto condamnationDto) throws IOException {
        Condamnation condamnation = mapToModel(condamnationDto);
        Citoyen citoyen=citoyenRepository.findById(condamnationDto.getCitoyen()).get();
        condamnation.setCitoyen(citoyen);
        
        condamnation = condamnationRepository.save(condamnation);
        
        return mapToDto(condamnation);
    }
    

    public CondamnationDto updateCondamnation(long id, CondamnationDto condamnation) {
        Optional<Condamnation> condamnationData = condamnationRepository.findById(id);
        if (condamnationData.isPresent()) {
            Condamnation condamnation1 = condamnationRepository.findById(id).get();
            // Mettre à jour les champs de l'commune avec les valeurs fournies dans updatedCitoyenDto
            //condamnation1.setId(condamnation.getId());
            condamnation1.setJuridiction(condamnation.getJuridiction());
            condamnation1.setNatureDelitCrime(condamnation.getNatureDelitCrime());
            condamnation1.setQuantum(condamnation.getQuantum());
            condamnation1.setDateCondamnation(condamnation.getDateCondamnation());
            condamnation1.setDateDelitCrime(condamnation.getDateDelitCrime());
            condamnation1.setDateDetention(condamnation.getDateDetention());
            

            // Récupérer la Citoyen correspondant à l'ID fourni dans condamnationDto
            Optional<Citoyen> citoyenData = citoyenRepository.findById(condamnation.getCitoyen());
            if (citoyenData.isPresent()) {
            	condamnation1.setCitoyen(citoyenData.get());
            } else {
                // Gérer le cas où aucun Cercle correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cercle non trouvé avec l'ID : " + condamnation.getCitoyen());
            }

            // Enregistrer les modifications dans la base de données
            Condamnation updatedCondamnation = condamnationRepository.save(condamnation1);

            // Convertir l'commune mise à jour en CitoyenDto et la retourner
            return mapToDto(updatedCondamnation);
        } else {
            // Gérer le cas où aucune commune avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Condamnation non trouvée avec l'ID : " + id);
        }
    }

    public void deleteCondamnation(long id) {
        condamnationRepository.deleteById(id);
    }

    // Méthodes utilitaires de mappage entre les entités et les DTO

    private CondamnationDto mapToDto(Condamnation condamnation) {
        CondamnationDto condamnationDto = new CondamnationDto();
        condamnationDto.setId(condamnation.getId());
        condamnationDto.setId(condamnation.getId());
        condamnationDto.setJuridiction(condamnation.getJuridiction());
        condamnationDto.setNatureDelitCrime(condamnation.getNatureDelitCrime());
        condamnationDto.setQuantum(condamnation.getQuantum());
        condamnationDto.setDateCondamnation(condamnation.getDateCondamnation());
        condamnationDto.setDateDelitCrime(condamnation.getDateDelitCrime());
        condamnationDto.setDateDetention(condamnation.getDateDetention());
        
        return condamnationDto;
    }
    
    public Page<Condamnation> searchCondamnations(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return condamnationRepository.searchByKeywordInAllColumns(searchTerm, pageable);
    }

    private List<CondamnationDto> mapToDtoList(List<Condamnation> condamnations) {
        return condamnations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Condamnation mapToModel(CondamnationDto condamnationDto) {
        Condamnation condamnation = new Condamnation();
        condamnation.setId(condamnation.getId());
        condamnation.setJuridiction(condamnationDto.getJuridiction());
        condamnation.setNatureDelitCrime(condamnationDto.getNatureDelitCrime());
        condamnationDto.setQuantum(condamnation.getQuantum());
        condamnation.setDateCondamnation(condamnationDto.getDateCondamnation());
        condamnation.setDateDelitCrime(condamnationDto.getDateDelitCrime());
        condamnation.setDateDetention(condamnationDto.getDateDetention());
        
        return condamnation;
    }
    
    
}
