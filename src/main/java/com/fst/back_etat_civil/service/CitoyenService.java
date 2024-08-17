package com.fst.back_etat_civil.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fst.back_etat_civil.dto.CitoyenDto;
import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.model.Profession;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.repository.CitoyenRepository;
import com.fst.back_etat_civil.repository.ProfessionRepository;
import com.fst.back_etat_civil.repository.VqfRepository;

import lombok.Data;

@Data
@Service
public class CitoyenService {
    @Autowired
    private CitoyenRepository citoyenRepository;
    @Autowired
    private VqfRepository vqfRepository;
    
    @Autowired
    private ProfessionRepository professionRepository;
   /* public List<CitoyenDto> getAllCitoyens() {
        List<Citoyen> citoyens = citoyenRepository.findAll();
        return mapToDtoList(citoyens);
    }
*/

    public List<CitoyenDto> getAllCitoyens() {
        List<Citoyen> citoyens = citoyenRepository.findAll();
        List<CitoyenDto> citoyenDtos = new ArrayList<>();

        for (Citoyen citoyen : citoyens) {
            CitoyenDto citoyenDto = new CitoyenDto();
            citoyenDto.setId(citoyen.getId());
            citoyenDto.setNiciv(citoyen.getNiciv());
            citoyenDto.setNom(citoyen.getNom());
            citoyenDto.setPrenom(citoyen.getPrenom());
            citoyenDto.setTelephone(citoyen.getTelephone());
            citoyenDto.setDateNaissance(citoyen.getDateNaissance());
            //// citoyenDto.setProfession(citoyen.getProfession());
            citoyenDto.setGenre(citoyen.getGenre());
            citoyenDto.setPrenomMere(citoyen.getPrenomMere());
            citoyenDto.setNomMere(citoyen.getNomMere());
            //// citoyenDto.setProfessionMere(citoyen.getProfessionMere());
            citoyenDto.setCivilite(citoyen.getCivilite());
            citoyenDto.setPrenomPere(citoyen.getPrenomPere());
            citoyen.setRue(citoyen.getRue());
            citoyen.setPorte(citoyen.getPorte());
            citoyen.setAutre(citoyen.getAutre());
            ////citoyenDto.setProfessionPere(citoyen.getProfessionPere());


 
            
         // Vérifier si LieuNaissance est null avant d'accéder à son ID
            if (citoyen.getLieuNaissance() != null) {
                citoyenDto.setLieuNaissance(citoyen.getLieuNaissance().getId());
            } 
            
            if (citoyen.getAdresse() != null) {
                citoyenDto.setAdresse(citoyen.getAdresse().getId());
            } else {
                // Gérer le cas où vqf est null, par exemple en définissant un ID par défaut
                citoyenDto.setAdresse(1); // Remplacez -1 par une valeur appropriée
            }
            
            if (citoyen.getProfession() != null) {
                citoyenDto.setProfession(citoyen.getProfession().getId());
            } else {
                // Gérer le cas où vqf est null, par exemple en définissant un ID par défaut
                citoyenDto.setProfession(1); // Remplacez -1 par une valeur appropriée
            }
            
            if (citoyen.getProfessionPere() != null) {
                citoyenDto.setProfessionPere(citoyen.getProfession().getId());
            } else {
                // Gérer le cas où vqf est null, par exemple en définissant un ID par défaut
                citoyenDto.setProfessionPere(1); // Remplacez -1 par une valeur appropriée
            }
            
            if (citoyen.getProfessionMere() != null) {
                citoyenDto.setProfessionMere(citoyen.getProfession().getId());
            } else {
                // Gérer le cas où vqf est null, par exemple en définissant un ID par défaut
                citoyenDto.setProfessionMere(1); // Remplacez -1 par une valeur appropriée
            }
            

            citoyenDtos.add(citoyenDto);
        }

        return citoyenDtos;
    }


