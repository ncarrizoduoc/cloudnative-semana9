package com.duoc.guiasdespacho.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.guiasdespacho.model.Transportista;

@Repository
public interface TransportistaRepository extends JpaRepository<Transportista, Integer>{


}
