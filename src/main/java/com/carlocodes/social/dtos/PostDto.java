package com.carlocodes.social.dtos;

import java.time.LocalDateTime;

public class PostDto {
    private Long id;
    private String image;
    private String message;
    private Long userId;
    private LocalDateTime createdDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
