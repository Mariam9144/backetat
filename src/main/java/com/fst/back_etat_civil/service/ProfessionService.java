package com.fst.back_etat_civil.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.fst.back_etat_civil.dto.ProfessionDto;
import com.fst.back_etat_civil.model.Profession;
import com.fst.back_etat_civil.model.Region;
import com.fst.back_etat_civil.repository.ProfessionRepository;

@Service
public class ProfessionService {

    @Autowired
    private ProfessionRepository professionRepository;

    public List<ProfessionDto> getAllProfessions() {
        List<Profession> professions = professionRepository.findAll();
        return mapToDtoList(professions);
    }

    public ProfessionDto getProfessionById(long id) {
        Optional<Profession> professionOptional = professionRepository.findById(id);
        if (professionOptional.isPresent()) {
            Profession profession = professionOptional.get();
            return mapToDto(profession);
        } else {
            throw new NotFoundException("Profession not found with id: " + id);
        }
    }

    public ProfessionDto createProfession(ProfessionDto professionDto) {
        Profession profession = mapToModel(professionDto);
        profession = professionRepository.save(profession);
        return mapToDto(profession);
    }

    public ProfessionDto updateProfession(long id, ProfessionDto professionDto) {
        Optional<Profession> professionOptional = professionRepository.findById(id);
        if (professionOptional.isPresent()) {
            Profession profession = professionOptional.get();
            profession.setLibelle(professionDto.getLibelle());
            profession = professionRepository.save(profession);
            return mapToDto(profession);
        } else {
            throw new NotFoundException("Profession not found with id: " + id);
        }
    }

    public void deleteProfession(long id) {
        professionRepository.deleteById(id);
    }

    // Méthodes utilitaires de mappage entre les entités et les DTO

    private ProfessionDto mapToDto(Profession profession) {
        ProfessionDto professionDto = new ProfessionDto();
        professionDto.setId(profession.getId());
        professionDto.setLibelle(profession.getLibelle());
        return professionDto;
    }

    private List<ProfessionDto> mapToDtoList(List<Profession> professions) {
        return professions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Profession mapToModel(ProfessionDto professionDto) {
        Profession profession = new Profession();
        profession.setLibelle(professionDto.getLibelle());
        return profession;
    }
    
    public Page<Profession> searchProfessions(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.ASC, "libelle");
        Pageable pageable = PageRequest.of(page, size, sort);
        return professionRepository.searchByKeywordInAllColumns(searchTerm, pageable);
    }
}
