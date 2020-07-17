package com.lyf.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {
    private static final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    // 默认direct
    @Bean
    public Queue queue() {
        return new Queue("queue");
    }

    // topic
    @Bean(name = "topic")
    public Queue queueMessage() {
        return new Queue("topic");
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("topic.exchange");
    }

    @Bean
    Binding bindingExchangeMessage(@Qualifier("topic") Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    // 手动ack确认
    @Bean(name = "fanout")
    Queue fanoutQueue() {
        return new Queue("fanout");
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout.exchange");
    }

    @Bean
    Binding bindingFQ1(@Qualifier("fanout") Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    /**
     * 定制化amqp模版
     * <p>
     * ConfirmCallback接口用于ack回调   即消息发送到exchange  ack
     * ReturnCallback接口用于消息发送失败回调  即消息发送不到任何一个队列中  ack
     */
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 消息返回, 需要配置 publisher-returns: true
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            logger.debug("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}",
                    correlationId, replyCode, replyText, exchange, routingKey);
        });

        // 消息确认, 需要配置 publisher-confirms: true
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                //logger.debug("消息发送到exchange成功,id: {}", correlationData.getId());
                logger.debug("消息发送到exchange成功");
            } else {
                logger.debug("消息发送到exchange失败,原因: {}", cause);
            }
        });
        return rabbitTemplate;
    }


     /**
      * Dead Letter Exchanges（DLX）
      * RabbitMQ的Queue可以配置x-dead-letter-exchange和x-dead-letter-routing-key（可选）两个参数，
      * 如果队列内出现了dead letter，则按照这两个参数重新路由转发到指定的队列。
      * x-dead-letter-exchange：出现dead letter之后将dead letter重新发送到指定exchange
      * x-dead-letter-routing-key：出现dead letter之后将dead letter重新按照指定的routing-key发送
      * ————————————————
      * 版权声明：本文为CSDN博主「酸酸的酸酸酱」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
      * 原文链接：https://blog.csdn.net/zhangyuxuan2/article/details/82986802
      *
      * 理解: A队列 通过参数指定 B队列为自己的死信队列, 当A中的消息为死信时, 消息转发到B队列
      * 实现: 在A队列中设置x-dead-letter-exchange 和 x-dead-letter-routing-key参数指定交换机和队列
      * 成为死信的方式:
      * 方式1: 队列放满
      * 方式2: 设置超时时间(x-message-ttl)
      */

    // 死信队列
    @Bean("A_EXCHANGE")
    Exchange A_EX() {
        return ExchangeBuilder.directExchange("A_EXCHANGE").durable(true).build();
    }

    @Bean("A")
    Queue A_Q() {
        Map<String, Object> args = new HashMap<>(3);
        args.put("x-dead-letter-exchange", "B_EXCHANGE");
        args.put("x-dead-letter-routing-key", "B_KEY");
        args.put("x-message-ttl", 5000);// 5s超时
        return QueueBuilder.durable("A").withArguments(args).build();
    }

    @Bean
    Binding BindingA(@Qualifier("A") Queue queue, @Qualifier("A_EXCHANGE") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("A_KEY").noargs();
    }

    @Bean("B_EXCHANGE")
    Exchange B_EX() {
        return ExchangeBuilder.directExchange("B_EXCHANGE").durable(true).build();
    }

    @Bean("B")
    Queue B_Q() {
        return QueueBuilder.durable("B").build();
    }

    @Bean
    Binding BindingB(@Qualifier("B") Queue queue, @Qualifier("B_EXCHANGE") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("B_KEY").noargs();
    }

}