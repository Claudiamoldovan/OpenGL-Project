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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dataTransferObjects.PacientDTO;
import com.example.demo.services.PacientService;

@RestController
public class PacientController {
    private PacientService pacientService;

    @Autowired
    public PacientController(PacientService pacientService) {
        this.pacientService = pacientService;
    }

    @GetMapping("/pacients")
    public ResponseEntity getPacients(){
        return ResponseEntity.status(HttpStatus.OK).body(pacientService.getAllPacients());
    }

    @GetMapping("/medic/pacients")
    public ResponseEntity getPacientsMedic(){
        return ResponseEntity.status(HttpStatus.OK).body(pacientService.getAllPacients());
    }

    @GetMapping("/pacient/{id}")
    public ResponseEntity getPacient(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(pacientService.getOnePacient(id));
    }

    @PostMapping("/newpacient")
    public String createPacient(@RequestBody PacientDTO pacientDTO) {
        pacientService.createPacient(pacientDTO);
        return "Save..";
    }

    @PutMapping("/editpacient/{id}")
    public String updatePacient(@RequestBody PacientDTO pacientDTO, @PathVariable Long id) {
        pacientService.updatePacient(pacientDTO, id);
        return "Update...";
    }

    @DeleteMapping("/deletepacient/{id}")
    public String deletePacient(@PathVariable Long id){
        pacientService.deletePacient(id);

        return "Delete...";
    }
}