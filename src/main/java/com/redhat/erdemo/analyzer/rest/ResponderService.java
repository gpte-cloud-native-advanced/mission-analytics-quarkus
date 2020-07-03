package com.redhat.erdemo.analyzer.rest;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.redhat.erdemo.responder.model.Responder;

@Path("/")
@RegisterRestClient
public interface ResponderService {

    @GET
    @Path("/responder/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response responder(@PathParam("id") long id);

}

