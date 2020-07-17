package com.lyf.controller;

import com.lyf.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rabbitmq")
public class RabbitController {

    @Autowired
    private RabbitService rabbitService;

    @GetMapping("send")
    public String send(String msg) {
        rabbitService.send(msg);
        return "OK";
    }

    @GetMapping("send/topic")
    public String sendTopic(String msg) {
        rabbitService.sendTopic(msg);
        return "OK";
    }

    @GetMapping("send/ack")
    public String sendAck(String msg) {
        rabbitService.sendAck(msg);
        return "OK";
    }

    @GetMapping("send/wrong")
    public String sendWrong(String msg) {
        rabbitService.sendWrong(msg);
        return "OK";
    }

    @GetMapping("send/dead")
    public String sendDead(String msg) {
        rabbitService.sendA(msg);
        return "OK";
    }

}
