package com.example.demo.service.impl;//package com.example.demo.services.impl;


import com.example.demo.dataTransferObjects.MedicDTO;
import com.example.demo.models.Medic;
import com.example.demo.models.User;
import com.example.demo.repositories.MedicRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.impl.MedicServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MedicServiceImplTest {

    @InjectMocks
    MedicServiceImpl medicService;

    @Mock
    UserRepository userRepository;

    @Mock
    MedicRepository medicRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllMedics() {
        List<Medic> medicList = new ArrayList<>();
        medicList.add(new Medic("Dr. John Smith", "AB123", "Cardiology", "john.smith@example.com", null));
        medicList.add(new Medic("Dr. Jane Doe", "CD456", "Pediatrics", "jane.doe@example.com", null));

        when(medicRepository.findAll()).thenReturn(medicList);

        List<MedicDTO> result = medicService.getAllMedics();

        assertEquals(2, result.size());
        assertEquals("Dr. John Smith", result.get(0).getNume());
        assertEquals("Cardiology", result.get(0).getSpecialitate());
        assertEquals("jane.doe@example.com", result.get(1).getEmail());
    }

    @Test
    public void testGetOneMedic() {
        Medic medic = new Medic("Dr. John Smith", "AB123", "Cardiology", "john.smith@example.com", null);


        when(medicRepository.getOne(1L)).thenReturn(medic);

        MedicDTO result = medicService.getOneMedic(1L);

        assertEquals("Dr. John Smith", result.getNume());
        assertEquals("AB123", result.getCodParafa());
        assertEquals("Cardiology", result.getSpecialitate());
        assertEquals("john.smith@example.com", result.getEmail());
    }


    @Test
    public void testCreateMedic() {
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setNume("John Doe");
        medicDTO.setCodParafa("1234");
        medicDTO.setSpecialitate("Cardiology");
        medicDTO.setEmail("john.doe@example.com");
        medicDTO.setDataAngajarii("2021-04-02");
        medicDTO.setUsername("johndoe");

        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("$2a$10$vd7Zz9/yq8E7WJkYOLy6c.AjIWvD8A50vfZ6UJok6UcX9nTOd37C6"); // password: password
        //user.setEnabled(true);

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
        when(medicRepository.save(any(Medic.class))).thenReturn(new Medic());

        medicService.createMedic(medicDTO);

        ArgumentCaptor<Medic> captor = ArgumentCaptor.forClass(Medic.class);
        verify(medicRepository, times(1)).save(captor.capture());
        Medic savedMedic = captor.getValue();

        assertEquals("John Doe", savedMedic.getNume());
        assertEquals("1234", savedMedic.getCodParafa());
        assertEquals("Cardiology", savedMedic.getSpecialitate());
        assertEquals("john.doe@example.com", savedMedic.getEmail());
        assertEquals("2021-04-02", savedMedic.getDataAngajarii());
        assertEquals(user, savedMedic.getUser());

        verify(userRepository, times(1)).findByUsername("johndoe");
    }
    @Test
    public void testUpdateMedic() {
        Long id = 1L;
        MedicDTO medicDTO = new MedicDTO();
        medicDTO.setNume("John Doe");
        medicDTO.setCodParafa("123456");
        medicDTO.setSpecialitate("Cardiologie");
        medicDTO.setEmail("john.doe@example.com");
        medicDTO.setDataAngajarii("2022-03-01");

        Medic existingMedic = new Medic();
        existingMedic.setId(id);
        existingMedic.setNume("Jane Doe");
        existingMedic.setCodParafa("654321");
        existingMedic.setSpecialitate("Ortopedie");
        existingMedic.setEmail("jane.doe@example.com");
        existingMedic.setDataAngajarii("2021-05-01");

        when(medicRepository.findById(id)).thenReturn(Optional.of(existingMedic));
        when(medicRepository.save(existingMedic)).thenReturn(existingMedic);

        Medic updatedMedic = medicService.updateMedic(medicDTO, id);

        assertEquals(existingMedic.getId(), updatedMedic.getId());
        assertEquals(medicDTO.getNume(), updatedMedic.getNume());
        assertEquals(medicDTO.getCodParafa(), updatedMedic.getCodParafa());
        assertEquals(medicDTO.getSpecialitate(), updatedMedic.getSpecialitate());
        assertEquals(medicDTO.getEmail(), updatedMedic.getEmail());
        assertEquals(medicDTO.getDataAngajarii(), updatedMedic.getDataAngajarii());
    }
}