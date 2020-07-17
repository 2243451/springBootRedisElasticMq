package com.lyf.service;

import com.rabbitmq.client.Channel;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Log4j2
public class RabbitService {

    @Autowired
    private AmqpTemplate template;
    private long start;

    public void send(String str) {
        template.convertAndSend("queue",str);
    }

    @RabbitListener(queues="queue")
    public void process(String str) {
        System.out.println("Receive:"+str);
    }

    public void sendTopic(String str) {
        template.convertAndSend("topic.exchange","topic.message",str);
    }

    @RabbitListener(queues="topic")
    public void processTopic(String str) {
        System.out.println("message:"+str);
    }

    public void sendAck(String str) {
        template.convertAndSend("fanout.exchange","",str);
    }

    public void sendWrong(String str) {
        template.convertAndSend("fanout.wrong","topic.wrong",str);
    }

    //手动确认消息
    @RabbitListener(queues = "fanout")
    public void FQ(Message message, Channel channel) throws IOException {
        // 采用手动应答模式, 手动确认应答更为安全稳定
        System.out.println("FQ:" + new String(message.getBody()));
        // 第一个参数是消息标识, 第二个是批量确认;
        // false当前消息确认, true此次之前的消息确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    public void sendA(String str) {
        template.convertAndSend("A_EXCHANGE","A_KEY",str);
        System.out.println("A [recive]: " + str);
        start = System.currentTimeMillis();
    }

    @RabbitListener(queues="B")
    public void processDeadLetter(String str) {
        System.out.println("B [recive] 耗时: "+ (System.currentTimeMillis()-start) +"ms");
        System.out.println("Receive:"+str);
    }

}
