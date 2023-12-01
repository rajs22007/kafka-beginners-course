package io.siesta.demos;

import io.siesta.demos.utils.PropertyUtil;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());

    public static void main(String[] args) {
        LOGGER.info("I am a kafka consumer!");

        String groupId = "my-java-application";
        String topic = "demo_java";

        Properties properties = PropertyUtil.getProperties();

        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.toString());

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(Arrays.asList(topic));

            while (true) {
                LOGGER.info("Polling");

                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    LOGGER.info("Key: {}, Value: {}\n Partition: {}, Offset: {}",
                            consumerRecord.key(), consumerRecord.value(), consumerRecord.partition(), consumerRecord.offset());
                }
            }
        }

    }
}
