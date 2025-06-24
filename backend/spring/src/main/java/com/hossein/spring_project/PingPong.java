package com.hossein.spring_project;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class PingPong {
    record PingPongRecord(String result){}
    @GetMapping("/ping")
    public PingPongRecord Pong() {
        return new PingPongRecord("pong");
    }
    
}
