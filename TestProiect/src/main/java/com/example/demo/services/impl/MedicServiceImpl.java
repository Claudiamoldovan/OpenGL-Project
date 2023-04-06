package com.example.demo.services.impl;


import com.example.demo.dataTransferObjects.MedicDTO;
import com.example.demo.models.Medic;
import com.example.demo.models.User;
import com.example.demo.repositories.MedicRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.MedicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicServiceImpl implements MedicService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MedicRepository medicRepository;

	@Autowired
	public MedicServiceImpl(UserRepository userRepository, MedicRepository medicRepository) {
		super();
		this.userRepository = userRepository;
		this.medicRepository = medicRepository;
	}
	
	public List<MedicDTO> getAllMedics(){
		List<MedicDTO> medics = new ArrayList<>();
		medicRepository.findAll().forEach(medic -> medics.add(medic.convertToDTO(medic)));
		return medics;
	}
	
	public MedicDTO getOneMedic(Long id) {
		Medic medic = medicRepository.getOne(id);
		if (medic != null)
			return medic.convertToDTO(medic);
		else
			return null;
	}
	
	public void createMedic(MedicDTO medicViewModel) {
		Medic entity = new Medic().convertToModel(medicViewModel);
		
		User user = userRepository.findByUsername(medicViewModel.getUsername())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: username not found"));;
		
		entity.setUser(user);
		
		medicRepository.save(entity); 
	}
	
	public Medic updateMedic(MedicDTO medicViewModel, Long id) {
		Medic entity = new Medic().convertToModel(medicViewModel);
		
		return medicRepository.findById(id)
				.map(medic -> {
					medic.setNume(entity.getNume());
					medic.setCodParafa(entity.getCodParafa());
					medic.setSpecialitate(entity.getSpecialitate());
					medic.setEmail(entity.getEmail());
					medic.setDataAngajarii(entity.getDataAngajarii());
					return medicRepository.save(medic);
				})
				.orElseGet(() -> {
					entity.setId(id);
					return medicRepository.save(entity);
				});
	}

}
