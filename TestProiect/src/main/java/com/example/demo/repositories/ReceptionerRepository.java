package com.example.demo.repositories;

import com.example.demo.models.Receptioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionerRepository extends JpaRepository<Receptioner, Long> {

}
