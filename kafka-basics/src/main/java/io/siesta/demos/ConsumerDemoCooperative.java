package io.siesta.demos;

import io.siesta.demos.utils.PropertyUtil;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoCooperative {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerDemoCooperative.class.getSimpleName());

    public static void main(String[] args) {
        LOGGER.info("I am a kafka consumer!");

        String groupId = "my-java-application";
        String topic = "demo_java";

        Properties properties = PropertyUtil.getProperties();

        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.toString());
        properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());
        // properties.setProperty(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "..."); // strategy for static assignment

        // Create a consumer
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {

            // get a reference to the main thread
            Thread mainThread = Thread.currentThread();

            // adding the shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Detected a shutdown, let's exit by calling consumer wakeup()...");
                consumer.wakeup();

                // join the main thread to allow the execution of the code in the main thread;
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    LOGGER.error("Error in mainThread join!", e);
                }
            }
            ));

            consumer.subscribe(Arrays.asList(topic));

            // poll for data
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    LOGGER.info("Key: {}, Value: {}\n Partition: {}, Offset: {}",
                            consumerRecord.key(), consumerRecord.value(), consumerRecord.partition(), consumerRecord.offset());
                }
            }
        } catch (WakeupException e) {
            LOGGER.info("Consumer is starting to shutdown!");
        } catch (Exception e) {
            LOGGER.error("Unexpected exception in the consumer", e);
        }

    }
}
