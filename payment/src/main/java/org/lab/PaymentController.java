package org.lab;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import lombok.RequiredArgsConstructor;

@Path("/payment")
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class PaymentController {
    @Inject
    private PaymentServiceImpl service;

    @Channel("payment-requests") Emitter<String> paymentEmitter; 

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response createOrder(PaymentDto dto) {
        UUID id = UUID.randomUUID();
        Payment payment = Payment.builder()
                .paymentId(id)
                .cashierId(dto.getCashierId())
                .userId(dto.getUserId())
                .comment(dto.getComment())
                .mount(dto.getMount())
                .createdAt(LocalDateTime.now().toString())
                .build();
        service.createPayment(payment);
        paymentEmitter.send(payment.toString());
        return Response.status(Response.Status.CREATED)
                .entity(String.format("Order Created with id: %s", id)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("id") UUID id) {
        return Response.status(Response.Status.OK)
                .entity(service.getPaymentById(id)).build();
    }

}
