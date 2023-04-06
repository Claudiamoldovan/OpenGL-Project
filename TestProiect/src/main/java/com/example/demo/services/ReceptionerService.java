package com.example.demo.services;

import com.example.demo.dataTransferObjects.ReceptionerDTO;
import com.example.demo.models.Receptioner;

import java.util.List;

public interface ReceptionerService {
    List<ReceptionerDTO> getAllReceptioners();
    ReceptionerDTO getOneReceptioner(Long id);
    void createReceptioner(ReceptionerDTO receptionerDTO);
    Receptioner updateReceptioner(ReceptionerDTO receptionerDTO, Long id);

}

