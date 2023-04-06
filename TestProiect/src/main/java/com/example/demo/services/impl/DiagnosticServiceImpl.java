package com.example.demo.services.impl;

import com.example.demo.models.Diagnostic;
import com.example.demo.repositories.DiagnosticRepository;
import com.example.demo.services.DiagnosticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiagnosticServiceImpl implements DiagnosticService {
	@Autowired
	private DiagnosticRepository diagnosticRepository;
	
	@Autowired
	public DiagnosticServiceImpl(DiagnosticRepository diagnosticRepository) {
		this.diagnosticRepository = diagnosticRepository;
	}
	
	public List<Diagnostic> getAllDiagnostics(){
		List<Diagnostic> diagnostics = new ArrayList<>();
		diagnosticRepository.findAll().forEach(diagnostic -> diagnostics.add(diagnostic));
		return diagnostics;
	}
	
}