    public CitoyenDto getCitoyenById(long id) {
        Optional<Citoyen> citoyenData = citoyenRepository.findById(id);
        if (citoyenData.isPresent()) {
            Citoyen citoyen = citoyenData.get();
            CitoyenDto citoyenDto = new CitoyenDto();
            citoyenDto.setId(citoyen.getId());
            citoyenDto.setNiciv(citoyen.getNiciv());
            citoyenDto.setNom(citoyen.getNom());
            citoyenDto.setPrenom(citoyen.getPrenom());
            citoyenDto.setTelephone(citoyen.getTelephone());
            citoyenDto.setDateNaissance(citoyen.getDateNaissance());
            // citoyenDto.setProfession(citoyen.getProfession());
            citoyenDto.setGenre(citoyen.getGenre());
            citoyenDto.setPrenomMere(citoyen.getPrenomMere());
            citoyenDto.setNomMere(citoyen.getNomMere());
            // citoyenDto.setProfessionMere(citoyen.getProfessionMere());
            citoyenDto.setCivilite(citoyen.getCivilite());
            citoyenDto.setPrenomPere(citoyen.getPrenomPere());
            citoyenDto.setRue(citoyen.getRue());
            citoyenDto.setPorte(citoyen.getPorte());
            citoyenDto.setPortrait(citoyen.getPortrait());
            citoyenDto.setAutre(citoyen.getAutre());
            citoyenDto.setCle(citoyen.getCle());
            //citoyenDto.setProfessionPere(citoyen.getProfessionPere());



         // Vérifier si LieuNaissance est null avant d'accéder à son ID
            if (citoyen.getLieuNaissance() != null) {
                citoyenDto.setLieuNaissance(citoyen.getLieuNaissance().getId());
            } else {
                // Gérer le cas où vqf est null, par exemple en définissant un ID par défaut
                citoyenDto.setLieuNaissance(1); // Remplacez -1 par une valeur appropriée
            }
            
            if (citoyen.getAdresse() != null) {
                citoyenDto.setAdresse(citoyen.getAdresse().getId());
            } else {
                // Gérer le cas où vqf est null, par exemple en définissant un ID par défaut
                citoyenDto.setAdresse(1); // Remplacez -1 par une valeur appropriée
            }
            
            if (citoyen.getProfession() != null) {
                citoyenDto.setProfession(citoyen.getProfession().getId());
            } else {
                // Gérer le cas où vqf est null, par exemple en définissant un ID par défaut
                citoyenDto.setProfession(1); // Remplacez -1 par une valeur appropriée
            }
            
            if (citoyen.getProfessionPere() != null) {
                citoyenDto.setProfessionPere(citoyen.getProfessionPere().getId());
            } else {
                // Gérer le cas où vqf est null, par exemple en définissant un ID par défaut
                citoyenDto.setProfessionPere(1); // Remplacez -1 par une valeur appropriée
            }
            
            if (citoyen.getProfessionMere() != null) {
                citoyenDto.setProfessionMere(citoyen.getProfessionMere().getId());
            } else {
                // Gérer le cas où vqf est null, par exemple en définissant un ID par défaut
                citoyenDto.setProfessionMere(1); // Remplacez -1 par une valeur appropriée
            }
            

            return citoyenDto;
        } else {
            // Gérer le cas où aucune citoyen avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Citoyen non trouvée avec l'ID : " + id);
        }
    }


    public CitoyenDto createCitoyen(CitoyenDto citoyenDto) {
        Citoyen citoyen = mapToModel(citoyenDto);
        citoyen = citoyenRepository.save(citoyen);
        return mapToDto(citoyen);
    }

