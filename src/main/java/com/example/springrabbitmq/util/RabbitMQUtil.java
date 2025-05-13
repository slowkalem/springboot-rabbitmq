package com.example.springrabbitmq.util;

import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;

@Service
public class RabbitMQUtil {
    private final Channel channel;

    public RabbitMQUtil(Channel channel) {
        this.channel = channel;
    }

    public void SendMessage(String queueName, String message) throws Exception {
        channel.queueDeclare(queueName, true, false, false, null); // Ensure queue exists
        channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
    }

    public GetResponse ReadOneMessage(String queueName) throws Exception {
        GetResponse response = channel.basicGet(queueName, true);
        return response;
    }
}
