package com.example.codaiapp.model;

public class Post {
    // CHAVE PRIMÁRIA
    private long id;

    private String title;
    private String content;
    private String author; // Mudando para authorName para simplificar a migração
    private String date;
    private String category;

    // Mudando de commentCount para replyCount, conforme padrão de fóruns
    private int replyCount;
    private boolean isHighlighted;

    // Construtor completo (sem ID, pois ele é gerado pelo DB)
    public Post(String title, String content, String author, String date, String category, int replyCount) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.category = category;
        this.replyCount = replyCount;
        this.isHighlighted = false;
    }

    // Construtor vazio (crucial para o DAO)
    public Post() {
    }

    // Getters e Setters para o ID
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    // Getters e Setters para os demais campos (necessários para o DAO)
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getReplyCount() { return replyCount; }
    public void setReplyCount(int replyCount) { this.replyCount = replyCount; }

    public boolean isHighlighted() { return isHighlighted; }
    public void setHighlighted(boolean highlighted) { isHighlighted = highlighted; }
}