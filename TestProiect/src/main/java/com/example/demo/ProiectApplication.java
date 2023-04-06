package com.example.demo;

import com.example.demo.dataTransferObjects.RoleDTO;
import com.example.demo.dataTransferObjects.UserDTO;
import com.example.demo.models.RoleName;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.impl.RoleServiceImpl;
import com.example.demo.services.impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repositories")

public class ProiectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProiectApplication.class, args);
	}
@Bean
CommandLineRunner init(UserRepository userRepository, RoleRepository roleRepository){
		return args->{
			UserServiceImpl userService=new UserServiceImpl(userRepository, roleRepository);
			//MedicServiceImpl medicService=new MedicServiceImpl(userRepository,medicRepository);
			RoleServiceImpl roleService=new RoleServiceImpl(roleRepository);
			RoleDTO role=new RoleDTO();
			role.setName(RoleName.ROLE_ADMIN);
			RoleDTO role1=new RoleDTO();
			role1.setName(RoleName.ROLE_MEDIC);
			RoleDTO role2=new RoleDTO();
			role2.setName(RoleName.ROLE_RECEPTIONER);
			roleService.createRole(role);
			roleService.createRole(role1);
			roleService.createRole(role2);


			UserDTO user = new UserDTO();
			user.setRole("ROLE_MEDIC");
			user.setUsername("johndoe");
			user.setPassword("$2a$10$vd7Zz9/yq8E7WJkYOLy6c.AjIWvD8A50vfZ6UJok6UcX9nTOd37C6");
			userService.createUser(user);
//			MedicDTO medicDTO = new MedicDTO();
//			medicDTO.setNume("John Doe");
//			medicDTO.setCodParafa("1234");
//			medicDTO.setSpecialitate("Cardiology");
//			medicDTO.setEmail("john.doe@example.com");
//			medicDTO.setDataAngajarii("2021-04-02");
//			medicDTO.setUsername("johndoe");

		//user.setEnabled(true);
			//medicService.createMedic(medicDTO);
		};

}
}
