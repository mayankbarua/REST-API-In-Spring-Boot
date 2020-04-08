package com.prototype1.services;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.prototype1.dao.ElasticSearchDao;

@Service
public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ElasticSearchDao ElasticSearchDao;

    @KafkaListener(topics = "bigdataindexing", groupId = "group_id")
    public void consume(ConsumerRecord<String, String> record) throws ExecutionException, InterruptedException {
        logger.info("Consumed Message - {} ", record);
        if (record.key().toString().equals("index")) {
        	JSONObject rootNode = new JSONObject(record.value().toString());
            String objectId = rootNode.getString("objectId");
            ElasticSearchDao.index(objectId, record.value().toString());
        } else if (record.key().toString().equals("delete")) {
        	ElasticSearchDao.delete(record.value().toString());
        }
    }

}
