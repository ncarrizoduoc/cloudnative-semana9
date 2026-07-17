package com.duoc.inscripciones.rabbitmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.inscripciones.dto.InscripcionRequest;
import com.duoc.inscripciones.mapper.InscripcionRequestMapper;
import com.duoc.inscripciones.model.Inscripcion;
import com.duoc.inscripciones.rabbitmq.service.ConsumidorService;
import com.duoc.inscripciones.rabbitmq.service.ProductorService;
import com.duoc.inscripciones.s3.service.AwsServiceImpl;

import jakarta.validation.Valid;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/rabbitmq/inscripciones")
public class InscripcionQueueController {

    @Autowired
    private InscripcionRequestMapper requestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductorService productorService;

    @Autowired
    private ConsumidorService consumidorService;

    @Autowired
    private AwsServiceImpl awsService;

    // Enviar mensaje a RabbitMQ con datos de inscripcion
    @PostMapping("/produce")
    public ResponseEntity<String> enviarMesajeInscripcion(@Valid @RequestBody InscripcionRequest request){
        Inscripcion inscripcion = requestMapper.toInscripcion(request);
        String mensaje = objectMapper.writeValueAsString(inscripcion);
        productorService.sendMessage(mensaje);
        return ResponseEntity.ok("Mensaje enviado a la cola: " + mensaje);
    }

    // Consumir mensaje de RabbitMQ y subir resumen de inscripcion a S3
    @GetMapping("/consume")
    public ResponseEntity<String> consumirMensajeInscripcion(@RequestParam("bucketName") String bucketName){
        String mensaje = consumidorService.receiveMessage(); // Leer mensaje desde la cola de RabbitMQ
        if (mensaje == null){
            return ResponseEntity.status(404).body("No hay mensajes en la cola.");
        }
        try{
            Inscripcion inscripcion = objectMapper.readValue(mensaje, Inscripcion.class); // Convertir el mensaje a Inscripcion
            String key = awsService.subirInscripcion(bucketName, inscripcion); // Subir el resumen de la inscripcion a S3
            return ResponseEntity.ok("Se ha guardado la inscripcion en S3 con el key: " + key + "\n" + inscripcion.toString());
        }catch (StreamReadException e){
            return ResponseEntity.badRequest().body("Error al convertir mensaje a objeto Inscripcion: " + e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Error al leer mensaje: " + e.getMessage());
        }
            
    }

}
