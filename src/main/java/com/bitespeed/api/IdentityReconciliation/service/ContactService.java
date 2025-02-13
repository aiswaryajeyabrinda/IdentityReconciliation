package com.bitespeed.api.IdentityReconciliation.service;

import com.bitespeed.api.IdentityReconciliation.dto.ContactResponse;
import com.bitespeed.api.IdentityReconciliation.model.Contact;
import com.bitespeed.api.IdentityReconciliation.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public ContactResponse identifyContact(String email, String phoneNumber)
    {
        //Search for existing contacts by email or phone number
        List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);

        // nwe contact
        if (contacts.isEmpty()) {
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence("primary");
            newContact.setCreatedAt(LocalDateTime.now());
            newContact.setUpdatedAt(LocalDateTime.now());

            contactRepository.save(newContact);
            return createContactResponse(newContact);
        }

        // primary contact
        Contact primaryContact = findPrimaryContact(contacts);

        //secondary contacts
        List<Contact> secondaryContacts = findSecondaryContacts(contacts, primaryContact);

        // If we have a new email or phone number similar - we need to add a new secondary contact
        if (email != null && !email.equals(primaryContact.getEmail()))
        {
            Contact newSecondaryContact = new Contact();
            newSecondaryContact.setEmail(email);
            newSecondaryContact.setPhoneNumber(phoneNumber);
            newSecondaryContact.setLinkPrecedence("secondary");
            newSecondaryContact.setLinkedContact(primaryContact);
            newSecondaryContact.setCreatedAt(LocalDateTime.now());
            newSecondaryContact.setUpdatedAt(LocalDateTime.now());

            contactRepository.save(newSecondaryContact);
            secondaryContacts.add(newSecondaryContact);
        }
        else if (phoneNumber != null && !phoneNumber.equals(primaryContact.getPhoneNumber())) {
            Contact newSecondaryContact = new Contact();
            newSecondaryContact.setPhoneNumber(phoneNumber);
            newSecondaryContact.setEmail(email);
            newSecondaryContact.setLinkPrecedence("secondary");
            newSecondaryContact.setLinkedContact(primaryContact);
            newSecondaryContact.setCreatedAt(LocalDateTime.now());
            newSecondaryContact.setUpdatedAt(LocalDateTime.now());

            contactRepository.save(newSecondaryContact);
            secondaryContacts.add(newSecondaryContact);
        }

        return createContactResponse(primaryContact, secondaryContacts);
    }

    private Contact findPrimaryContact(List<Contact> contacts)
    {
        // Looking for the primary contact among the list of contacts
        return contacts.stream()
                .filter(contact -> "primary".equals(contact.getLinkPrecedence()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No primary contact found"));
    }

    private List<Contact> findSecondaryContacts(List<Contact> contacts, Contact primaryContact) {
        // Filter out the primary contact to get the secondary contacts
        return contacts.stream()
                .filter(contact -> !contact.equals(primaryContact))
                .collect(Collectors.toList());
    }

    private ContactResponse createContactResponse(Contact primaryContact, List<Contact> secondaryContacts) {
        // Collect all emails from the primary contact and its secondary contacts
        Set<String> emails = new LinkedHashSet<>();  // Using LinkedHashSet to maintain insertion order
        emails.add(primaryContact.getEmail());

        // Add secondary emails if they are not already added (to avoid duplicates)
        secondaryContacts.forEach(contact -> emails.add(contact.getEmail()));

        // Collect all phone numbers from the primary contact and its secondary contacts
        Set<String> phoneNumbers = new LinkedHashSet<>();
        phoneNumbers.add(primaryContact.getPhoneNumber());

        // Add phone numbers from secondary contacts but avoid duplicates
        secondaryContacts.forEach(contact -> phoneNumbers.add(contact.getPhoneNumber()));

        // Convert the sets to lists to return in the response
        List<String> emailList = new ArrayList<>(emails);
        List<String> phoneNumberList = new ArrayList<>(phoneNumbers);

        // Create the response object
        ContactResponse response = new ContactResponse();
        response.setPrimaryContactId(primaryContact.getId());
        response.setEmails(emailList);
        response.setPhoneNumbers(phoneNumberList);
        response.setSecondaryContactIds(secondaryContacts.stream().map(Contact::getId).collect(Collectors.toList()));

        return response;
    }


    private ContactResponse createContactResponse(Contact primaryContact)
    {
        // Handle case for a new contact that has no secondary contacts
        return createContactResponse(primaryContact, new ArrayList<>());
    }

}
