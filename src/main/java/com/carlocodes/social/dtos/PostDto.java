package com.carlocodes.social.dtos;

public class PostDto {
    private Long id;
    private String image;
    private String message;
    private Long userId;

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

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                '}';
    }
}
