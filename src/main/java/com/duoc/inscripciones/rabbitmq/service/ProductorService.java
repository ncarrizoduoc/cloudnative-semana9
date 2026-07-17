package com.duoc.inscripciones.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.inscripciones.rabbitmq.config.RabbitMQConfig;

@Service
public class ProductorService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.INSCRIPCIONES_EXCHANGE, 
            RabbitMQConfig.INSCRIPCIONES_ROUTING_KEY,
            message);
    }

}