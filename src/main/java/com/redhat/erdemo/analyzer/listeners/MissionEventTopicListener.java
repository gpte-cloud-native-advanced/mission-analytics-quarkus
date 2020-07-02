package com.redhat.cajun.navy.process.message.listeners;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.redhat.cajun.navy.process.message.model.Message;
import com.redhat.cajun.navy.process.message.model.MissionStartedEvent;
import com.redhat.cajun.navy.process.message.model.VictimDeliveredEvent;
import com.redhat.cajun.navy.process.message.model.VictimPickedUpEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class MissionEventTopicListener {

    private static final Logger log = LoggerFactory.getLogger(MissionEventTopicListener.class);

    private static final String TYPE_MISSION_STARTED_EVENT = "MissionStartedEvent";
    private static final String TYPE_MISSION_PICKEDUP_EVENT = "MissionPickedUpEvent";
    private static final String TYPE_MISSION_COMPLETED_EVENT = "MissionCompletedEvent";
    private static final String[] ACCEPTED_MESSAGE_TYPES = {TYPE_MISSION_STARTED_EVENT, TYPE_MISSION_PICKEDUP_EVENT, TYPE_MISSION_COMPLETED_EVENT};

    private static final String SIGNAL_MISSION_STARTED = "MissionStarted";
    private static final String SIGNAL_VICTIM_PICKEDUP = "VictimPickedUp";
    private static final String SIGNAL_VICTIM_DELIVERED = "VictimDelivered";

    private CorrelationKeyFactory correlationKeyFactory = KieInternalServices.Factory.get().newCorrelationKeyFactory();

    @Autowired
    private ProcessService processService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @KafkaListener(topics = "${listener.destination.mission-event}")
    public void processMessage(@Payload String messageAsJson,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition, Acknowledgment ack) {

        messageType(messageAsJson, ack).ifPresent(s -> {
            switch (s) {
                case TYPE_MISSION_STARTED_EVENT:
                    processMissionStartedEvent(messageAsJson, topic, partition, ack);
                    break;
                case TYPE_MISSION_PICKEDUP_EVENT:
                    processVictimPickedUpEvent(messageAsJson, topic, partition, ack);
                    break;
                case TYPE_MISSION_COMPLETED_EVENT:
                    processVictimDeliveredEvent(messageAsJson, topic, partition, ack);
                    break;
            }
        });
    }
