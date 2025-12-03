package com.example.codaiapp.model;

public class Comment {

    private long id;
    private long postId; // Chave estrangeira para o Post ao qual pertence
    private String content;
    private String author; // Nome do autor do comentário
    private String date; // Data ou hora de criação

    // Construtor vazio
    public Comment() {
    }

    // Construtor completo
    public Comment(long postId, String content, String author, String date) {
        this.postId = postId;
        this.content = content;
        this.author = author;
        this.date = date;
    }

    // Getters
    public long getId() {
        return id;
    }

    public long getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }
}