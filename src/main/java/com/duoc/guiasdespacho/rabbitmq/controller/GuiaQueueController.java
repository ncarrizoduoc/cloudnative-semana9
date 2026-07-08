package com.duoc.guiasdespacho.rabbitmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.guiasdespacho.dto.GuiaRequest;
import com.duoc.guiasdespacho.mapper.GuiaRequestMapper;
import com.duoc.guiasdespacho.model.Guia;
import com.duoc.guiasdespacho.rabbitmq.service.ConsumidorService;
import com.duoc.guiasdespacho.rabbitmq.service.ProductorService;
import com.duoc.guiasdespacho.s3.service.AwsServiceImpl;

import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/rabbitmq/guias")
public class GuiaQueueController {

    @Autowired
    private GuiaRequestMapper requestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductorService productorService;

    @Autowired
    private ConsumidorService consumidorService;

    @Autowired
    private AwsServiceImpl awsService;

    @PostMapping
    public ResponseEntity<String> enviarMesajeGuia(@Valid @RequestBody GuiaRequest request){
        Guia guia = requestMapper.toGuia(request);
        String mensaje = objectMapper.writeValueAsString(guia);
        productorService.sendMessage(mensaje);
        return ResponseEntity.ok("Mensaje enviado a la cola: " + mensaje);
    }

    @GetMapping("/consume")
    public ResponseEntity<String> consumirMensajeGuia(@RequestParam("bucketName") String bucketName){
        String mensaje = consumidorService.receiveMessage();
        if (mensaje == null){
            return ResponseEntity.status(404).body("No hay mensajes en la cola.");
        }
        Guia guia = objectMapper.readValue(mensaje, Guia.class);
        String key = awsService.subirGuia(bucketName, guia);
        return ResponseEntity.ok("Se ha guardado la guia en S3 con el key: " + key + "\n" + guia.toString());
            
    }

}
