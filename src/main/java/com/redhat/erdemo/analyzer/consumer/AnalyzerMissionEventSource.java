package com.redhat.erdemo.analyzer.consumer;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.redhat.erdemo.analyzer.model.Responder;
import com.redhat.erdemo.analyzer.model.Analyzer;
import com.redhat.erdemo.analyzer.model.Incident;
import com.redhat.erdemo.analyzer.rest.IncidentService;
import com.redhat.erdemo.analyzer.rest.ResponderService;

import io.vertx.core.json.JsonObject;
import io.reactivex.Flowable;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.erdemo.analyzer.message.Message;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/")
@Singleton
public class AnalyzerMissionEventSource {

    private final static Logger log = LoggerFactory.getLogger(AnalyzerMissionEventSource.class);
    private String outboundPayload = "";

    @Inject
    @RestClient
    IncidentService incidentService;

    @Inject
    @RestClient
    ResponderService responderService;
    
    Analyzer analyzer=null;
    
    @Produces(MediaType.APPLICATION_JSON)
    @Outgoing("mission-enhanced-event")
    @Broadcast
    private String publishEnhancedEvent() {
	log.info("Processing payload "+outboundPayload+ "\n");
	return outboundPayload;
    }


    @POST
    @Path("/")
    public Response eventingEndpoint(@Context HttpHeaders httpHeaders,
            String cloudEventJSON) {

        log.info("ce-id=" + httpHeaders.getHeaderString("ce-id"));
        log.info(
                "ce-source=" + httpHeaders.getHeaderString("ce-source"));
        log.info("ce-specversion="
                + httpHeaders.getHeaderString("ce-specversion"));
        log.info("ce-time=" + httpHeaders.getHeaderString("ce-time"));
        log.info("ce-type=" + httpHeaders.getHeaderString("ce-type"));
        log.info(
                "content-type=" + httpHeaders.getHeaderString("content-type"));
        log.info("content-length="
                + httpHeaders.getHeaderString("content-length"));


	JsonObject json = new JsonObject(cloudEventJSON);
	String messageType = json.getString("messageType");
	JsonObject jsonChildObject = (JsonObject)json.getJsonObject("body");
	    
        String missionId = jsonChildObject.getString("id");
        log.info("missionId: " + missionId + " messageType: "+ messageType);

	if (messageType.equals("MissionCompletedEvent"))
	    {
		log.info("Processing mission "+missionId+ "\n");

		String incidentId = jsonChildObject.getString("incidentId");
		String responderId = jsonChildObject.getString("responderId");

		log.info("Incident ID= "+incidentId+" ResponderId= "+responderId+ "\n");

		// Call incidentById
		Incident incident = incidentService.incidentById(incidentId);

		Integer numberOfPeople=0;
		if (incident != null)
		    numberOfPeople = incident.getNumberOfPeople();
		else
		    log.info("returned null incident\n");

		// Call responder/{id}
		Responder responder = responderService.responder(responderId);
		String responderName = responder.getName();

		log.info("numberOfPeople = "+numberOfPeople+" and responderName = "+responderName);
	
		//Pick elements from incident and responder and build Analyzer
		analyzer = new Analyzer.Builder(missionId).incidentId(incidentId).numberOfPeople(numberOfPeople).responderId(responderId).responderName(responderName).build();

		// Convert analyzer object to JSON string
		ObjectMapper mapper = new ObjectMapper();
		try {
		    outboundPayload = mapper.writeValueAsString(analyzer);
		    log.info("ResultingJSONstring = " + outboundPayload);
		} catch (JsonProcessingException e) {
		    e.printStackTrace();
		}

		log.info("outboundPayload = "+outboundPayload);
		String retVal = publishEnhancedEvent();
	    }
	
        return Response.status(Status.OK).entity("{\"hello\":\"world\"}")
                .build();
    }
    
}
