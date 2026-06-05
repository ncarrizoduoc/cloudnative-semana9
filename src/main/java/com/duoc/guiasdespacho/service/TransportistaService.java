package com.duoc.guiasdespacho.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.guiasdespacho.model.Transportista;
import com.duoc.guiasdespacho.repository.TransportistaRepository;

@Service
public class TransportistaService {

    @Autowired
    private TransportistaRepository transpoRepo;

    public List<Transportista> listarTransportistas(){
        return transpoRepo.findAll();
    }

    public Optional<Transportista> buscarTransportista(int id){
        return transpoRepo.findById(id);
    }

    public Transportista registrarTransportista(Transportista transportista){
        return transpoRepo.save(transportista);
    }

    public Optional<Transportista> modificarTransportista(int id, Transportista transportista){
        if (transpoRepo.existsById(id)){
            transportista.setId(id);
            return Optional.of(transpoRepo.save(transportista));
        } else {
            return Optional.empty();
        }
    }

    public boolean eliminarTransportista(int id){
        if (transpoRepo.existsById(id)){
            transpoRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
