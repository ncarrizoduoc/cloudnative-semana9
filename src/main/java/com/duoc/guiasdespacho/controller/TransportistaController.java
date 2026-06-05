package com.duoc.guiasdespacho.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.duoc.guiasdespacho.model.Transportista;
import com.duoc.guiasdespacho.service.TransportistaService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/api/transportistas")
public class TransportistaController {

    @Autowired
    private TransportistaService transportistaService;

    @GetMapping
    public ResponseEntity<List<Transportista>> listarTransportistas(){
        return ResponseEntity.ok(transportistaService.listarTransportistas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transportista> buscarTransportista(@PathVariable @PositiveOrZero int id){
        return transportistaService.buscarTransportista(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transportista> registrarTransportista(@Valid @RequestBody Transportista transportista){
        Transportista creado = transportistaService.registrarTransportista(transportista);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(creado.getId())
            .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transportista> modificarTransportista(@PathVariable @PositiveOrZero int id, @Valid @RequestBody Transportista transportista){
        return transportistaService.modificarTransportista(id, transportista)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTransportista(@PathVariable @PositiveOrZero int id){
        return transportistaService.eliminarTransportista(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }


}
