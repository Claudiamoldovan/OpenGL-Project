package com.example.demo.services.impl;


import com.example.demo.dataTransferObjects.ReceptionerDTO;
import com.example.demo.models.Receptioner;
import com.example.demo.models.User;
import com.example.demo.repositories.ReceptionerRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ReceptionerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceptionerServiceImpl implements ReceptionerService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReceptionerRepository receptionerRepository;

	@Autowired
	public ReceptionerServiceImpl(UserRepository userRepository, ReceptionerRepository receptionerRepository) {
		this.userRepository = userRepository;
		this.receptionerRepository = receptionerRepository;
	}
	
	public List<ReceptionerDTO> getAllReceptioners(){
		List<ReceptionerDTO> receptioners = new ArrayList<>();
		receptionerRepository.findAll().forEach(receptioner -> receptioners.add(receptioner.convertToDTO(receptioner)));
		return receptioners;
	}
	
	public ReceptionerDTO getOneReceptioner(Long id) {
		Receptioner receptioner = receptionerRepository.getOne(id);
		if (receptioner != null)
			return receptioner.convertToDTO(receptioner);
		else
			return null;
	}
	
	public void createReceptioner(ReceptionerDTO receptionerDTO) {
		Receptioner entity = new Receptioner().convertToModel(receptionerDTO);
		
		User user = userRepository.findByUsername(receptionerDTO.getUsername())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: username not found"));;
		
		entity.setUser(user);
		
		receptionerRepository.save(entity);
	}
	
	public Receptioner updateReceptioner(ReceptionerDTO receptionerDTO, Long id) {
		Receptioner entity = new Receptioner().convertToModel(receptionerDTO);
		
		return receptionerRepository.findById(id)
				.map(receptioner -> {
					receptioner.setNume(entity.getNume());
					receptioner.setEmail(entity.getEmail());
					receptioner.setDataAngajarii(entity.getDataAngajarii());
					return receptionerRepository.save(receptioner);
				})
				.orElseGet(() -> {
					entity.setId(id);
					return receptionerRepository.save(entity);
				});				
	}
	

}
