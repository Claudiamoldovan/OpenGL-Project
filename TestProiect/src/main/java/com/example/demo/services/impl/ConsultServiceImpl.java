package com.example.demo.services.impl;


import com.example.demo.dataTransferObjects.ConsultDTO;
import com.example.demo.models.Consult;
import com.example.demo.models.Medic;
import com.example.demo.models.Pacient;
import com.example.demo.models.User;
import com.example.demo.repositories.ConsultRepository;
import com.example.demo.repositories.MedicRepository;
import com.example.demo.repositories.PacientRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultServiceImpl implements ConsultService {
	@Autowired
	private ConsultRepository consultRepository;
	
	@Autowired
	private MedicRepository medicRepository;
	
	@Autowired
	private PacientRepository pacientRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	public ConsultServiceImpl(ConsultRepository consultRepository, MedicRepository medicRepository,
							  PacientRepository pacientRepository, UserRepository userRepository) {
		this.consultRepository = consultRepository;
		this.medicRepository = medicRepository;
		this.pacientRepository = pacientRepository;
		this.userRepository = userRepository;
	}
	
	public List<ConsultDTO> getAllConsults(){
		List<ConsultDTO> consults = new ArrayList<>();
		consultRepository.findAll().forEach(consult -> consults.add(consult.convertToDTO(consult)));
		return consults;
	}
	
	public List<ConsultDTO> getMedicConsults(String username){
		List<ConsultDTO> consults = new ArrayList<>();
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: username not found"));;
				
		Medic medic = medicRepository.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: username not found"));;
				
		consultRepository.findByMedicId(medic.getId()).forEach(consult -> consults.add(consult.convertToDTO(consult)));;
		
		return consults;
	}

	public void createConsult(ConsultDTO consultViewModel) {
		Consult entity = new Consult().convertToModel(consultViewModel);
		
		User user = userRepository.findByUsername(consultViewModel.getMedicUsername())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: username not found"));;
		
		Medic medic = medicRepository.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: username not found"));;
				
		entity.setMedic(medic);
		
		Pacient pacient = pacientRepository.findById(Long.parseLong(consultViewModel.getPacientId()))
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: pacient not found"));
		
		entity.setPacient(pacient);
		
		consultRepository.save(entity);
	}

}
