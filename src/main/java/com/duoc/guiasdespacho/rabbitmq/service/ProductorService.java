package com.duoc.guiasdespacho.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoc.guiasdespacho.rabbitmq.config.RabbitMQConfig;

@Service
public class ProductorService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.GUIAS_EXCHANGE, 
            RabbitMQConfig.GUIAS_ROUTING_KEY,
            message);
    }

}