package com.redhat.erdemo.analyzer.consumer;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.erdemo.analyzer.model.Responder;
import com.redhat.erdemo.analyzer.rest.ResponderService;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.erdemo.responder.message.Message;
import com.redhat.erdemo.responder.message.ResponderUpdatedEvent;
import com.redhat.erdemo.responder.message.RespondersCreatedEvent;
import com.redhat.erdemo.responder.message.RespondersDeletedEvent;
import com.redhat.erdemo.responder.model.Responder;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class AnalyzerMissionEventSource {

    private final static Logger log = LoggerFactory.getLogger(AnalyzerMissionEventSource.class);

    @Inject
    AnalyzerMissionEventSource analyzerService;

    // Handles incoming Kafka events - this code will change when Knative Eventing is introduced
    @Incoming("topic-mission-event")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public CompletionStage<CompletionStage<Void>> onMessage(IncomingKafkaRecord<String, String> message) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject json = new JsonObject(message.getPayload());
                String missionId = json.getString("missionId");

                if (missionId != null) /* && "MOVING".equalsIgnoreCase(status))*/ {

                // BigDecimal lat = json.getDouble("lat") != null ? BigDecimal.valueOf(json.getDouble("lat")) : null;
                // BigDecimal lon = json.getDouble("lon") != null ? BigDecimal.valueOf(json.getDouble("lon")) : null;
                // String status = json.getString("status");

		    //Call incidentById(@PathParam("id") String incidentId) 
		    IncidentResource incidentResource;
		    Incident incident = incidentResource.incidentById(json.getString("incidentId"));
		    
		    //Call /responder/{id}
		    ResponderResource responderResource;
		    Responder responder = responderResource.responder(json.getString("responderId"));

		    Analyzer analyzer = new Analyzer.Builder(responderId).latitude(lat).longitude(lon).build();
		    // log.debug("Processing 'ResponderUpdateLocationEvent' message for responder '" + responder.getId()
                    //        + "' from topic:partition:offset " + message.getTopic() + ":" + message.getPartition()
		    //      + ":" + message.getOffset());

		    //Publish the aggregated mission event
		    publishToKafka(analyzer);
		    
                }

            } catch (Exception e) {
                log.warn("Unexpected message structure: " + message.getPayload());
            }
            return message.ack();
        });
    }


    @Outgoing("topic-mission-enhanced-event")
    private Flowable<String> publishToKafka(Analyzer analyzer) {               
        String json = "";
        try {
            json = new ObjectMapper().writeValue(analyzer);
        } catch (JsonProcessingException e) {
            log.error("Error serializing message to class Analyzer", e);
        }
        return Flowable.map(json);
    }
}
