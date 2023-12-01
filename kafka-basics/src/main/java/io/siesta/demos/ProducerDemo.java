package io.siesta.demos;

import io.siesta.demos.utils.PropertyUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {
        LOGGER.info("Hello World!");

        Properties properties = PropertyUtil.getProperties();

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "Hello World!");

            producer.send(producerRecord);

            producer.flush();
        }

    }
}
