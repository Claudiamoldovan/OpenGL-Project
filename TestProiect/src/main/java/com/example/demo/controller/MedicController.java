package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dataTransferObjects.MedicDTO;
import com.example.demo.services.MedicService;

@RestController
public class MedicController {
    private MedicService medicService;

    @Autowired
    public MedicController(MedicService medicService) {
        this.medicService = medicService;
    }

    @GetMapping("/admin/medics")
    public ResponseEntity getMedics(){
        return ResponseEntity.status(HttpStatus.OK).body(medicService.getAllMedics());
    }

    @GetMapping("/medic/{id}")
    public ResponseEntity getMedic(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(medicService.getOneMedic(id));
    }

    @PostMapping("/user/medic")
    public String createMedic(@RequestBody MedicDTO medicViewModel) {
        medicService.createMedic(medicViewModel);
        return "Save...";
    }

    @PutMapping("/editmedic/{id}")
    public String updateMedic(@RequestBody MedicDTO medicViewModel, @PathVariable Long id) {
        medicService.updateMedic(medicViewModel, id);
        return "Update..";
    }


}