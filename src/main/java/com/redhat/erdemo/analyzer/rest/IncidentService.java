package com.redhat.erdemo.analyzer.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.smallrye.mutiny.Uni;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.eventbus.EventBus;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import com.redhat.erdemo.analyzer.model.Incident;

@Path("/incidents")
@RegisterRestClient
public interface IncidentService {

    @GET
    @Path("/incident/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Incident incidentById(@PathParam("id") String id);
}
