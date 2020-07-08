package com.redhat.erdemo.analyzer.rest;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.core.Response;


@Path("/incidents")
public class IncidentResource {

    @Inject
    @RestClient
    IncidentService incidentsService;

    @GET
    @Path("/incident/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> incidentById(@PathParam("id") String incidentId){
        return incidentsService.incidentById(incidentId);
    }
}
