package com.duoc.guiasdespacho.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.guiasdespacho.rabbitmq.config.RabbitMQConfig;

@Service
public class ConsumidorService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String receiveMessage(){
        String mensaje = (String) rabbitTemplate.receiveAndConvert(RabbitMQConfig.GUIAS_QUEUE);
        return mensaje;
    }

}
