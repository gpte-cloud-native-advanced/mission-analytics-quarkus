package com.redhat.erdemo.analyzer.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.util.Set;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.erdemo.analyzer.model.Incident;

@Path("/incidents")
public class IncidentResource {

    private final static Logger log = LoggerFactory.getLogger(IncidentResource.class);

    @Inject
    @RestClient
    IncidentService incidentService;

    @GET
    @Path("/incident/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Incident incidentById(@PathParam("id") String incidentId){
	if (incidentService != null) {
	    log.debug("incidentService is non-null and incidentId = "+incidentId);
	}
	return incidentService.incidentById(incidentId);
    }
}
