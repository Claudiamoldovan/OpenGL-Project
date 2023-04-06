package com.example.demo.repositories;


import com.example.demo.models.Medic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicRepository extends JpaRepository<Medic, Long> {
	Optional<Medic> findByUserId(Long id);
}
