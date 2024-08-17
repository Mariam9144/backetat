package com.fst.back_etat_civil.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fst.back_etat_civil.dto.CitoyenDto;
import com.fst.back_etat_civil.dto.RegionDto;
import com.fst.back_etat_civil.model.Cercle;
import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.model.Profession;
import com.fst.back_etat_civil.model.Region;
import com.fst.back_etat_civil.model.Vqf;
import com.fst.back_etat_civil.repository.CitoyenRepository;
import com.fst.back_etat_civil.repository.ProfessionRepository;
import com.fst.back_etat_civil.repository.VqfRepository;
import com.fst.back_etat_civil.service.CitoyenService;
import com.fst.back_etat_civil.service.VqfService;
import com.fst.back_etat_civil.services.NumeroUniqueService;
import com.fst.back_etat_civil.util.DataSanitizer;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/citoyen")
public class CitoyenController {
    @Autowired
    private CitoyenService citoyenService;
    
    @Autowired
    NumeroUniqueService numeroUniqueService;
   @Autowired
   VqfRepository vqfRepository;
     @Autowired
    private VqfService vqfService;
     @Autowired
     private ProfessionRepository professionRepository;
    @Autowired
    private CitoyenRepository citoyenRepository;

    @GetMapping("")
    public ResponseEntity<List<CitoyenDto>> getAllCitoyens() {
        List<CitoyenDto> citoyens = citoyenService.getAllCitoyens();
        return new ResponseEntity<>(citoyens, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitoyenDto> getCitoyenById(@PathVariable long id) {
        CitoyenDto citoyen = citoyenService.getCitoyenById(id);
        return new ResponseEntity<>(citoyen, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CitoyenDto> createCitoyen(@RequestBody CitoyenDto citoyen) {
    	
    	if (citoyen.getTelephone()!=null && citoyenRepository.existsByTelephone(citoyen.getTelephone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce numero de telephone existe déjà");
        }else {
        try {
           Optional<Vqf> lieu=vqfRepository.findById(citoyen.getLieuNaissance());
           Optional<Vqf> adresse=vqfRepository.findById(citoyen.getAdresse());
           Optional<Profession> prof=professionRepository.findById(citoyen.getProfession());
           Optional<Profession> profP=professionRepository.findById(citoyen.getProfessionPere());
           Optional<Profession> profM=professionRepository.findById(citoyen.getProfessionMere());
            Citoyen citoyen1= new Citoyen();
            //MAPPING
            citoyen1.setNiciv(citoyen.getNiciv());
            citoyen1.setNom(citoyen.getNom());
            citoyen1.setPrenom(citoyen.getPrenom());
            citoyen1.setDateNaissance(citoyen.getDateNaissance());
            citoyen1.setTelephone(citoyen.getTelephone());
            // citoyen1.setProfession(citoyen.getProfession());
            citoyen1.setGenre(citoyen.getGenre());
            citoyen1.setNomMere(citoyen.getNomMere());
            citoyen1.setPrenomMere(citoyen.getPrenomMere());
            citoyen1.setRue(citoyen.getRue());
            citoyen1.setAutre(citoyen.getAutre());
            citoyen1.setPorte(citoyen.getPorte());
            
            citoyen1.setPrenomPere(citoyen.getPrenomPere());
            
            citoyen1.setCivilite(citoyen.getCivilite());
            
            //generation de cle avec genre , annee Naissance, lieu Naissance
            String cle=String.valueOf((citoyen.getGenre().equals("Homme"))?1:2)+
        			String.valueOf(String.format("%02d", citoyen.getDateNaissance().getYear() % 100))+
        			lieu.get().getCode();
            citoyen1.setCle(cle);
             
            //appel de methode pour generer la Niciv pr le citoyen
            citoyen1.setNiciv(NumeroNiciv(cle));
            
          //appel de methode pour generer la Niciv pr le citoyenDto
            citoyen.setNiciv(NumeroNiciv(cle));
            
             if(!lieu.isPresent())
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND,"VQF NOT FOUND");
             citoyen1.setLieuNaissance(lieu.get());if(!adresse.isPresent())
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND,"VQF NOT FOUND");
             citoyen1.setAdresse(adresse.get());
             
            if(!adresse.isPresent())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"VQF NOT FOUND");
            citoyen1.setAdresse(adresse.get());
            //END MAPPING
            if(!prof.isPresent())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"VQF NOT FOUND");
            citoyen1.setProfession(prof.get());
            //END MAPPING
            if(!profP.isPresent())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"PROFESSION NOT FOUND");
            citoyen1.setProfessionPere(profP.get());
            //END MAPPING
            if(!profM.isPresent())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"PROFESSION NOT FOUND");
            citoyen1.setProfessionMere(profM.get());
            //END MAPPING
            if(!profM.isPresent())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"PROFESSION NOT FOUND");
            
            if (citoyenRepository.existsByNiciv(NumeroNiciv(cle))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce numero niciv existe déjà");
            }else {

            Citoyen _citoyen = citoyenRepository
                    .save(citoyen1);
            citoyen.setId(_citoyen.getId());

            return new ResponseEntity<>(citoyen, HttpStatus.CREATED);}
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitoyenDto> updateCitoyen(@PathVariable long id, @RequestBody CitoyenDto citoyenDto) {
    	if (citoyenDto.getTelephone()!=null && !citoyenDto.getTelephone().equals(citoyenRepository.findById(id).get().getTelephone())
    			&& citoyenRepository.existsByTelephone(citoyenDto.getTelephone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce numero de telephone existe déjà");
        }else {
        CitoyenDto updatedCitoyen = citoyenService.updateCitoyen(id, citoyenDto);
        return new ResponseEntity<>(updatedCitoyen, HttpStatus.OK);}
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCitoyen(@PathVariable long id) {
        citoyenService.deleteCitoyen(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    @GetMapping("/search")
    public ResponseEntity<Page<Citoyen>> searchActualites(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<Citoyen> citoyens = citoyenService.searchCitoyens(keyword, page, size);
        return ResponseEntity.ok(citoyens);
    }
    
    public String NumeroNiciv(String cle) {
    	int seq = (int) Seq(cle); 
        String numero=numeroUniqueService.generateNumeroUnique(cle,seq);
        return numero;
    }
    
   
    public long Seq(String cle) {
    	List<Citoyen>  citoyens=citoyenRepository.findByCle(cle);
    	long seq=0;
    	for (Citoyen citoyen : citoyens) seq++;
			
		return seq;
    }
    
    @PostMapping("/import")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file) {
        try {
            List<CitoyenDto> citoyens = parseFile(file);
            List<CitoyenDto> importedCitoyens = new ArrayList<>();

            for (CitoyenDto citoyen : citoyens) {
                if (citoyen.getTelephone() != null && citoyenRepository.existsByTelephone(citoyen.getTelephone())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce numéro de téléphone existe déjà");
                }

                try {
                    Optional<Vqf> lieu = vqfRepository.findById(citoyen.getLieuNaissance());
                    Optional<Vqf> adresse = vqfRepository.findById(citoyen.getAdresse());
                    Optional<Profession> prof = professionRepository.findById(citoyen.getProfession());
                    Optional<Profession> profP = professionRepository.findById(citoyen.getProfessionPere());
                    Optional<Profession> profM = professionRepository.findById(citoyen.getProfessionMere());

                    if (!lieu.isPresent() || !adresse.isPresent() || !prof.isPresent() || !profP.isPresent() || !profM.isPresent()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VQF ou profession non trouvé");
                    }

                    Citoyen citoyen1 = new Citoyen();
                    // MAPPING
                    citoyen1.setNiciv(citoyen.getNiciv());
                    citoyen1.setNom(citoyen.getNom());
                    citoyen1.setPrenom(citoyen.getPrenom());
                    citoyen1.setDateNaissance(citoyen.getDateNaissance());
                    citoyen1.setTelephone(citoyen.getTelephone());
                    citoyen1.setGenre(citoyen.getGenre());
                    citoyen1.setNomMere(citoyen.getNomMere());
                    citoyen1.setPrenomMere(citoyen.getPrenomMere());
                    citoyen1.setRue(citoyen.getRue());
                    citoyen1.setAutre(citoyen.getAutre());
                    citoyen1.setPorte(citoyen.getPorte());
                    citoyen1.setPrenomPere(citoyen.getPrenomPere());
                    citoyen1.setCivilite(citoyen.getCivilite());

                    // Génération de la clé avec genre, année de naissance, lieu de naissance
                    String cle = String.valueOf((citoyen.getGenre().equals("Homme")) ? 1 : 2) +
                            String.valueOf(String.format("%02d", citoyen.getDateNaissance().getYear() % 100)) +
                            lieu.get().getCode();
                    citoyen1.setCle(cle);

                    // Appel de méthode pour générer la Niciv pour le citoyen
                    citoyen1.setNiciv(NumeroNiciv(cle));

                    // Assignation des objets trouvés
                    citoyen1.setLieuNaissance(lieu.get());
                    citoyen1.setAdresse(adresse.get());
                    citoyen1.setProfession(prof.get());
                    citoyen1.setProfessionPere(profP.get());
                    citoyen1.setProfessionMere(profM.get());

                    if (citoyenRepository.existsByNiciv(NumeroNiciv(cle))) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce numéro NICIV existe déjà");
                    }

                    // Enregistrement du citoyen dans la base de données
                    Citoyen savedCitoyen = citoyenRepository.save(citoyen1);
                    citoyen.setId(savedCitoyen.getId());
                    importedCitoyens.add(citoyen);

                } catch (ResponseStatusException e) {
                    // Gestion des exceptions pour les erreurs spécifiques HTTP
                    return new ResponseEntity<>(e.getReason(), e.getStatus());
                } catch (Exception e) {
                    // Gestion générale des exceptions
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'importation du fichier");
                }
            }

            // Retourne une réponse OK avec la liste des citoyens importés
            return ResponseEntity.ok(importedCitoyens);
            
        } catch (Exception e) {
            // Gestion générale des exceptions lors du traitement initial du fichier
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'importation du fichier");
        }
    }

    private List<CitoyenDto> parseFile(MultipartFile file) throws IOException, ParseException {
    	List<Long> list = Arrays.asList(2L, 6L, 3L, 4L, 5L);
    	long number=60009842;
        List<CitoyenDto> citoyens = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_16LE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 7) continue; // Ignore invalid lines
                
                CitoyenDto citoyen = new CitoyenDto();
                
                // Assuming your fields order matches the expected order
                citoyen.setPrenom(DataSanitizer.removeNullCharacters(fields[0].trim()));
                citoyen.setNom(DataSanitizer.removeNullCharacters(fields[1].trim()));
                citoyen.setGenre(fields[2].trim().equals("F") ? "Femme" : "Homme");
              
                citoyen.setDateNaissance(new Date(new Date().getTime() - (long) list.get(new Random().nextInt(list.size()))* 2 * 365 * 24 * 60 * 60 * 1000)); // Assuming date format is yyyy-MM-dd
                citoyen.setPrenomPere(DataSanitizer.removeNullCharacters(fields[4].trim()));
                citoyen.setPrenomMere(DataSanitizer.removeNullCharacters(fields[5].trim()));
                citoyen.setNomMere(DataSanitizer.removeNullCharacters(fields[6].trim()));
                citoyen.setProfession((long) list.get(new Random().nextInt(list.size())));
                citoyen.setProfessionPere((long) list.get(new Random().nextInt(list.size())));
                citoyen.setProfessionMere((long) list.get(new Random().nextInt(list.size())));
                citoyen.setLieuNaissance((long) list.get(new Random().nextInt(list.size())));
                citoyen.setAdresse((long) list.get(new Random().nextInt(list.size())));
                citoyen.setRue(String.valueOf(new Random().nextInt(20002)));
                citoyen.setPorte(String.valueOf(new Random().nextInt(10001)));
                citoyen.setTelephone(String.valueOf(number));
                citoyen.setCivilite("Celibataire");
                
                
                
                // Set other fields as needed
                
                citoyens.add(citoyen);
                number=number+2;
            }
        }
        return citoyens;
    }


    private String normalize(String input) {
        // Normalisez en NFC pour gérer les accents et autres caractères Unicode
        return Normalizer.normalize(input, Normalizer.Form.NFC);
    }
    
}
