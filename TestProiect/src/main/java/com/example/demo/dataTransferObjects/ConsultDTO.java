package com.example.demo.dataTransferObjects;

import com.example.demo.models.Diagnostic;
import com.example.demo.models.Pacient;

import java.sql.Date;

public class ConsultDTO {
	private Long id;
	private Date dataConsult;
	private String anamneza;
	private Diagnostic diagnostic;
	private String medicUsername;
	private String pacientId;
	private Pacient pacient;
	
	public ConsultDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataConsult() {
		return dataConsult;
	}

	public void setDataConsult(Date dataConsult) {
		this.dataConsult = dataConsult;
	}

	public String getAnamneza() {
		return anamneza;
	}

	public void setAnamneza(String anamneza) {
		this.anamneza = anamneza;
	}

	public Diagnostic getDiagnostic() {
		return diagnostic;
	}

	public void setDiagnostic(Diagnostic diagnostic) {
		this.diagnostic = diagnostic;
	}

	public String getMedicUsername() {
		return medicUsername;
	}

	public void setMedicUsername(String medicUsername) {
		this.medicUsername = medicUsername;
	}

	public String getPacientId() {
		return pacientId;
	}

	public void setPacientId(String pacientId) {
		this.pacientId = pacientId;
	}

	public Pacient getPacient() {
		return pacient;
	}

	public void setPacient(Pacient pacient) {
		this.pacient = pacient;
	}
	
	
}
