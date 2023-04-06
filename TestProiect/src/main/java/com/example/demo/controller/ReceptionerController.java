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

import com.example.demo.dataTransferObjects.ReceptionerDTO;
import com.example.demo.services.ReceptionerService;

@RestController
public class ReceptionerController {
    private ReceptionerService receptionerService;

    @Autowired
    public ReceptionerController(ReceptionerService receptionerService) {
        this.receptionerService = receptionerService;
    }

    @GetMapping("/admin/receptioners")
    public ResponseEntity getReceptioners(){
        return ResponseEntity.status(HttpStatus.OK).body(receptionerService.getAllReceptioners());
    }

    @GetMapping("/receptioner/{id}")
    public ResponseEntity getReceptioner(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(receptionerService.getOneReceptioner(id));
    }

    @PostMapping("/user/receptioner")
    public String createReceptioner(@RequestBody ReceptionerDTO receptionerDTO) {
        receptionerService.createReceptioner(receptionerDTO);
        return "Save...";
    }

    @PutMapping("editreceptioner/{id}")
    public String  updateReceptioner(@RequestBody ReceptionerDTO receptionerDTO, @PathVariable Long id) {
        receptionerService.updateReceptioner(receptionerDTO, id);
        return "Save..";
    }
}