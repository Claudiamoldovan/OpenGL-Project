package com.example.demo.models;

import com.example.demo.dataTransferObjects.AppointmentDTO;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name="appointment")
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column
	private Timestamp start;
	@Column
	private Timestamp end;

	public Appointment() {}

	public Appointment(Timestamp start, Timestamp end) {
		this.start = start;
		this.end = end;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="medic_id")
	private Medic medic;
	
	@ManyToOne()
	@JoinColumn(name = "pacient_id")
	private Pacient pacient;
	
	public Appointment convertToModel(AppointmentDTO appointmentDTO) {
		Appointment entity = new Appointment();
		if (appointmentDTO.getId() != null)
			entity.setId(appointmentDTO.getId());
		entity.setStart(appointmentDTO.getStart());
		entity.setEnd(appointmentDTO.getEnd());
		
		return entity;
	}
	
	public AppointmentDTO convertToDTO(Appointment appointment) {
		AppointmentDTO appointmentDTO = new AppointmentDTO();
		if (appointment.getId() != null)
			appointmentDTO.setId(appointment.getId());
		appointmentDTO.setStart(appointment.getStart());
		appointmentDTO.setEnd(appointment.getEnd());
		appointmentDTO.setPacient(appointment.getPacient());
		appointmentDTO.setTitle(appointment.getPacient().getNume());
		
		return appointmentDTO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Medic getMedic() {
		return medic;
	}

	public void setMedic(Medic medic) {
		this.medic = medic;
	}

	public Pacient getPacient() {
		return pacient;
	}

	public void setPacient(Pacient pacient) {
		this.pacient = pacient;
	}

	public Timestamp getStart() {
		return start;
	}

	public void setStart(Timestamp start) {
		this.start = start;
	}

	public Timestamp getEnd() {
		return end;
	}

	public void setEnd(Timestamp end) {
		this.end = end;
	}
}
