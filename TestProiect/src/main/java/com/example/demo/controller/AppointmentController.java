package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dataTransferObjects.AppointmentDTO;
import com.example.demo.services.AppointmentService;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/Allappointments/{id}")
    public ResponseEntity getAppointmentsMedic(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body( appointmentService.getAppointmentsMedic(id));
    }

    @GetMapping("/MedicUsernameAppointment/{username}")
    public ResponseEntity getAppointmentsMedicUsername(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAppointmentsMedicUsername(username));
    }

    @PostMapping("/newappointment")
    public String createAppointment(@RequestBody AppointmentDTO appointmentViewModel) {
        appointmentService.CreateAppointment(appointmentViewModel);
        return "Saved...";
    }

    @DeleteMapping("/deleteappointment/{id}")
    public String deletePacient(@PathVariable Long id){
        appointmentService.deleteAppointment(id);
        return "Delete...";
    }

}