    public CitoyenDto updateCitoyen(long id, CitoyenDto updatedCitoyenDto) {
        Optional<Citoyen> citoyenData = citoyenRepository.findById(id);
        if (citoyenData.isPresent()) {
            Citoyen citoyen = citoyenData.get();
            // Mettre à jour les champs de l'citoyen avec les valeurs fournies dans updatedCitoyenDto
            //citoyen.setNiciv(updatedCitoyenDto.getNiciv());
            citoyen.setNom(updatedCitoyenDto.getNom());
            citoyen.setPrenom(updatedCitoyenDto.getPrenom());
            citoyen.setTelephone(updatedCitoyenDto.getTelephone());
            citoyen.setDateNaissance(updatedCitoyenDto.getDateNaissance());
            // // Vérifier si LieuNaissance est null avant d'accéder à son ID
            citoyen.setGenre(updatedCitoyenDto.getGenre());
            citoyen.setPrenomMere(updatedCitoyenDto.getPrenomMere());
            citoyen.setNomMere(updatedCitoyenDto.getNomMere());
            // // Vérifier si LieuNaissance est null avant d'accéder à son ID
            citoyen.setCivilite(updatedCitoyenDto.getCivilite());
            citoyen.setPrenomPere(updatedCitoyenDto.getPrenomPere());
            citoyen.setRue(updatedCitoyenDto.getRue());
            citoyen.setPorte(updatedCitoyenDto.getPorte());
            citoyen.setAutre(updatedCitoyenDto.getAutre());
            //citoyen.setPortrait(updatedCitoyenDto.getPortrait());
            //citoyen.setCle(updatedCitoyenDto.getCle());
            // // Vérifier si LieuNaissance est null avant d'accéder à son ID



            // Récupérer le lieu de naissance correspondant à l'ID fourni dans updatedCitoyenDto
            Optional<Vqf> lieuData = vqfRepository.findById(updatedCitoyenDto.getLieuNaissance());
            if (lieuData.isPresent()) {
                citoyen.setLieuNaissance(lieuData.get());
            } else {
                // Gérer le cas où aucun Vqf correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lieu de naissance non trouvé avec l'ID : " + updatedCitoyenDto.getLieuNaissance());
            }
         // Récupérer l adresse correspondant à l'ID fourni dans updatedCitoyenDto
            Optional<Vqf> adresseData = vqfRepository.findById(updatedCitoyenDto.getAdresse());
            if (adresseData.isPresent()) {
                citoyen.setAdresse(adresseData.get());
            } else {
                // Gérer le cas où aucun Vqf correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Adresse non trouvé avec l'ID : " + updatedCitoyenDto.getAdresse());
            }
            
         // Récupérer la profession correspondant à l'ID fourni dans updatedCitoyenDto
            Optional<Profession> profData = professionRepository.findById(updatedCitoyenDto.getProfession());
            if (profData.isPresent()) {
                citoyen.setProfession(profData.get());
            } else {
                // Gérer le cas où aucun Vqf correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profession non trouvé avec l'ID : " + updatedCitoyenDto.getProfession());
            }
            
         // Récupérer la profession du pere correspondant à l'ID fourni dans updatedCitoyenDto
            Optional<Profession> profPData = professionRepository.findById(updatedCitoyenDto.getProfessionPere());
            if (profPData.isPresent()) {
                citoyen.setProfessionPere(profPData.get());
            } else {
                // Gérer le cas où aucun Vqf correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profession non trouvé avec l'ID : " + updatedCitoyenDto.getProfessionPere());
            }
            
         // Récupérer la profession de la mere correspondant à l'ID fourni dans updatedCitoyenDto
            Optional<Profession> profMData = professionRepository.findById(updatedCitoyenDto.getProfessionMere());
            if (profMData.isPresent()) {
                citoyen.setProfessionMere(profMData.get());
            } else {
                // Gérer le cas où aucun Vqf correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profession non trouvé avec l'ID : " + updatedCitoyenDto.getProfessionMere());
            }

            // Enregistrer les modifications dans la base de données
            Citoyen updatedCitoyen = citoyenRepository.save(citoyen);

            // Convertir l'citoyen mise à jour en CitoyenDto et la retourner
            return mapToDto(updatedCitoyen);
        } else {
            // Gérer le cas où aucune citoyen avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Citoyen non trouvée avec l'ID : " + id);
        }
    }
    
    
    public CitoyenDto updatePortrait(long id, CitoyenDto updatedCitoyenDto) {
        Optional<Citoyen> citoyenData = citoyenRepository.findById(id);
        if (citoyenData.isPresent()) {
            Citoyen citoyen = citoyenData.get();
            // Mettre à jour les champs de l'citoyen avec les valeurs fournies dans updatedCitoyenDto
            //citoyen.setNiciv(updatedCitoyenDto.getNiciv());
            citoyen.setNom(updatedCitoyenDto.getNom());
            citoyen.setPrenom(updatedCitoyenDto.getPrenom());
            citoyen.setTelephone(updatedCitoyenDto.getTelephone());
            citoyen.setDateNaissance(updatedCitoyenDto.getDateNaissance());
            // // Vérifier si LieuNaissance est null avant d'accéder à son ID
            citoyen.setGenre(updatedCitoyenDto.getGenre());
            citoyen.setPrenomMere(updatedCitoyenDto.getPrenomMere());
            citoyen.setNomMere(updatedCitoyenDto.getNomMere());
            // // Vérifier si LieuNaissance est null avant d'accéder à son ID
            citoyen.setCivilite(updatedCitoyenDto.getCivilite());
            citoyen.setPrenomPere(updatedCitoyenDto.getPrenomPere());
            citoyen.setRue(updatedCitoyenDto.getRue());
            citoyen.setPorte(updatedCitoyenDto.getPorte());
            citoyen.setAutre(updatedCitoyenDto.getAutre());
            citoyen.setPortrait(updatedCitoyenDto.getPortrait());
            //citoyen.setCle(updatedCitoyenDto.getCle());
            // // Vérifier si LieuNaissance est null avant d'accéder à son ID



            // Récupérer le lieu de naissance correspondant à l'ID fourni dans updatedCitoyenDto
            Optional<Vqf> lieuData = vqfRepository.findById(updatedCitoyenDto.getLieuNaissance());
            if (lieuData.isPresent()) {
                citoyen.setLieuNaissance(lieuData.get());
            } else {
                // Gérer le cas où aucun Vqf correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lieu de naissance non trouvé avec l'ID : " + updatedCitoyenDto.getLieuNaissance());
            }
         // Récupérer l adresse correspondant à l'ID fourni dans updatedCitoyenDto
            Optional<Vqf> adresseData = vqfRepository.findById(updatedCitoyenDto.getAdresse());
            if (adresseData.isPresent()) {
                citoyen.setAdresse(adresseData.get());
            } else {
                // Gérer le cas où aucun Vqf correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Adresse non trouvé avec l'ID : " + updatedCitoyenDto.getAdresse());
            }
            
         // Récupérer la profession correspondant à l'ID fourni dans updatedCitoyenDto
            Optional<Profession> profData = professionRepository.findById(updatedCitoyenDto.getProfession());
            if (profData.isPresent()) {
                citoyen.setProfession(profData.get());
            } else {
                // Gérer le cas où aucun Vqf correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profession non trouvé avec l'ID : " + updatedCitoyenDto.getProfession());
            }
            
         // Récupérer la profession du pere correspondant à l'ID fourni dans updatedCitoyenDto
            Optional<Profession> profPData = professionRepository.findById(updatedCitoyenDto.getProfessionPere());
            if (profPData.isPresent()) {
                citoyen.setProfessionPere(profPData.get());
            } else {
                // Gérer le cas où aucun Vqf correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profession non trouvé avec l'ID : " + updatedCitoyenDto.getProfessionPere());
            }
            
         // Récupérer la profession de la mere correspondant à l'ID fourni dans updatedCitoyenDto
            Optional<Profession> profMData = professionRepository.findById(updatedCitoyenDto.getProfessionMere());
            if (profMData.isPresent()) {
                citoyen.setProfessionMere(profMData.get());
            } else {
                // Gérer le cas où aucun Vqf correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profession non trouvé avec l'ID : " + updatedCitoyenDto.getProfessionMere());
            }

            // Enregistrer les modifications dans la base de données
            Citoyen updatedCitoyen = citoyenRepository.save(citoyen);

            // Convertir l'citoyen mise à jour en CitoyenDto et la retourner
            return mapToDto(updatedCitoyen);
        } else {
            // Gérer le cas où aucune citoyen avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Citoyen non trouvée avec l'ID : " + id);
        }
    }



