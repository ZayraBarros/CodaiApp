package com.example.codaiapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "codaiapp.db";
    private static final int DATABASE_VERSION = 5;

    // Constantes da Tabela USERS
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_BIO = "bio";
    public static final String COLUMN_ARTICLES_READ_COUNT = "articlesReadCount";
    public static final String COLUMN_FORUM_POSTS_COUNT = "forumPostsCount";
    public static final String COLUMN_COMMENTS_COUNT = "commentsCount";

    // Constantes da Tabela ARTICLES
    public static final String TABLE_ARTICLES = "articles";
    public static final String ARTICLE_COLUMN_ID = "id";
    public static final String ARTICLE_COLUMN_TITLE = "title";
    public static final String ARTICLE_COLUMN_EXCERPT = "excerpt";
    public static final String ARTICLE_COLUMN_CONTENT = "content";
    public static final String ARTICLE_COLUMN_AUTHOR = "author";
    public static final String ARTICLE_COLUMN_DATE = "date";
    public static final String ARTICLE_COLUMN_CATEGORY = "category";
    public static final String ARTICLE_COLUMN_READ_TIME = "readTime";
    public static final String ARTICLE_COLUMN_VIEW_COUNT = "viewCount";
    public static final String ARTICLE_COLUMN_IS_FEATURED = "isFeatured";

    // Constantes da Tabela de Posts do Fórum
    public static final String TABLE_FORUM_POSTS = "forum_posts";
    public static final String POST_COLUMN_ID = "id";
    public static final String POST_COLUMN_TITLE = "title";
    public static final String POST_COLUMN_CONTENT = "content";
    public static final String POST_COLUMN_AUTHOR_NAME = "author_name";
    public static final String POST_COLUMN_DATE = "date";
    public static final String POST_COLUMN_CATEGORY = "category";
    public static final String POST_COLUMN_REPLY_COUNT = "reply_count";

    // NOVAS CONSTANTES DA TABELA DE COMENTÁRIOS (Versão 5)
    public static final String TABLE_COMMENTS = "comments";
    public static final String COMMENT_COLUMN_ID = "comment_id";
    public static final String COMMENT_COLUMN_POST_ID = "post_id";
    public static final String COMMENT_COLUMN_CONTENT = "content";
    public static final String COMMENT_COLUMN_AUTHOR = "author_name";
    public static final String COMMENT_COLUMN_DATE = "date";


    // SQL de Criação da Tabela USERS
    private static final String TABLE_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_BIO + " TEXT DEFAULT '', " +
                    COLUMN_ARTICLES_READ_COUNT + " INTEGER DEFAULT 0, " +
                    COLUMN_FORUM_POSTS_COUNT + " INTEGER DEFAULT 0, " +
                    COLUMN_COMMENTS_COUNT + " INTEGER DEFAULT 0" +
                    ");";

    // SQL de Criação da Tabela ARTICLES
    private static final String TABLE_CREATE_ARTICLES =
            "CREATE TABLE " + TABLE_ARTICLES + " (" +
                    ARTICLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ARTICLE_COLUMN_TITLE + " TEXT NOT NULL, " +
                    ARTICLE_COLUMN_EXCERPT + " TEXT, " +
                    ARTICLE_COLUMN_CONTENT + " TEXT, " +
                    ARTICLE_COLUMN_AUTHOR + " TEXT, " +
                    ARTICLE_COLUMN_DATE + " TEXT, " +
                    ARTICLE_COLUMN_CATEGORY + " TEXT, " +
                    ARTICLE_COLUMN_READ_TIME + " INTEGER DEFAULT 0, " +
                    ARTICLE_COLUMN_VIEW_COUNT + " INTEGER DEFAULT 0, " +
                    ARTICLE_COLUMN_IS_FEATURED + " INTEGER DEFAULT 0" +
                    ");";

    // SQL de Criação da Tabela FORUM_POSTS
    private static final String TABLE_CREATE_FORUM_POSTS =
            "CREATE TABLE " + TABLE_FORUM_POSTS + " (" +
                    POST_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    POST_COLUMN_TITLE + " TEXT NOT NULL, " +
                    POST_COLUMN_CONTENT + " TEXT, " +
                    POST_COLUMN_AUTHOR_NAME + " TEXT, " +
                    POST_COLUMN_DATE + " TEXT, " +
                    POST_COLUMN_CATEGORY + " TEXT, " +
                    POST_COLUMN_REPLY_COUNT + " INTEGER DEFAULT 0" +
                    ");";

    // NOVO: SQL de Criação da Tabela COMMENTS (Versão 5)
    private static final String CREATE_TABLE_COMMENTS = "CREATE TABLE " + TABLE_COMMENTS + " (" +
            COMMENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COMMENT_COLUMN_POST_ID + " INTEGER, " +
            COMMENT_COLUMN_CONTENT + " TEXT, " +
            COMMENT_COLUMN_AUTHOR + " TEXT, " +
            COMMENT_COLUMN_DATE + " TEXT, " +
            "FOREIGN KEY(" + COMMENT_COLUMN_POST_ID + ") REFERENCES " +
            TABLE_FORUM_POSTS + "(" + POST_COLUMN_ID + ") ON DELETE CASCADE" +
            ");";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USERS);
        db.execSQL(TABLE_CREATE_ARTICLES);
        db.execSQL(TABLE_CREATE_FORUM_POSTS);
        db.execSQL(CREATE_TABLE_COMMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_BIO + " TEXT DEFAULT '';");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_ARTICLES_READ_COUNT + " INTEGER DEFAULT 0;");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_FORUM_POSTS_COUNT + " INTEGER DEFAULT 0;");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_COMMENTS_COUNT + " INTEGER DEFAULT 0;");
        }

        if (oldVersion < 3) {
            db.execSQL(TABLE_CREATE_ARTICLES);
        }

        if (oldVersion < 4) {
            db.execSQL(TABLE_CREATE_FORUM_POSTS);
        }

        // NOVO: Upgrade de V4 para V5 (Adição da Tabela COMMENTS)
        if (oldVersion < 5) {
            db.execSQL(CREATE_TABLE_COMMENTS);
        }

        // Se você optar por DROP e recriar
        // if (oldVersion < newVersion) {
        //     db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        //     db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
        //     db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORUM_POSTS);
        //     db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        //     onCreate(db);
        // }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Garante que chaves estrangeiras estejam ativas
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}