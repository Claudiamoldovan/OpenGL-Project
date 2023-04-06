package com.example.demo.services;

import com.example.demo.dataTransferObjects.UserDTO;
import com.example.demo.models.User;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getOneUser(Long id);
    void createUser(UserDTO userDTO);
    User updateUser(UserDTO userDTO, Long id);
    void deleteUser(Long id);


}