    public void deleteCitoyen(long id) {
        citoyenRepository.deleteById(id);
    }

    // Méthodes utilitaires de mappage entre les entités et les DTO

    public CitoyenDto mapToDto(Citoyen citoyen) {
        CitoyenDto citoyenDto = new CitoyenDto();
        citoyenDto.setId(citoyen.getId());
        citoyenDto.setNiciv(citoyen.getNiciv());
        citoyenDto.setNom(citoyen.getNom());
        citoyenDto.setPrenom(citoyen.getPrenom());
        citoyenDto.setTelephone(citoyen.getTelephone());
        citoyenDto.setDateNaissance(citoyen.getDateNaissance());
        // citoyenDto.setProfession(citoyen.getProfession());
        citoyenDto.setGenre(citoyen.getGenre());
        citoyenDto.setPrenomMere(citoyen.getPrenomMere());
        citoyenDto.setNomMere(citoyen.getNomMere());
        // citoyenDto.setProfessionMere(citoyen.getProfessionMere());
        citoyenDto.setCivilite(citoyen.getCivilite());
        citoyenDto.setPrenomPere(citoyen.getPrenomPere());
        //citoyenDto.setProfessionPere(citoyen.getProfessionPere());
        citoyenDto.setLieuNaissance(citoyen.getLieuNaissance().getId());
        citoyenDto.setAdresse(citoyen.getAdresse().getId());
        citoyenDto.setProfession(citoyen.getProfession().getId());
        citoyenDto.setProfessionPere(citoyen.getProfessionPere().getId());
        citoyenDto.setProfessionMere(citoyen.getProfessionMere().getId());
        citoyenDto.setRue(citoyen.getRue());
        citoyenDto.setPorte(citoyen.getPorte());
        citoyenDto.setPortrait(citoyen.getPortrait());
        citoyenDto.setAutre(citoyen.getAutre());
        citoyenDto.setCle(citoyen.getCle());
        return citoyenDto;
    }

