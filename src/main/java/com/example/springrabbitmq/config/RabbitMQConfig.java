package com.example.springrabbitmq.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    private Connection connection;
    private Channel channel;

    @Bean
    public synchronized Connection rabbitConnection() throws Exception {
        if (connection == null || !connection.isOpen()) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setAutomaticRecoveryEnabled(true); // Enable auto-reconnect
            factory.setNetworkRecoveryInterval(5000); // Retry every 5 seconds

            connection = factory.newConnection();
        }
        return connection;
    }

    @Bean
    public synchronized Channel rabbitChannel(Connection connection) throws Exception {
        if (channel == null || !channel.isOpen()) {
            channel = connection.createChannel();
        }
        return channel;
    }

    @PreDestroy
    public void shutdown() throws Exception {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }
}
