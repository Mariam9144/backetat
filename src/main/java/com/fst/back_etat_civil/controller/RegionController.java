package com.fst.back_etat_civil.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import com.fst.back_etat_civil.dto.RegionDto;
import com.fst.back_etat_civil.model.Region;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.repository.RegionRepository;
import com.fst.back_etat_civil.service.RegionService;
import com.fst.back_etat_civil.util.DataSanitizer;
import com.lowagie.text.Cell;

import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/region")
public class RegionController {
    @Autowired
    private RegionService regionService;
    @Autowired
    private RegionRepository regionRepository;


    

	@GetMapping
    public ResponseEntity<List<RegionDto>> getAllRegions() {
        List<RegionDto> regions = regionService.getAllRegions();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDto> getRegionById(@PathVariable Long id) {
        RegionDto region = regionService.getRegionById(id);
        return new ResponseEntity<>(region, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RegionDto> createRegion(@RequestBody RegionDto regionDto) {
    	if (regionRepository.existsByCode(regionDto.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce code de region existe déjà");
        }
    	if (regionRepository.existsByNomIgnoreCase(regionDto.getNom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce nom de region existe déjà");
        }else {
        RegionDto createdRegion = regionService.createRegion(regionDto);
        return new ResponseEntity<>(createdRegion, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDto> updateRegion(@PathVariable Long id, @RequestBody RegionDto regionDto) {
    	if (regionRepository.existsByCode(regionDto.getCode()) 
    		&& !regionRepository.findById(id).get().getCode().equals(regionDto.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce code de region existe déjà");
        }
    	if (regionRepository.existsByNomIgnoreCase(regionDto.getNom())
    			&& !regionRepository.findById(id).get().getNom().equals(regionDto.getNom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce nom de region existe déjà");
        }else {
        RegionDto updatedRegion = regionService.updateRegion(id, regionDto);
        return new ResponseEntity<>(updatedRegion, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Region>> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Region> regions = regionService.searchRegions(keyword, page, size);
        return ResponseEntity.ok(regions);
    }
    
    
    @PostMapping("/import")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file) {
        try {
            List<RegionDto> regions = parseFile(file);
            for (RegionDto region : regions) {
                // Vérifiez l'existence en ignorant la casse et en normalisant le nom
                if (!regionRepository.existsByCode(region.getCode()) &&
                    !regionRepository.existsByNomIgnoreCase(normalize(region.getNom()))) {
                    regionService.createRegion(region);
                }
            }
            return ResponseEntity.ok("Importation réussie");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'importation du fichier");
        }
    }

    private List<RegionDto> parseFile(MultipartFile file) throws IOException {
        List<RegionDto> regions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_16LE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 2) continue; // Ignore invalid lines
                String code = DataSanitizer.removeNullCharacters(fields[0].trim()); // Supprimez les espaces autour du code
                String nom = DataSanitizer.removeNullCharacters(fields[1].trim()); // Supprimez les espaces autour du nom
                RegionDto region = new RegionDto();
                region.setCode(code);
                region.setNom(nom);
                regions.add(region);
            }
        }
        return regions;
    }

    private String normalize(String input) {
        // Normalisez en NFC pour gérer les accents et autres caractères Unicode
        return Normalizer.normalize(input, Normalizer.Form.NFC);
    }
    
    @PostMapping("/importer")
    public ResponseEntity<?> importExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            List<RegionDto> regions = parseExcelFile(file);
            for (RegionDto region : regions) {
                // Vérifiez l'existence en ignorant la casse et en normalisant le nom
                if (!regionRepository.existsByCode(region.getCode()) &&
                    !regionRepository.existsByNomIgnoreCase(normalize(region.getNom()))) {
                    regionService.createRegion(region);
                }
            }
            return ResponseEntity.ok("Importation réussie");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'importation du fichier");
        }
    }

    private List<RegionDto> parseExcelFile(MultipartFile file) throws IOException {
        List<RegionDto> regions = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Ignore the header row
                }
                String code = getCellValueAsString(row.getCell(0));
                String nom = getCellValueAsString(row.getCell(1));
                if (code != null && !code.isEmpty() && nom != null && !nom.isEmpty()) {
                    RegionDto region = new RegionDto();
                    region.setCode(code);
                    region.setNom(nom);
                    regions.add(region);
                }
            }
        }
        return regions;
    }

    private String getCellValueAsString(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }

    

}
