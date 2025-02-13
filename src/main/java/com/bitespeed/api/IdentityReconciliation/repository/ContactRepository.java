package com.bitespeed.api.IdentityReconciliation.repository;

import com.bitespeed.api.IdentityReconciliation.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
