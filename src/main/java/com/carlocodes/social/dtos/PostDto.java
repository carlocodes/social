package com.carlocodes.social.dtos;

import java.time.LocalDateTime;

public class PostDto {
    private long id;
    private String image;
    private String message;
    private long userId;
    private LocalDateTime createdDateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
