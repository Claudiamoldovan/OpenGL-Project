package com.example.demo.services.impl;


import com.example.demo.dataTransferObjects.RoleDTO;
import com.example.demo.models.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
	
	private RoleRepository roleRepository;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public List<RoleDTO> getAllRoles(){
		List<RoleDTO> roles = new ArrayList<>();
		roleRepository.findAll().forEach(role -> roles.add(role.convertToDTO(role)));
		return roles;
	}
	
	public RoleDTO getOneRole(Long id) {
		Role role = roleRepository.getOne(id);
		if (role != null)
			return role.convertToDTO(role);
		else
			return null;
	}
	
	public void createRole(RoleDTO roleDTO) {
		Role role = new Role().convertToModel(roleDTO);
		roleRepository.save(role);
	}
	
	public void deleteRole(Long id) {
		roleRepository.deleteById(id);
	}
}
