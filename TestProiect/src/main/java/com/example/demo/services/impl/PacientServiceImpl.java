package com.example.demo.services.impl;


import com.example.demo.dataTransferObjects.PacientDTO;
import com.example.demo.models.Pacient;
import com.example.demo.repositories.PacientRepository;
import com.example.demo.services.PacientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PacientServiceImpl implements PacientService {
	@Autowired
	private PacientRepository pacientRepository;

	@Autowired
	public PacientServiceImpl(PacientRepository pacientRepository) {
		this.pacientRepository = pacientRepository;
	}
	
	public List<PacientDTO> getAllPacients(){
		List<PacientDTO> pacients = new ArrayList<>();
		pacientRepository.findAll().forEach(pacient -> pacients.add(pacient.convertToDTO(pacient)));
		return pacients;
	}
	
	public PacientDTO getOnePacient(Long id) {
		Pacient pacient = pacientRepository.getOne(id);
		if (pacient != null)
			return pacient.convertToDTO(pacient);
		else
			return null;
	}
	
	public void createPacient(PacientDTO pacientDTO) {
		Pacient entity = new Pacient().convertToModel(pacientDTO);
		
		pacientRepository.save(entity);
	}
	
	public Pacient updatePacient(PacientDTO pacientDTO, Long id) {
		Pacient entity = new Pacient().convertToModel(pacientDTO);
		
		return pacientRepository.findById(id)
				.map(pacient -> {
					pacient.setNume(entity.getNume());
					pacient.setCnp(entity.getCnp());
					pacient.setDataNasterii(entity.getDataNasterii());
					pacient.setAdresa(entity.getAdresa());
					pacient.setTelefon(entity.getTelefon());
					pacient.setEmail(entity.getEmail());
					return pacientRepository.save(pacient);
				})
				.orElseGet(() -> {
					entity.setId(id);
					return pacientRepository.save(entity);
				});
	}
	
	public void deletePacient(Long id) {
		pacientRepository.deleteById(id);
	}	

}
