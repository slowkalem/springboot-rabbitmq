package com.example.springrabbitmq.request;

import lombok.Data;

@Data
public class SendMessageBody {
    private String queueName;
    private Object message;
}
