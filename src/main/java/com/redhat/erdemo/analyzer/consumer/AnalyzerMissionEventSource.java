package com.redhat.erdemo.responder.consumer;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.erdemo.responder.model.Responder;
import com.redhat.erdemo.responder.service.ResponderService;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
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
		    IncidentsResource incidentsResource;
		    incidentsResource.incidentById(json.getString("incidentId"));
		    
		    //Call /responder/{id}
		    ResponderResource responderResource;
		    responderResource.responder(json.getString("responderId"));

		    Analyzer analyzer = new Analyzer.Builder(responderId).latitude(lat).longitude(lon).build();
		    // log.debug("Processing 'ResponderUpdateLocationEvent' message for responder '" + responder.getId()
                    //        + "' from topic:partition:offset " + message.getTopic() + ":" + message.getPartition()
		    //      + ":" + message.getOffset());
                    //responderService.updateResponderLocation(responder);

		    //Publish the aggregated mission event
		    
                }

            } catch (Exception e) {
                log.warn("Unexpected message structure: " + message.getPayload());
            }
            return message.ack();
        });
    }

}
