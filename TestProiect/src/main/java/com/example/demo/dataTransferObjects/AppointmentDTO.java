package com.example.demo.dataTransferObjects;

import com.example.demo.models.Pacient;

import java.sql.Timestamp;


import com.example.demo.models.Pacient;

import java.sql.Timestamp;

public class AppointmentDTO {
    private Long id;
    private Timestamp start;
    private Timestamp end;
    private Pacient pacient;
    private String pacientId;
    private String medicId;
    private String title;

    public AppointmentDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPacientId() {
        return pacientId;
    }

    public void setPacientId(String pacientId) {
        this.pacientId = pacientId;
    }

    public String getMedicId() {
        return medicId;
    }

    public void setMedicId(String medicId) {
        this.medicId = medicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
