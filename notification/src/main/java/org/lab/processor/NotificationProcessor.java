package org.lab.processor;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class NotificationProcessor {

    @Incoming("payment-requests")
    public void process(String request)  {
        log.info("Topics data: {}", request);
    }
}
