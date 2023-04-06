package com.example.demo.services;

import com.example.demo.dataTransferObjects.AppointmentDTO;

import java.util.List;


public interface AppointmentService {
    List<AppointmentDTO> getAppointmentsMedic(Long id);
    List<AppointmentDTO> getAppointmentsMedicUsername(String username);
    void CreateAppointment(AppointmentDTO appointmentViewModel);
    void deleteAppointment(Long id);
    List<AppointmentDTO> getAllAppointments();

}
