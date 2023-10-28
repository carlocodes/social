package com.carlocodes.social.dtos;

public class BuddyRequestDto {
    private long senderId;
    private long receiverId;

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "BuddyRequestDto{" +
                "senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
