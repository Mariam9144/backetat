package com.fst.back_etat_civil.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fst.back_etat_civil.dto.CitoyenDto;
import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.repository.CitoyenRepository;
import com.fst.back_etat_civil.service.CitoyenService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/citoyen")
public class ImageController {
	@Autowired
    private CitoyenService citoyenService;
	@Autowired
    private CitoyenRepository citoyenRepository;
	
	
	//String userHome = System.getProperty("user.home"); // Obtient le répertoire utilisateur
    //String desktopPath = userHome + File.separator + "Desktop"; // Chemin vers le bureau

   
	
	String repertoireDoc ="/portraits"; // Remplacez par le chemin souhaité pour le répertoire "images"
    
    @RequestMapping(value = "/uploadPortrait/{id}", method = RequestMethod.PUT, headers = "accept=Application/json")
    public CitoyenDto ajouter(@RequestParam("file") MultipartFile file, @PathVariable String id
                           ) throws JsonProcessingException {

    	Citoyen citoyen = citoyenRepository.findByNiciv(id);
        
        File repertoire = new File(repertoireDoc);
        if (!repertoire.exists()) {
            boolean repertoireCree = repertoire.mkdirs();
            if (!repertoireCree) {
                // Gestion de l'erreur si le répertoire ne peut pas être créé
                throw new RuntimeException("Impossible de créer le répertoire 'portraits'");
            }
        }
        String nomFichier = "portrait_"+id;
        String nouveauNom = FilenameUtils.getBaseName(nomFichier) + ".jpeg";
        File fichierDuServeur = new File(repertoire, nouveauNom);
        try {
        	if (fichierDuServeur.exists()) {
                // Si le fichier existe, le supprimer pour le remplacer par le nouveau
                FileUtils.forceDelete(fichierDuServeur);
            }
            FileUtils.writeByteArrayToFile(fichierDuServeur, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        CitoyenDto citoyenDto= citoyenService.mapToDto(citoyen);
        citoyenDto.setPortrait(nouveauNom);
        return citoyenService.updatePortrait(citoyenDto.getId(), citoyenDto);
    }

    private final Path imageStoragePath = Paths.get(repertoireDoc);

    @GetMapping("affichePortrait/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = imageStoragePath.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Définissez le type MIME approprié
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    
}

