package com.carlocodes.social.dtos;

import com.carlocodes.social.entities.User;

import java.time.LocalDateTime;

public class BuddyDto {
    private long id;
    private UserDto sender;
    private UserDto receiver;
    private Boolean accepted;
    private LocalDateTime createdDateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDto getSender() {
        return sender;
    }

    public void setSender(UserDto sender) {
        this.sender = sender;
    }

    public UserDto getReceiver() {
        return receiver;
    }

    public void setReceiver(UserDto receiver) {
        this.receiver = receiver;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public String toString() {
        return "BuddyDto{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", accepted=" + accepted +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
