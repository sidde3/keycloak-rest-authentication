package org.example.controller;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.example.model.AuthenticationRequestModel;
import org.example.services.DirectoryService;

@Slf4j
@Path("/v1")
public class Controller {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@QueryParam("name") String name) {
        return name !=null ? "Hello "+name : "Welcome";
    }

    @POST
    @Path("auth")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean authenticate(AuthenticationRequestModel request){
        log.info("Authentication requested [{}]",request.getUsername());
        if(request.getUsername().isEmpty() || request.getPassword().isEmpty()){
            throw new RuntimeException("user and password not received");
        }
        return DirectoryService.getInstance().isValid(request.getUsername(),request.getPassword());
    }

    @GET
    @Path("{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean isUserExists(@PathParam("username") String username){
        log.info("Validating existance of user: [{}]", username);
        return DirectoryService.getInstance().isUserExists(username);
    }
}
