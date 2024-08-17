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
import org.webjars.NotFoundException;

import com.fst.back_etat_civil.dto.CercleDto;
import com.fst.back_etat_civil.model.Cercle;
import com.fst.back_etat_civil.model.Region;
import com.fst.back_etat_civil.repository.CercleRepository;
import com.fst.back_etat_civil.repository.RegionRepository;

import lombok.Data;

@Data
@Service
public class CercleService {

    @Autowired
    private CercleRepository cercleRepository;
    @Autowired
    private RegionRepository regionRepository;
   /* public List<CercleDto> getAllCercles() {
        List<Cercle> cercles = cercleRepository.findAll();
        return mapToDtoList(cercles);
    }
*/
    
    public List<CercleDto> getCerclesByReg(long id) {
    	Region region=regionRepository.findById(id).get();
        List<Cercle> cercles = cercleRepository.findByRegion(region);
        List<CercleDto> cercleDtos = new ArrayList<>();

        for (Cercle cercle : cercles) {
            CercleDto cercleDto = new CercleDto();
            cercleDto.setId(cercle.getId());
            cercleDto.setAutre(cercle.getAutre());
            cercleDto.setNom(cercle.getNom());
            cercleDto.setCode(cercle.getCode());

            

            cercleDtos.add(cercleDto);
        }

        return cercleDtos;
    }

    public List<CercleDto> getAllCercles() {
        List<Cercle> cercles = cercleRepository.findAll();
        List<CercleDto> cercleDtos = new ArrayList<>();

        for (Cercle cercle : cercles) {
            CercleDto cercleDto = new CercleDto();
            cercleDto.setId(cercle.getId());
            cercleDto.setAutre(cercle.getAutre());
            cercleDto.setNom(cercle.getNom());
            cercleDto.setCode(cercle.getCode());

            // Vérifier si region est null avant d'accéder à son ID
            if (cercle.getRegion() != null) {
                cercleDto.setRegion(cercle.getRegion().getId());
            } else {
                // Gérer le cas où region est null, par exemple en définissant un ID par défaut
                cercleDto.setRegion(1); // Remplacez -1 par une valeur appropriée
            }

            cercleDtos.add(cercleDto);
        }

        return cercleDtos;
    }


    public CercleDto getCercleById(long id) {
    	Optional<Cercle> cercleOptional = cercleRepository.findById(id);
        if (cercleOptional.isPresent()) {
            Cercle cercle = cercleOptional.get();
            return mapToDto(cercle);
            
        } else {
            throw new NotFoundException("Cercle not found with id: " + id);
        }
    }


    public CercleDto createCercle(CercleDto cercleDto) {
        Cercle cercle = mapToModel(cercleDto);
        cercle = cercleRepository.save(cercle);
        return mapToDto(cercle);
    }

    public CercleDto updateCercle(long id, CercleDto updatedCercleDto) {
        Optional<Cercle> cercleData = cercleRepository.findById(id);
        if (cercleData.isPresent()) {
            Cercle cercle = cercleData.get();
            // Mettre à jour les champs de l'cercle avec les valeurs fournies dans updatedCercleDto
            cercle.setNom(updatedCercleDto.getNom());
            cercle.setCode(updatedCercleDto.getCode());
            cercle.setAutre(updatedCercleDto.getAutre());

            // Récupérer le Region correspondant à l'ID fourni dans updatedCercleDto
            Optional<Region> regionData = regionRepository.findById(updatedCercleDto.getRegion());
            if (regionData.isPresent()) {
                cercle.setRegion(regionData.get());
            } else {
                // Gérer le cas où aucun Region correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Region non trouvé avec l'ID : " + updatedCercleDto.getRegion());
            }

            // Enregistrer les modifications dans la base de données
            Cercle updatedCercle = cercleRepository.save(cercle);

            // Convertir l'cercle mise à jour en CercleDto et la retourner
            return mapToDto(updatedCercle);
        } else {
            // Gérer le cas où aucune cercle avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cercle non trouvée avec l'ID : " + id);
        }
    }



    public void deleteCercle(long id) {
        cercleRepository.deleteById(id);
    }

    // Méthodes utilitaires de mappage entre les entités et les DTO

    private CercleDto mapToDto(Cercle cercle) {
        CercleDto cercleDto = new CercleDto();
        cercleDto.setId(cercle.getId());
        cercleDto.setNom(cercle.getNom());
        cercleDto.setCode(cercle.getCode());

        cercleDto.setAutre(cercle.getAutre());
        cercleDto.setRegion(cercle.getRegion().getId());
        return cercleDto;
    }

    private List<CercleDto> mapToDtoList(List<Cercle> cercles) {
        return cercles.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Cercle mapToModel(CercleDto cercleDto) {
        Cercle cercle = new Cercle();
        cercle.setNom(cercleDto.getNom());
        cercle.setCode(cercleDto.getCode());
        cercle.setAutre(cercleDto.getAutre());
        return cercle;
    }
    
    public Page<Cercle> searchCercles(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.ASC, "code");
        Pageable pageable = PageRequest.of(page, size, sort);
        return cercleRepository.searchByKeywordInAllColumns(searchTerm, pageable);
    }
}
