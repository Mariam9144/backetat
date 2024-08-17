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
import com.fst.back_etat_civil.dto.CommuneDto;
import com.fst.back_etat_civil.dto.VqfDto;
import com.fst.back_etat_civil.model.Cercle;
import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.model.Commune;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.repository.CommuneRepository;
import com.fst.back_etat_civil.repository.VqfRepository;

import lombok.Data;

@Data
@Service
public class VqfService {
    @Autowired
    private VqfRepository vqfRepository;
    
    @Autowired
    private CommuneRepository communeRepository;

    public List<VqfDto> getAllVqfs() {
        List<Vqf> vqfs = vqfRepository.findAll();
        List<VqfDto> vqfDtos = new ArrayList<>();

        for (Vqf vqf : vqfs) {
            VqfDto vqfDto = new VqfDto();
            vqfDto.setId(vqf.getId());
            vqfDto.setAutre(vqf.getAutre());
            vqfDto.setNom(vqf.getNom());
            vqfDto.setCode(vqf.getCode());

            // Vérifier si region est null avant d'accéder à son ID
            if (vqf.getCommune() != null) {
            	vqfDto.setCommune(vqf.getCommune().getId());
            } else {
                // Gérer le cas où region est null, par exemple en définissant un ID par défaut
                vqfDto.setCommune(1); // Remplacez -1 par une valeur appropriée
            }

            vqfDtos.add(vqfDto);
        }

        return vqfDtos;
    }
    
    public List<VqfDto> getVqfsByCom(long id) {
    	Commune commune=communeRepository.findById(id).get();
        List<Vqf> vqfs = vqfRepository.findByCommune(commune);
        List<VqfDto> vqfDtos = new ArrayList<>();

        for (Vqf vqf : vqfs) {
            VqfDto vqfDto = new VqfDto();
            vqfDto.setId(vqf.getId());
            vqfDto.setAutre(vqf.getAutre());
            vqfDto.setNom(vqf.getNom());
            vqfDto.setCode(vqf.getCode());

         // Vérifier si cercle est null avant d'accéder à son ID
            if (vqf.getCommune() != null) {
                vqfDto.setCommune(vqf.getCommune().getId());
            } else {
                // Gérer le cas où cercle est null, par exemple en définissant un ID par défaut
                vqfDto.setCommune(1); // Remplacez -1 par une valeur appropriée
            }

            vqfDtos.add(vqfDto);
        }

        return vqfDtos;
    }

    public VqfDto getVqfById(long id) {
        Optional<Vqf> vqfData = vqfRepository.findById(id);
        if (vqfData.isPresent()) {
            Vqf vqf = vqfData.get();
            VqfDto vqfDto = new VqfDto();
            vqfDto.setId(vqf.getId());
            vqfDto.setNom(vqf.getNom());
            vqfDto.setCode(vqf.getCode());
            vqfDto.setAutre(vqf.getAutre());


            // Vérifier si cercle est null avant d'accéder à son ID
            if (vqf.getCommune() != null) {
            	vqfDto.setCommune(vqf.getCommune().getId());
            } else {
                // Gérer le cas où cercle est null, par exemple en définissant un ID par défaut
            	vqfDto.setCommune(1); // Remplacez -1 par une valeur appropriée
            }

            return vqfDto;
        } else {
            // Gérer le cas où aucune commune avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vqf non trouvée avec l'ID : " + id);
        }
    }

    public VqfDto createVqf(VqfDto vqfDto) {
        Vqf vqf = mapToModel(vqfDto);
        vqf.setCommune(communeRepository.findById(vqfDto.getCommune()).get());
        vqf = vqfRepository.save(vqf);
        return mapToDto(vqf);
    }
    
    public VqfDto createVille(VqfDto vqfDto) {
        Vqf vqf = mapToModel(vqfDto);
        vqf.setCommune(communeRepository.findById(vqfDto.getCommune()).get());
        vqf.setCode(GeneCode(communeRepository.findById(vqfDto.getCommune()).get()));
        vqf = vqfRepository.save(vqf);
        return mapToDto(vqf);
    }

    public VqfDto updateVqf(long id, VqfDto vqfDto) {
        Optional<Vqf> vqfData = vqfRepository.findById(id);
        if (vqfData.isPresent()) {
            Vqf vqf = vqfData.get();
            // Mettre à jour les champs de l'commune avec les valeurs fournies dans updatedCommuneDto
            vqf.setNom(vqfDto.getNom());
            vqf.setCode(vqfDto.getCode());
            vqf.setAutre(vqfDto.getAutre());

            // Récupérer la Commune correspondant à l'ID fourni dans vqfDto
            Optional<Commune> communeData = communeRepository.findById(vqfDto.getCommune());
            if (communeData.isPresent()) {
            	vqf.setCommune(communeData.get());
            } else {
                // Gérer le cas où aucun Cercle correspondant n'est trouvé
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cercle non trouvé avec l'ID : " + vqfDto.getCommune());
            }

            // Enregistrer les modifications dans la base de données
            Vqf updatedVqf = vqfRepository.save(vqf);

            // Convertir l'commune mise à jour en CommuneDto et la retourner
            return mapToDto(updatedVqf);
        } else {
            // Gérer le cas où aucune commune avec cet ID n'est trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vqf non trouvée avec l'ID : " + id);
        }
    }

    public void deleteVqf(long id) {
        vqfRepository.deleteById(id);
    }

    // Méthodes utilitaires de mappage entre les entités et les DTO

    private VqfDto mapToDto(Vqf vqf) {
        VqfDto vqfDto = new VqfDto();
        vqfDto.setId(vqf.getId());
        vqfDto.setNom(vqf.getNom());
        vqfDto.setCode(vqf.getCode());
        vqfDto.setAutre(vqf.getAutre());
        return vqfDto;
    }

    private List<VqfDto> mapToDtoList(List<Vqf> vqfs) {
        return vqfs.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Vqf mapToModel(VqfDto vqfDto) {
        Vqf vqf = new Vqf();
        vqf.setNom(vqfDto.getNom());
        vqf.setCode(vqfDto.getCode());
        vqf.setAutre(vqfDto.getAutre());
        return vqf;
    }
    
    public Page<Vqf> searchVqfs(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.ASC, "code");
        Pageable pageable = PageRequest.of(page, size, sort);
        return vqfRepository.searchByKeywordInAllColumns(searchTerm, pageable);
    }
    
    public Page<Vqf> searchVilles(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.ASC, "code");
        Pageable pageable = PageRequest.of(page, size, sort);
        return vqfRepository.searchByKeywordInAllColumnsVille(searchTerm, pageable);
    }
    
    
    public String GeneCode(Commune commune) {
    	List<Vqf>  vqfs=vqfRepository.findByCommune(commune);
    	long seq=1;
    	for (Vqf v : vqfs) seq++;
			
		return commune.getCode()+String.valueOf(String.format("%06d",seq));
    }

}
