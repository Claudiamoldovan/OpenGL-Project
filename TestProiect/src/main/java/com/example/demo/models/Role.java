package com.example.demo.models;


import com.example.demo.dataTransferObjects.RoleDTO;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.*;

@Entity
@Table (name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60)
	private RoleName name;
	
	//@ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
	//@JsonIgnore
	//private Set<User> users = new HashSet<>();
	
	public Role() {}
	
	public Role convertToModel(RoleDTO roleDTO) {
		Role entity = new Role();
		if(roleDTO.getId() != null)
			entity.setId(roleDTO.getId());
		entity.setName(roleDTO.getName());
		return entity;
	}
	
	public RoleDTO convertToDTO(Role role) {
		RoleDTO roleDTO = new RoleDTO();
		if (role.getId() != null)
			roleDTO.setId(role.getId());
		roleDTO.setName(role.getName());
		
		return roleDTO;
	}
	
	public Role(RoleName name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleName getName() {
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
	}
	
}
