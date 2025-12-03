package com.example.codaiapp.model;

public class Article {
    // Novo: Chave primária do banco de dados (SQLite)
    private long id;

    private String title;
    private String excerpt;
    private String content; // Novo: Conteúdo completo do artigo
    private String author;
    private String date;
    private String category;
    private int readTime;
    private int viewCount;
    private boolean isFeatured;

    // Construtor completo usado na criação do artigo (Insert no DB)
    public Article(String title, String excerpt, String content, String author, String date, String category, int readTime, int viewCount, boolean isFeatured) {
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
        this.author = author;
        this.date = date;
        this.category = category;
        this.readTime = readTime;
        this.viewCount = viewCount;
        this.isFeatured = isFeatured;
    }

    // Construtor vazio (opcional, mas útil para o DAO)
    public Article() {
    }

    // Getters e Setters para o ID (Essencial para buscas e Adapters)
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    // Getters e Setters para os demais campos
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getExcerpt() { return excerpt; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; } // Novo Setter

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDate() { return date; }
    public String getCategory() { return category; }
    public int getReadTime() { return readTime; }
    public int getViewCount() { return viewCount; }
    public boolean isFeatured() { return isFeatured; }
}