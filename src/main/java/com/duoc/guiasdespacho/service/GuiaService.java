package com.duoc.guiasdespacho.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.guiasdespacho.dto.GuiaRequest;
import com.duoc.guiasdespacho.mapper.GuiaRequestMapper;
import com.duoc.guiasdespacho.model.Guia;
import com.duoc.guiasdespacho.repository.GuiaRepository;

@Service
public class GuiaService {
    @Autowired
    private GuiaRepository guiaRepo;

    @Autowired
    private GuiaRequestMapper requestMapper;

    public List<Guia> listarGuias(){
        return guiaRepo.findAll();
    }

    public Optional<Guia> buscarGuia(Long id){
        if (guiaRepo.existsById(id)){
            return Optional.of(guiaRepo.findById(id).get());
        } else {
            return Optional.empty();
        }
    }

    public Guia registrarGuia(GuiaRequest request){
        Guia guia = requestMapper.toGuia(request);
        guia.setId(null);
        return guiaRepo.save(guia);
    }

    public Optional<Guia> modificarGuia(Long id, GuiaRequest request){
        Guia guia = requestMapper.toGuia(request);
        if (guiaRepo.existsById(id)){
            guia.setId(id);
            return Optional.of(guiaRepo.save(guia));
        }
        return Optional.empty();
    }

    public boolean eliminarGuia(Long id){
        if (guiaRepo.existsById(id)){
            guiaRepo.deleteById(id);
            return true;
        }
        return false;
    }

}
