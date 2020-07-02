package com.redhat.erdemo.response.incident.rest;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("/incidents")
public class IncidentssResource {

    @Inject
    @RestClient
    IncidentsService incidentsService;

    @GET
    @Path("/incident/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> incidentById(@PathParam("id") String incidentId){
        return incidentsService.incidentById(incidentId);
    }
}
