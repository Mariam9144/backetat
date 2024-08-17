package com.fst.back_etat_civil.service;

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

import com.fst.back_etat_civil.dto.CommuneDto;
import com.fst.back_etat_civil.model.Cercle;
import com.fst.back_etat_civil.model.Commune;
import com.fst.back_etat_civil.repository.CercleRepository;
import com.fst.back_etat_civil.repository.CommuneRepository;

import lombok.Data;

@Data
@Service
public class CommuneService {


    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private CercleRepository cercleRepository;
   /* public List<CommuneDto> getAllCommunes() {
        List<Commune> communes = communeRepository.findAll();
        return mapToDtoList(communes);
    }
*/

    public List<CommuneDto> getAllCommunes() {
        List<Commune> communes = communeRepository.findAll();
        List<CommuneDto> communeDtos = new ArrayList<>();

        for (Commune commune : communes) {
            CommuneDto communeDto = new CommuneDto();
            communeDto.setId(commune.getId());
            communeDto.setNom(commune.getNom());
            communeDto.setCode(commune.getCode());
            communeDto.setAutre(commune.getAutre());


            // Vérifier si cercle est null avant d'accéder à son ID
            if (commune.getCercle() != null) {
                communeDto.setCercle(commune.getCercle().getId());
            } else {
                // Gérer le cas où cercle est null, par exemple en définissant un ID par défaut
                communeDto.setCercle(1); // Remplacez -1 par une valeur appropriée
            }

            communeDtos.add(communeDto);
        }

        return communeDtos;
    }
    
    public List<CommuneDto> getCommunesByCercle(long id) {
    	Cercle cercle=cercleRepository.findById(id).get();
        List<Commune> communes = communeRepository.findByCercle(cercle);
        List<CommuneDto> communeDtos = new ArrayList<>();

        for (Commune commune : communes) {
            CommuneDto communeDto = new CommuneDto();
            communeDto.setId(commune.getId());
            communeDto.setAutre(commune.getAutre());
            communeDto.setNom(commune.getNom());
            communeDto.setCode(commune.getCode());

            

            communeDtos.add(communeDto);
        }

        return communeDtos;
    }


    public CommuneDto getCommuneById(long id) {
        Optional<Commune> communeData = communeRepository.findById(id);
        if (communeData.isPresent()) {
            Commune commune = communeData.get();
            CommuneDto communeDto = new CommuneDto();
            communeDto.setId(commune.getId());
            communeDto.setNom(commune.getNom());
            communeDto.setCode(commune.getCode());
            communeDto.setAutre(commune.getAutre());


            // Vérifier si cercle est null avant d'accéder à son ID
            if (commune.getCercle() != null) {
                communeDto.setCercle(commune.getCercle().getId());
            } else {
                // Gérer le cas où cercle est null, par exemple en définissant un ID par défaut
                communeDto.setCercle(1); // Remplacez -1 par une valeur appropriée
            }

            return communeDto;
        } else {
            // Gérer le cas où aucune commune avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Commune non trouvée avec l'ID : " + id);
        }
    }


    public CommuneDto createCommune(CommuneDto communeDto) {
        Commune commune = mapToModel(communeDto);
        commune = communeRepository.save(commune);
        return mapToDto(commune);
    }

    public CommuneDto updateCommune(long id, CommuneDto updatedCommuneDto) {
        Optional<Commune> communeData = communeRepository.findById(id);
        if (communeData.isPresent()) {
            Commune commune = communeData.get();
            // Mettre à jour les champs de l'commune avec les valeurs fournies dans updatedCommuneDto
            commune.setNom(updatedCommuneDto.getNom());
            commune.setCode(updatedCommuneDto.getCode());
            commune.setAutre(updatedCommuneDto.getAutre());

            // Récupérer le Cercle correspondant à l'ID fourni dans updatedCommuneDto
            Optional<Cercle> cercleData = cercleRepository.findById(updatedCommuneDto.getCercle());
            if (cercleData.isPresent()) {
                commune.setCercle(cercleData.get());
            } else {
                // Gérer le cas où aucun Cercle correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cercle non trouvé avec l'ID : " + updatedCommuneDto.getCercle());
            }

            // Enregistrer les modifications dans la base de données
            Commune updatedCommune = communeRepository.save(commune);

            // Convertir l'commune mise à jour en CommuneDto et la retourner
            return mapToDto(updatedCommune);
        } else {
            // Gérer le cas où aucune commune avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Commune non trouvée avec l'ID : " + id);
        }
    }



    public void deleteCommune(long id) {
        communeRepository.deleteById(id);
    }

    // Méthodes utilitaires de mappage entre les entités et les DTO

    private CommuneDto mapToDto(Commune commune) {
        CommuneDto communeDto = new CommuneDto();
        communeDto.setId(commune.getId());
        communeDto.setNom(commune.getNom());
        communeDto.setCode(commune.getCode());
        communeDto.setAutre(commune.getAutre());
        communeDto.setCercle(commune.getCercle().getId());
        return communeDto;
    }

    private List<CommuneDto> mapToDtoList(List<Commune> communes) {
        return communes.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Commune mapToModel(CommuneDto communeDto) {
        Commune commune = new Commune();
        commune.setNom(communeDto.getNom());
        commune.setCode(communeDto.getCode());
        commune.setAutre(communeDto.getAutre());

        return commune;
    }
    
    public Page<Commune> searchCommunes(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.ASC, "code");
        Pageable pageable = PageRequest.of(page, size, sort);
        return communeRepository.searchByKeywordInAllColumns(searchTerm, pageable);
    }
    
    public Page<Commune> searchPayes(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.ASC, "code");
        Pageable pageable = PageRequest.of(page, size, sort);
        return communeRepository.searchByKeywordInAllColumnsPayes(searchTerm, pageable);
    }

}
