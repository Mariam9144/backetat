package com.fst.back_etat_civil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fst.back_etat_civil.controller.ImageController;
import com.fst.back_etat_civil.dto.CercleDto;
import com.fst.back_etat_civil.dto.CitoyenDto;
import com.fst.back_etat_civil.dto.CommuneDto;
import com.fst.back_etat_civil.dto.CondamnationDto;
import com.fst.back_etat_civil.dto.ProfessionDto;
import com.fst.back_etat_civil.dto.RegionDto;
import com.fst.back_etat_civil.dto.VqfDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class JasperService {
	@Autowired
    private CondamnationService condamnationService;
	@Autowired
    private CitoyenService citoyenService;
    @Autowired
    private VqfService vqfService;
    @Autowired
    private CommuneService communeService;
    @Autowired
    private CercleService cercleService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private ProfessionService professionService;
    @Autowired
    private ImageController imageController;
    @Autowired
	
    //Generation de recu
	public ResponseEntity<byte[]> generateRecu(Map<String, Object> parameters) throws JRException, IOException {
        JasperReport jasperReport = JasperCompileManager.compileReport(
            new ClassPathResource("reports/recepisse.jrxml").getInputStream());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recepisse.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
	
	//Generation de nationalite
		public ResponseEntity<byte[]> generateNationalite(Map<String, Object> parameters) throws JRException, IOException {
	        JasperReport jasperReport = JasperCompileManager.compileReport(
	            new ClassPathResource("reports/nationalite.jrxml").getInputStream());

	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

	        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

	        HttpHeaders headers = new HttpHeaders();
	        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
	        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=nationalite.pdf");

	        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	    }
		
		//Generation de casier
				public ResponseEntity<byte[]> generateCasier(Map<String, Object> parameters) throws JRException, IOException {
			        JasperReport jasperReport = JasperCompileManager.compileReport(
			            new ClassPathResource("reports/casier.jrxml").getInputStream());

			        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

			        HttpHeaders headers = new HttpHeaders();
			        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
			        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=casier.pdf");

			        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
			    }
	
	//Generation de carte
		public ResponseEntity<byte[]> generateCarte(Map<String, Object> parameters) throws JRException, IOException {
	        JasperReport jasperReport = JasperCompileManager.compileReport(
	            new ClassPathResource("reports/cartePvc.jrxml").getInputStream());

	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

	        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

	        HttpHeaders headers = new HttpHeaders();
	        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
	        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=carte_bio.pdf");

	        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	    }
		
		
		//Generation de fiche individuelle
				public ResponseEntity<byte[]> generateFicheIndividuelle(Map<String, Object> parameters) throws JRException, IOException {
			        JasperReport jasperReport = JasperCompileManager.compileReport(
			            new ClassPathResource("reports/fiche_individuelle.jrxml").getInputStream());

			        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

			        HttpHeaders headers = new HttpHeaders();
			        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
			        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fiche_individuelle.pdf");

			        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
			    }
				
				
				//Generation de casier 
				public byte[] generateCasier(Long id, Long numero) throws Exception {
			        // Charger le fichier JRXML
			        InputStream jasperStream = new ClassPathResource("reports/casier.jrxml").getInputStream();
			        JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);
			        
			     // Récupérer les détails du citoyen
			        CitoyenDto citoyen = citoyenService.getCitoyenById(id);
			        VqfDto adresse = vqfService.getVqfById(citoyen.getAdresse());
			        VqfDto lieu = vqfService.getVqfById(citoyen.getLieuNaissance());
			        CommuneDto communeA = communeService.getCommuneById(adresse.getCommune());
			        CommuneDto commune = communeService.getCommuneById(lieu.getCommune());
			        CercleDto cercleA = cercleService.getCercleById(communeA.getCercle());
			        CercleDto cercle = cercleService.getCercleById(commune.getCercle());
			        RegionDto regionA = regionService.getRegionById(cercleA.getRegion());
			        RegionDto region = regionService.getRegionById(cercle.getRegion());
			        ProfessionDto profession = professionService.getProfessionById(citoyen.getProfession());
			        List<CondamnationDto> condamnationDtos = condamnationService.getCondamnationsByCitoyen(citoyen.getId());
			        

			        // Créer des paramètres pour le rapport JasperReports
			        Map<String, Object> parameters = new HashMap<>();
			        parameters.put("id", citoyen.getId());
			        parameters.put("nom", citoyen.getNom());
			        parameters.put("prenom", citoyen.getPrenom());
			        parameters.put("niciv", citoyen.getNiciv());
			        parameters.put("telephone", citoyen.getTelephone());
			        parameters.put("civilite", citoyen.getCivilite());
			        parameters.put("prenomPere", citoyen.getPrenomPere());
			        parameters.put("nomMere", citoyen.getNomMere());
			        parameters.put("prenomMere", citoyen.getPrenomMere());
			        parameters.put("dateNaissance", citoyen.getDateNaissance());
			        parameters.put("profession", profession.getLibelle());
			        parameters.put("CondamnationsDataSource", condamnationDtos);
			        
			        parameters.put("region", region.getNom());
			        parameters.put("cercle", cercle.getNom());
			        parameters.put("commune", commune.getNom());
			        parameters.put("adresse", adresse.getNom());
			        
			        parameters.put("regionA", regionA.getNom());
			        parameters.put("cercleA", cercleA.getNom());
			        parameters.put("communeA", communeA.getNom());
			        parameters.put("lieuNaissance", lieu.getNom());
			        parameters.put("toDay", new Date());
			        parameters.put("numero", numero);

			        // Obtenir les données
			        List<CondamnationDto> condamnations = condamnationService.getCondamnationsByCitoyen(citoyen.getId());
			        if(condamnations.isEmpty()) { 
			        	CondamnationDto condam=new CondamnationDto();
			        	condam.setCitoyen(citoyen.getId());
			        	
			        			condamnations.add(condam);
			        }

			        // Créer une source de données Jasper
			        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(condamnations);

			        

			        // Générer le rapport
			        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			        // Exporter en PDF
			        return JasperExportManager.exportReportToPdf(jasperPrint);
			    }
			
		
}

