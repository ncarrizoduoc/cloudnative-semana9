package com.duoc.inscripciones.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.inscripciones.rabbitmq.config.RabbitMQConfig;
import com.rabbitmq.client.GetResponse;

import static com.duoc.inscripciones.rabbitmq.config.RabbitMQConfig.*;

@Service
public class ConsumidorService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String receiveMessage(){
        return rabbitTemplate.execute(channel -> {
            // Obtener mensaje
            GetResponse response = channel.basicGet(INSCRIPCIONES_QUEUE, false);
            
            // Si no hay un mensaje retorna null
            if (response == null){
                return null;
            }

            // Se extra el deliveryTag y contenido del mensaje
            Long deliveryTag = response.getEnvelope().getDeliveryTag();
            String mensaje = new String(response.getBody(), "UTF-8");

            try{
                // Se simula un error para reenviar el mensaje a la DLQ
                if (mensaje.contains("CURSO FALLIDO")){
                    throw new IllegalArgumentException("Datos del mensaje invalido: Nombre del curso no permitido");
                }

                //Si el mensaje es valido retorna el mensaje
                channel.basicAck(deliveryTag, false);
                return mensaje;

            } catch (Exception e){
                // Se rechaza el mensaje y se envia a la DLQ
                channel.basicNack(deliveryTag, false, false);
                return "Mensaje fallido enviado a DLQ: " + mensaje;
            }
            
        });
        
    }

}
