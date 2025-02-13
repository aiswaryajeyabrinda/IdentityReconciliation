package com.bitespeed.api.IdentityReconciliation.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Contact
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String phoneNumber;
    private String email;

    @ManyToOne
    @JoinColumn(name = "linkedId")
    private Contact linkedContact;

    private String linkPrecedence;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact getLinkedContact() {
        return linkedContact;
    }

    public void setLinkedContact(Contact linkedContact) {
        this.linkedContact = linkedContact;
    }

    public String getLinkPrecedence() {
        return linkPrecedence;
    }

    public void setLinkPrecedence(String linkPrecedence) {
        this.linkPrecedence = linkPrecedence;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

}
