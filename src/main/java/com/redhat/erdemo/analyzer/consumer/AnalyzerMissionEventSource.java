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
import com.redhat.erdemo.analyzer.rest.IncidentService;
import com.redhat.erdemo.analyzer.rest.ResponderService;
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
    IncidentService incidentService;

    @Inject
    @RestClient
    ResponderService responderService;
    
    Analyzer analyzer=null;
    
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
	Incident incident = incidentService.incidentById(incidentId);
	Integer numberOfPeople = incident.getNumberOfPeople();
	
	//Call responder/{id}
	Responder responder = responderService.responder(Long.parseLong(responderId));
	String responderName = responder.getName();
	
	//Pick elements from incident and responder and build Analyzer
	analyzer = new Analyzer.Builder(missionId).incidentId(incidentId).numberOfPeople(numberOfPeople).responderId(responderId).responderName(responderName).build();

	// Convert analyzer object to JSON string
	ObjectMapper mapper = new ObjectMapper();
	String outboundPayload = "";
	try {
	    outboundPayload = mapper.writeValueAsString(analyzer);
	    log.info("ResultingJSONstring = " + outboundPayload);
	} catch (JsonProcessingException e) {
             e.printStackTrace();
	}
	// to integrate Knative eventing, setup a broker/trigger and send events
	
	return outboundPayload;
    }
    
}
