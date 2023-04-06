package com.example.demo.repositories;

import com.example.demo.models.Consult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultRepository extends JpaRepository<Consult, Long> {
	List<Consult> findByMedicId(Long id);
}
