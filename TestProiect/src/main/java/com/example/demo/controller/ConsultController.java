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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dataTransferObjects.ConsultDTO;
import com.example.demo.services.ConsultService;

@RestController
public class ConsultController {
    private final ConsultService consultService;

    @Autowired
    public ConsultController(ConsultService consultService) {
        this.consultService = consultService;
    }

    @GetMapping("/medic/consults/{username}")
    @PreAuthorize("hasRole('MEDIC')")
    public ResponseEntity getMedicConsults(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK).body( consultService.getMedicConsults(username));
    }


    @PostMapping("/newconsult")
    @PreAuthorize("hasRole('MEDIC') or hasRole ('ADMIN')")
    public String createConsult(@RequestBody ConsultDTO consultViewModel) {
        consultService.createConsult(consultViewModel);
        return "Save...";
    }

}