package com.example.demo.services.impl;


import com.example.demo.dataTransferObjects.AppointmentDTO;
import com.example.demo.dataTransferObjects.ConsultDTO;
import com.example.demo.models.Appointment;
import com.example.demo.models.Medic;
import com.example.demo.models.Pacient;
import com.example.demo.models.User;
import com.example.demo.repositories.AppointmentRepository;
import com.example.demo.repositories.MedicRepository;
import com.example.demo.repositories.PacientRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	private AppointmentRepository appointmentRepository;
	

	private MedicRepository medicRepository;
	

	private PacientRepository pacientRepository;

	private UserRepository userRepository;

	public AppointmentServiceImpl(AppointmentRepository appointmentRepository, MedicRepository medicRepository,
								  PacientRepository pacientRepository, UserRepository userRepository) {
		this.appointmentRepository = appointmentRepository;
		this.medicRepository = medicRepository;
		this.pacientRepository = pacientRepository;
		this.userRepository = userRepository;
	}
	
	public List<AppointmentDTO> getAppointmentsMedic(Long id){
		List<AppointmentDTO> appointments = new ArrayList<>();
		
		appointmentRepository.findByMedicId(id).forEach(appointment -> appointments.add(appointment.convertToDTO(appointment)));
		
		return appointments;
	}
	public List<AppointmentDTO> getAllAppointments(){
		List<AppointmentDTO> appointments = new ArrayList<>();
		appointmentRepository.findAll().forEach(appointment -> appointments.add(appointment.convertToDTO(appointment)));
		return appointments;
	}
	public List<AppointmentDTO> getAppointmentsMedicUsername(String username){
		List<AppointmentDTO> appointments = new ArrayList<>();
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: username not found"));;
				
		Medic medic = medicRepository.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: username not found"));;
		
		appointmentRepository.findByMedicId(medic.getId()).forEach(appointment -> appointments.add(appointment.convertToDTO(appointment)));
		
		return appointments;
	}
	
	public void CreateAppointment(AppointmentDTO appointmentViewModel) {
		Appointment entity = new Appointment().convertToModel(appointmentViewModel);
		
		Pacient pacient = pacientRepository.findById(Long.parseLong(appointmentViewModel.getPacientId()))
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: pacient not found"));
		entity.setPacient(pacient);
		
		Medic medic = medicRepository.findById(Long.parseLong(appointmentViewModel.getMedicId()))
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: medic not found"));
		entity.setMedic(medic);
		
		appointmentRepository.save(entity);
	}
	
	public void deleteAppointment(Long id) {
		appointmentRepository.deleteById(id);
	}
}
