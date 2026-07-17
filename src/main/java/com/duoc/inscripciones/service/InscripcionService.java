package com.duoc.inscripciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.inscripciones.repository.InscripcionRepository;
import com.duoc.inscripciones.model.Inscripcion;

import java.util.List;
import java.util.Optional;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepo;

    public List<Inscripcion> listarInscripciones(){
        return inscripcionRepo.findAll();
    }

    public Optional<Inscripcion> buscarInscripcion(Long id){
        if (inscripcionRepo.existsById(id)){
            return Optional.of(inscripcionRepo.findById(id).get());
        } else {
            return Optional.empty();
        }
    }

    public Inscripcion registrarInscripcion(Inscripcion inscripcion){
        inscripcion.setId(null);
        return inscripcionRepo.save(inscripcion);
    }

    public boolean eliminarInscripcion(Long id){
        if (inscripcionRepo.existsById(id)){
            inscripcionRepo.deleteById(id);
            return true;
        }
        return false;
    }

}
