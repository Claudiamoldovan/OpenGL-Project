package com.example.demo.services;


import com.example.demo.dataTransferObjects.MedicDTO;
import com.example.demo.models.Medic;

import java.util.List;

public interface MedicService {


    List<MedicDTO> getAllMedics();
    MedicDTO getOneMedic(Long id);
    void createMedic(MedicDTO medicViewModel);
    Medic updateMedic(MedicDTO medicViewModel, Long id);

}
