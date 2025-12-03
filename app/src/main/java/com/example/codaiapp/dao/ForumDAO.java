package com.example.codaiapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.codaiapp.model.Post;
import java.util.ArrayList;
import java.util.List;

public class ForumDAO {

    private final DatabaseHelper dbHelper;

    public ForumDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Insere um novo post no fórum no banco de dados.
     */
    public boolean insertPost(Post post) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // 1. Mapeamento dos campos do modelo para as colunas da tabela
        values.put(DatabaseHelper.POST_COLUMN_TITLE, post.getTitle());
        values.put(DatabaseHelper.POST_COLUMN_CONTENT, post.getContent());
        values.put(DatabaseHelper.POST_COLUMN_AUTHOR_NAME, post.getAuthor()); // Usando o campo 'author' do modelo
        // Nota: Assumindo que você ainda não tem 'authorId' ou 'authorAvatarUrl' no seu modelo ForumPost,
        // usarei as colunas do DatabaseHelper que definimos na etapa anterior (POST_COLUMN_AUTHOR_ID e POST_COLUMN_AUTHOR_AVATAR),
        // mas as deixando como NULL ou DEFAULT no ContentValues por enquanto.
        values.put(DatabaseHelper.POST_COLUMN_DATE, post.getDate());
        values.put(DatabaseHelper.POST_COLUMN_CATEGORY, post.getCategory());
        values.put(DatabaseHelper.POST_COLUMN_REPLY_COUNT, post.getReplyCount());
        // isHighlighted não precisa ser inserido se o DEFAULT for 0 (false)

        long result = db.insert(DatabaseHelper.TABLE_FORUM_POSTS, null, values);
        db.close();

        return result != -1;
    }

    /**
     * Busca todos os posts do fórum.
     */
    public List<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String orderBy = DatabaseHelper.POST_COLUMN_ID + " DESC";

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_FORUM_POSTS,
                null,
                null,
                null,
                null,
                null,
                orderBy
        );

        if (cursor.moveToFirst()) {
            do {
                Post post = new Post();

                // Mapeamento dos campos do Cursor para o Modelo
                post.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_ID)));
                post.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_TITLE)));
                post.setContent(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_CONTENT)));
                post.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_AUTHOR_NAME)));
                post.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_DATE)));
                post.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_CATEGORY)));
                post.setReplyCount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_REPLY_COUNT)));

                // Nota: O campo 'isHighlighted' será mapeado somente se você adicionar a coluna correspondente
                // ao DatabaseHelper e ao onCreate/onUpgrade.

                postList.add(post);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return postList;
    }

    public Post getPostById(long postId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Post post = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_FORUM_POSTS +
                " WHERE " + DatabaseHelper.POST_COLUMN_ID + " = ?";

        // O método query para IDs deve ser seguro contra injeção SQL
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(postId)});

        if (cursor.moveToFirst()) {
            post = cursorToPost(cursor); // Reutiliza o método de mapeamento
        }
        cursor.close();
        db.close();
        return post;
    }
    private Post cursorToPost(Cursor cursor) {
        Post post = new Post();

        // Mapeamento das colunas
        post.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_ID)));
        post.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_TITLE)));
        post.setContent(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_CONTENT)));
        post.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_AUTHOR_NAME)));
        post.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_DATE)));
        post.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_CATEGORY)));
        post.setReplyCount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_COLUMN_REPLY_COUNT)));

        return post;
    }
    public boolean deletePost(long postId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Constantes CORRETAS para a tabela de Posts
        String whereClause = DatabaseHelper.POST_COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(postId)};

        // A exclusão na tabela forum_posts
        int rowsAffected = db.delete(
                DatabaseHelper.TABLE_FORUM_POSTS,
                whereClause,
                whereArgs
        );

        db.close();

        return rowsAffected > 0;
    }
}