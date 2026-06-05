package com.duoc.guiasdespacho.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.guiasdespacho.dto.GuiaRequest;
import com.duoc.guiasdespacho.mapper.GuiaRequestMapper;
import com.duoc.guiasdespacho.model.Guia;
import com.duoc.guiasdespacho.repository.GuiaRepository;

import static com.duoc.guiasdespacho.config.CONSTANTS.*;

@Service
public class GuiaService {
    @Autowired
    private GuiaRepository guiaRepo;

    @Autowired
    private GuiaRequestMapper requestMapper;

    public List<Guia> listarGuias(){
        return guiaRepo.findAll();
    }

    public Optional<Guia> buscarGuia(int id){
        if (guiaRepo.existsById(id)){
            return Optional.of(guiaRepo.findById(id).get());
        } else {
            return Optional.empty();
        }
    }

    public Guia registrarGuia(GuiaRequest request){
        Guia guia = requestMapper.toGuia(request);
        return guiaRepo.save(guia);
    }

    public Optional<Guia> modificarGuia(int id, GuiaRequest request){
        Guia guia = requestMapper.toGuia(request);
        if (guiaRepo.existsById(id)){
            guia.setId(id);
            return Optional.of(guiaRepo.save(guia));
        }
        return Optional.empty();
    }

    public boolean eliminarGuia(int id){
        if (guiaRepo.existsById(id)){
            guiaRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Guia> filtrarGuias(String fechaString, int transportista){
        LocalDate fecha = LocalDate.parse(fechaString, DateTimeFormatter.ofPattern(DATEFORMAT));
        return guiaRepo.findByTransportista_IdAndFecha(transportista, fecha);
    }

}
