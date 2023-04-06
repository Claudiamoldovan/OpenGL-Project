package com.example.demo.services;

import com.example.demo.dataTransferObjects.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAllRoles();
    RoleDTO getOneRole(Long id);
    void createRole(RoleDTO roleDTO);
    void deleteRole(Long id);


}
