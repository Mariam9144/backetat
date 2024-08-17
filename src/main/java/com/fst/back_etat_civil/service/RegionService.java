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

import com.fst.back_etat_civil.dto.RegionDto;
import com.fst.back_etat_civil.model.Region;
import com.fst.back_etat_civil.repository.RegionRepository;

import lombok.Data;

@Data
@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<RegionDto> getAllRegions() {
        List<Region> regions = regionRepository.findAll();
        return mapToDtoList(regions);
    }

    public RegionDto getRegionById(long id) {
        Optional<Region> regionOptional = regionRepository.findById(id);
        if (regionOptional.isPresent()) {
            Region region = regionOptional.get();
            return mapToDto(region);
        } else {
            throw new NotFoundException("Region not found with id: " + id);
        }
    }

    public RegionDto createRegion(RegionDto regionDto) {
        Region region = mapToModel(regionDto);
        region = regionRepository.save(region);
        return mapToDto(region);
    }

    public RegionDto updateRegion(long id, RegionDto regionDto) {
        Optional<Region> regionOptional = regionRepository.findById(id);
        if (regionOptional.isPresent()) {
            Region region = regionOptional.get();
            region.setNom(regionDto.getNom());
            region.setCode(regionDto.getCode());
            region.setAutre(regionDto.getAutre());
            region = regionRepository.save(region);
            return mapToDto(region);
        } else {
            throw new NotFoundException("Region not found with id: " + id);
        }
    }

    public void deleteRegion(long id) {
        regionRepository.deleteById(id);

    }

    // Méthodes utilitaires de mappage entre les entités et les DTO

    private RegionDto mapToDto(Region region) {
        RegionDto regionDto = new RegionDto();
        regionDto.setId(region.getId());
        regionDto.setCode(region.getCode());
        regionDto.setNom(region.getNom());
        regionDto.setAutre(region.getAutre());
        return regionDto;
    }

    private List<RegionDto> mapToDtoList(List<Region> regions) {
        return regions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Region mapToModel(RegionDto regionDto) {
        Region region = new Region();
        region.setCode(regionDto.getCode());
        region.setNom(regionDto.getNom());
        region.setAutre(regionDto.getAutre());
        return region;
    }
    
    public Page<Region> searchRegions(String searchTerm, int page, int size) {
    	Sort sort = Sort.by(Sort.Direction.ASC, "code");
        Pageable pageable = PageRequest.of(page, size, sort);
        return regionRepository.searchByKeywordInAllColumns(searchTerm, pageable);
    }
}
