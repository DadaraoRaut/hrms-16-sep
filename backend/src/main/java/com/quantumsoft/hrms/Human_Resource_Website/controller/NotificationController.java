package com.quantumsoft.hrms.Human_Resource_Website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("*")
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendBenefitAssignedNotification(String message) {
        messagingTemplate.convertAndSend("/topic/benefitUpdates", message);
    }
}
