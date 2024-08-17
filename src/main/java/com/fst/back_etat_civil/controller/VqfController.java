package com.fst.back_etat_civil.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fst.back_etat_civil.dto.VqfDto;
import com.fst.back_etat_civil.model.Cercle;
import com.fst.back_etat_civil.model.Commune;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.repository.CommuneRepository;
import com.fst.back_etat_civil.repository.VqfRepository;
import com.fst.back_etat_civil.service.VqfService;
import com.fst.back_etat_civil.util.DataSanitizer;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/vqf")
public class VqfController {
    @Autowired
    private VqfService vqfService;
    
    @Autowired
    private VqfRepository vqfRepository;
    @Autowired
    CommuneRepository communeRepository;

    @GetMapping
    public ResponseEntity<List<VqfDto>> getAllVqfs() {
        List<VqfDto> vqfs = vqfService.getAllVqfs();
        return new ResponseEntity<>(vqfs, HttpStatus.OK);
    }
    
    @GetMapping("/getVqfsByCommune/{id}")
    public ResponseEntity<List<VqfDto>> getVqfsByCommune(@PathVariable long id) {
        List<VqfDto> vqfs = vqfService.getVqfsByCom(id);
        return new ResponseEntity<>(vqfs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VqfDto> getVqfById(@PathVariable Long id) {
        VqfDto vqf = vqfService.getVqfById(id);
        return new ResponseEntity<>(vqf, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VqfDto> createVqf(@RequestBody VqfDto vqfDto) {
    	if (vqfRepository.existsByCode(vqfDto.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce code de vqf existe déjà");
        }
    	else {
        VqfDto createdVqf = vqfService.createVqf(vqfDto);
        return new ResponseEntity<>(createdVqf, HttpStatus.CREATED);
        }
    }
    
    @PostMapping("/ville")
    public ResponseEntity<VqfDto> createVille(@RequestBody VqfDto vqfDto) {
    	if (vqfRepository.existsByNom(vqfDto.getNom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce nom de Ville existe déjà");
        }
    	else {
        VqfDto createdVqf = vqfService.createVille(vqfDto);
        return new ResponseEntity<>(createdVqf, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VqfDto> updateVqf(@PathVariable Long id, @RequestBody VqfDto vqfDto) {
    	if (vqfRepository.existsByCode(vqfDto.getCode()) 
        		&& !vqfRepository.findById(id).get().getCode().equals(vqfDto.getCode())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce code de vqf existe déjà");
            }
        	else {
        VqfDto updatedVqf = vqfService.updateVqf(id, vqfDto);
        return new ResponseEntity<>(updatedVqf, HttpStatus.OK);
            }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVqf(@PathVariable Long id) {
        vqfService.deleteVqf(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Vqf>> search(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Vqf> vqfs = vqfService.searchVqfs(keyword, page, size);
        return ResponseEntity.ok(vqfs);
    }
    
    @GetMapping("/searchVille")
    public ResponseEntity<Page<Vqf>> searchV(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Vqf> vqfs = vqfService.searchVilles(keyword, page, size);
        return ResponseEntity.ok(vqfs);
    }
    
    
    @PostMapping("/import")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file) {
        try {
            List<Vqf> vqfs = parseFile(file);
            for (Vqf vqf : vqfs) {
                // Vérifiez l'existence en ignorant la casse et en normalisant le nom
                if (!vqfRepository.existsByCode(vqf.getCode()) &&
                    !vqfRepository.existsByNomIgnoreCase(normalize(vqf.getNom()))) {
                    vqfRepository.save(vqf);
                }
            }
            return ResponseEntity.ok("Importation réussie");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'importation du fichier");
        }
    }

    private List<Vqf> parseFile(MultipartFile file) throws IOException {
        List<Vqf> vqfs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_16LE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 2) continue; // Ignore invalid lines
                String nom = DataSanitizer.removeNullCharacters(fields[0].trim()); // Supprimez les espaces autour du code
                String code = DataSanitizer.removeNullCharacters(fields[1].trim()); // Supprimez les espaces autour du nom
                //String cercle = DataSanitizer.removeNullCharacters(fields[2].trim());
                Vqf vqf = new Vqf();
                vqf.setCode(code);
                vqf.setNom(nom);
                
				Commune commune = communeRepository.findByCode(code.substring(0, 6));
                if (commune!=null) {
                    vqf.setCommune(commune);
                    vqfs.add(vqf);
                } else {
                    System.out.println("Commune non trouvée pour l'ID: ");
                }
            }
        }
        return vqfs;
    }

    private String normalize(String input) {
        // Normalisez en NFC pour gérer les accents et autres caractères Unicode
        return Normalizer.normalize(input, Normalizer.Form.NFC);
    }

}
