package com.bitespeed.api.IdentityReconciliation.controller;

import com.bitespeed.api.IdentityReconciliation.dto.ContactRequest;
import com.bitespeed.api.IdentityReconciliation.dto.ContactResponse;
import com.bitespeed.api.IdentityReconciliation.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identify")
public class ContactController
{
    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponse> identifyContact(@RequestBody ContactRequest contactRequest) {
        String email = contactRequest.getEmail();
        String phoneNumber = contactRequest.getPhoneNumber();

        ContactResponse response = contactService.identifyContact(email, phoneNumber);
        return ResponseEntity.ok(response);
    }
}
