package com.carlocodes.social.dtos;

public class BuddyRequestDto {
    private Long senderId;
    private Long receiverId;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
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
