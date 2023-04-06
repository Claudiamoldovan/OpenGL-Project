package com.example.demo.dataTransferObjects;

import java.sql.Date;

public class MedicDTO {
	private Long id;
	private String nume;
	private String codParafa;
	private String specialitate;
	private String email;
	private String dataAngajarii;
	private String username;
	
	public MedicDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getCodParafa() {
		return codParafa;
	}

	public void setCodParafa(String codParafa) {
		this.codParafa = codParafa;
	}

	public String getSpecialitate() {
		return specialitate;
	}

	public void setSpecialitate(String specialitate) {
		this.specialitate = specialitate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDataAngajarii() {
		return dataAngajarii;
	}

	public void setDataAngajarii(String date) {
		this.dataAngajarii = date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}
