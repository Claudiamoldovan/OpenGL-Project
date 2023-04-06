package com.example.demo.services;


import com.example.demo.dataTransferObjects.PacientDTO;
import com.example.demo.models.Pacient;

import java.util.List;

public interface PacientService {
    List<PacientDTO> getAllPacients();
    PacientDTO getOnePacient(Long id);
    void createPacient(PacientDTO pacientDTO);
    Pacient updatePacient(PacientDTO pacientDTO, Long id);
    void deletePacient(Long id);


}
