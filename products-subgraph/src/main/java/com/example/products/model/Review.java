package com.example.products.model;

public class Review {
    private String id;
    private Integer rating;
    private String comment;
    private User author;

    public Review() {}

    public Review(String id, Integer rating, String comment, User author) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.author = author;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
}
