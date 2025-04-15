package com.learning.reelnet.common.infrastructure.actuator;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuditEventListener {

    @EventListener
    public void onAuditEvent(AuditApplicationEvent event) {
        AuditEvent auditEvent = event.getAuditEvent();
        
        log.info("Audit Event: type={}, principal={}, timestamp={}, data={}",
                auditEvent.getType(),
                auditEvent.getPrincipal(),
                auditEvent.getTimestamp(),
                auditEvent.getData());
        
        // Log IP address for authentication events if available
        if (auditEvent.getType().equals("AUTHENTICATION_SUCCESS") || 
            auditEvent.getType().equals("AUTHENTICATION_FAILURE")) {
            
            Object details = auditEvent.getData().get("details");
            if (details instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails authDetails = (WebAuthenticationDetails) details;
                log.info("Authentication attempt from IP: {}", authDetails.getRemoteAddress());
            }
        }
    }
}