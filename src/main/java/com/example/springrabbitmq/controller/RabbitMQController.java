package com.example.springrabbitmq.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springrabbitmq.request.SendMessageBody;
import com.example.springrabbitmq.util.RabbitMQUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.GetResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitMQController {

    @Autowired
    private RabbitMQUtil rabbitMQUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/sendMessage")
    public ResponseEntity<?> SendMessage(@RequestBody SendMessageBody sendMessageBody) {
        try {
            rabbitMQUtil.SendMessage(sendMessageBody.getQueueName(),
                    objectMapper.writeValueAsString(sendMessageBody.getMessage()));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/getOneMessage")
    public ResponseEntity<?> GetOneMessage(@RequestParam String queueName) {
        try {
            GetResponse response = rabbitMQUtil.ReadOneMessage(queueName);
            if (response == null) {
                return ResponseEntity.noContent().build();
            }
            System.out.println(response);
            String result = new String(response.getBody(), java.nio.charset.StandardCharsets.UTF_8);
            System.out.println(result);
            Map<String, Object> jsonMap = objectMapper.readValue(result, Map.class);
            return ResponseEntity.ok().body(jsonMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
