package com.Kafka.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerDemo {

    Properties properties;

    public KafkaProducerDemo(Properties properties) {
        super();
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void sendMessage(String msg) {

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        ProducerRecord<String, String> record = new ProducerRecord<String, String>(properties.getProperty("topic"),
                msg);
        producer.send(record);

        producer.close();

    }

}

