package com.duoc.guiasdespacho.rabbitmq.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {

    public static final String GUIAS_QUEUE = "guiasQueue";
    public static final String GUIAS_EXCHANGE = "guiasExchange";
    public static final String GUIAS_ROUTING_KEY = "guiasKey";


    public static final String DLX_QUEUE = "dlxQueue";
    public static final String DLX_EXCHANGE = "dlxExchange";
    public static final String DLX_ROUTING_KEY = "dlxKey";

    @Bean
    public DirectExchange guiasExchange(){
        return new DirectExchange(GUIAS_EXCHANGE);
    }

    @Bean
    public DirectExchange dlxExchange(){
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Queue guiasQueue(){
        Map<String, Object> args = new HashMap<>();

        args.put("x-dead-letter-exchange", DLX_EXCHANGE);
        args.put("x-dead-letter-routing-key", DLX_ROUTING_KEY);

        return new Queue(GUIAS_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue dlxQueue(){
        return new Queue(DLX_QUEUE, true);
    }

    @Bean
    public Binding bindingGuias(
        @Qualifier(GUIAS_QUEUE) Queue cola, 
        @Qualifier(GUIAS_EXCHANGE) DirectExchange exchange
    ){
        return BindingBuilder.bind(cola).to(exchange).with(GUIAS_ROUTING_KEY);
    }

    @Bean
    public Binding bindingDlx(
        @Qualifier(DLX_QUEUE) Queue cola, 
        @Qualifier(DLX_EXCHANGE) DirectExchange exchange
    ){
        return BindingBuilder.bind(cola).to(exchange).with(DLX_ROUTING_KEY);
    }

    





    

}
