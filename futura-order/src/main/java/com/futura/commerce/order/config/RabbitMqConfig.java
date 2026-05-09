package com.futura.commerce.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for seckill order processing queue and exchange
 *
 * @author Vitalii
 */
@Configuration
public class RabbitMqConfig {

    public static final String SECKILL_PAY_QUEUE = "seckill.pay.queue";
    public static final String SECKILL_PAY_EXCHANGE = "seckill.pay.exchange";
    public static final String SECKILL_PAY_ROUTING_KEY = "seckill.pay.routing.key";

    @Bean
    public Queue seckillPayQueue() {
        return new Queue(SECKILL_PAY_QUEUE, true);
    }

    @Bean
    public DirectExchange seckillPayExchange() {
        return new DirectExchange(SECKILL_PAY_EXCHANGE, true, false);
    }

    @Bean
    public Binding seckillPayBinding(Queue seckillPayQueue, DirectExchange seckillPayExchange) {
        return BindingBuilder.bind(seckillPayQueue).to(seckillPayExchange).with(SECKILL_PAY_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
