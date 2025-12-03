package com.example.codaiapp.model;

public class User {

    // Mudança de 'int' para 'long' para maior compatibilidade com IDs de banco de dados
    private long id;
    private String nome;
    private String email;
    private String senha;
    private String bio;
    private int articlesReadCount;
    private int forumPostsCount;
    private int commentsCount;

    public User() {
    }

    public User(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.bio = "";
        this.articlesReadCount = 0;
        this.forumPostsCount = 0;
        this.commentsCount = 0;
    }

    // Getters e Setters atualizados para 'long'
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public int getArticlesReadCount() { return articlesReadCount; }
    public void setArticlesReadCount(int articlesReadCount) { this.articlesReadCount = articlesReadCount; }

    public int getForumPostsCount() { return forumPostsCount; }
    public void setForumPostsCount(int forumPostsCount) { this.forumPostsCount = forumPostsCount; }

    public int getCommentsCount() { return commentsCount; }
    public void setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; }
}