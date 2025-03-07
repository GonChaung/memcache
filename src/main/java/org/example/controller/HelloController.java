package org.example.controller;

import org.example.dto.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hi")
    public ResponseEntity<MessageDto> hi() {
        return ResponseEntity.ok(new MessageDto("hi"));
    }

    @GetMapping("/hello")
    public ResponseEntity<MessageDto> hello() {
        return ResponseEntity.ok(new MessageDto("hello"));
    }
}
