package com.example.demo.services;

import com.example.demo.dataTransferObjects.ConsultDTO;

import java.util.List;


public interface ConsultService {
    List<ConsultDTO> getAllConsults();
    List<ConsultDTO> getMedicConsults(String username);
    void createConsult(ConsultDTO consultViewModel);
}
