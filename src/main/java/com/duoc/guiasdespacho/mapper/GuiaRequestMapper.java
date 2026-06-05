package com.duoc.guiasdespacho.mapper;

import static com.duoc.guiasdespacho.config.CONSTANTS.DATEFORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.duoc.guiasdespacho.dto.GuiaRequest;
import com.duoc.guiasdespacho.exception.ResourceNotFoundException;
import com.duoc.guiasdespacho.model.Guia;
import com.duoc.guiasdespacho.model.Transportista;
import com.duoc.guiasdespacho.repository.TransportistaRepository;

@Component
public class GuiaRequestMapper {

    @Autowired
    private TransportistaRepository transpoRepo;

    public Guia toGuia(GuiaRequest request){
        Guia guia = new Guia();

        guia.setId(request.getId());
        guia.setContenido(request.getContenido());
        guia.setDireccion(request.getDireccion());
        
        //Convertir fecha de String a LocalDate
        LocalDate fecha = LocalDate.parse(request.getFecha(), DateTimeFormatter.ofPattern(DATEFORMAT));
        guia.setFecha(fecha);

        //Convertir transportista de Integer (se ingresa el ID) a Transportista
        int transpoId = request.getTransportista();
        if (transpoRepo.existsById(transpoId)){
            Transportista transpo = transpoRepo.findById(transpoId).get();
            guia.setTransportista(transpo);
        } else {
            throw new ResourceNotFoundException("No existe el transportista con el ID ingresado");
        }
        return guia;
    }

}
