package com.example.demo.controller;

import com.example.demo.dataTransferObjects.AppointmentDTO;
import com.example.demo.dataTransferObjects.ConsultDTO;
import com.example.demo.exceptions.ApiExceptionResponse;
import com.example.demo.services.AppointmentService;
import com.example.demo.services.ConsultService;
import com.example.demo.services.DiagnosticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diagnostic")
public class DiagnosticController{

    private final DiagnosticService diagnosticService;
@Autowired
    public DiagnosticController(DiagnosticService diagnosticService) {
        this.diagnosticService=diagnosticService;
    }

    @GetMapping("/allDiagnostic")
    public ResponseEntity findAllConsult() {
        return ResponseEntity.status(HttpStatus.OK).body(diagnosticService.getAllDiagnostics());
    }
}
