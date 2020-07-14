package com.redhat.erdemo.analyzer.consumer;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.erdemo.analyzer.model.Responder;
import com.redhat.erdemo.analyzer.model.Analyzer;
import com.redhat.erdemo.analyzer.model.Incident;
import com.redhat.erdemo.analyzer.rest.IncidentResource;
import com.redhat.erdemo.analyzer.rest.ResponderResource;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import io.vertx.core.json.JsonObject;
import io.reactivex.Flowable;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.ws.rs.core.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.erdemo.analyzer.message.Message;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.smallrye.reactive.messaging.annotations.Broadcast;
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
    @RestClient
    IncidentResource incidentResource;

    @Inject
    @RestClient
    ResponderResource responderResource;
    
    Analyzer analyzer=null;
    String objJson = "";
    
    @Incoming("mission-event")
    @Outgoing("mission-enhanced-event")
    @Broadcast
    public String process(String payload) {

	log.info("Processing payload "+payload+ "\n");

	JsonObject json = new JsonObject(payload);
	JsonObject jsonChildObject = (JsonObject)json.getJsonObject("body");
	    
        String missionId = jsonChildObject.getString("id");

	log.info("Processing mission "+missionId+ "\n");

	String incidentId = jsonChildObject.getString("incidentId");
	String responderId = jsonChildObject.getString("responderId");

	log.info("Incident ID= "+incidentId+" ResponderId= "+responderId+ "\n");

	// Call incidentById
	Incident incident = incidentResource.incidentById(incidentId);

	//Call responder/{id}
	Responder responder = responderResource.responder(Long.parseLong(responderId));


	return payload;
    }
    
    // Handles incoming Kafka events - this code will change when Knative Eventing is introduced
    // @Incoming("topic-mission-event")
    // @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    // public CompletionStage<CompletionStage<Void>> onMessage(IncomingKafkaRecord<String, String> message) {
    //     return CompletableFuture.supplyAsync(() -> {
    //         try {
    //             JsonObject json = new JsonObject(message.getPayload());
    //             String missionId = json.getString("missionId");

    //             if (missionId != null) /* && "MOVING".equalsIgnoreCase(status))*/ {

    // 		    log.info("Processing 'topic-mission-event' for analytics service " + missionId);

    // 		    //Call incidentById(@PathParam("id") String incidentId) 
    // 		    IncidentResource incidentResource = new IncidentResource();
    // 		    Incident incident = incidentResource.incidentById(json.getString("incidentId"));

    // 		    // //Call /responder/{id}
    // 		    ResponderResource responderResource = new ResponderResource();
    // 		    Responder responder = responderResource.responder(json.getLong("responderId"));

    // 		    String incidentId = json.getString("incidentId");
    // 		    String responderId = json.getString("responderId");
    // 		    BigDecimal lat = json.getDouble("lat") != null ? BigDecimal.valueOf(json.getDouble("lat")) : null;
    // 		    BigDecimal lon = json.getDouble("lon") != null ? BigDecimal.valueOf(json.getDouble("lon")) : null;
    // 		    analyzer = new Analyzer.Builder(responderId).latitude(lat).longitude(lon).build();
    // 		    // log.debug("Processing 'ResponderUpdateLocationEvent' message for responder '" + responder.getId()
    //                 //        + "' from topic:partition:offset " + message.getTopic() + ":" + message.getPartition()
    // 		    //      + ":" + message.getOffset());

    // 		    // Knative Eventing option: write to an Eventing channel, using a REST call and the channel internally will post that event to a Knative broker or service
		    
    //             }

    //         } catch (Exception e) {
    //             log.warn("Unexpected message structure: " + message.getPayload());
    //         }
    //         return message.ack();
    //     });
    // }
    
}