    private List<CitoyenDto> mapToDtoList(List<Citoyen> citoyens) {
        return citoyens.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Citoyen mapToModel(CitoyenDto citoyenDto) {
        Citoyen citoyen = new Citoyen();
        citoyen.setNom(citoyenDto.getNom());

        citoyen.setNiciv(citoyenDto.getNiciv());
        citoyen.setNom(citoyenDto.getNom());
        citoyen.setPrenom(citoyenDto.getPrenom());
        citoyen.setTelephone(citoyenDto.getTelephone());
        citoyen.setDateNaissance(citoyenDto.getDateNaissance());
        //citoyen.setProfession(citoyenDto.getProfession());
        citoyen.setGenre(citoyenDto.getGenre());
        citoyen.setPrenomMere(citoyenDto.getPrenomMere());
        citoyen.setNomMere(citoyenDto.getNomMere());
        //citoyen.setProfessionMere(citoyenDto.getProfessionMere());
        citoyen.setCivilite(citoyenDto.getCivilite());
        citoyen.setPrenomPere(citoyenDto.getPrenomPere());
        //citoyen.setProfessionPere(citoyenDto.getProfessionPere());
        citoyen.setRue(citoyenDto.getRue());
        citoyen.setPorte(citoyenDto.getPorte());
        citoyen.setPortrait(citoyenDto.getPortrait());
        citoyen.setAutre(citoyenDto.getAutre());
        citoyen.setCle(citoyenDto.getCle());


        return citoyen;
    }
    
    public Page<Citoyen> searchCitoyens(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return citoyenRepository.searchByKeywordInAllColumns(searchTerm, pageable);
    }

}

