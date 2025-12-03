package com.example.codaiapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.codaiapp.model.Comment;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    private final DatabaseHelper dbHelper;

    public CommentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Insere um novo comentário no banco de dados.
     */
    public boolean insertComment(Comment comment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COMMENT_COLUMN_POST_ID, comment.getPostId());
        values.put(DatabaseHelper.COMMENT_COLUMN_CONTENT, comment.getContent());
        values.put(DatabaseHelper.COMMENT_COLUMN_AUTHOR, comment.getAuthor());
        values.put(DatabaseHelper.COMMENT_COLUMN_DATE, comment.getDate());

        long result = db.insert(DatabaseHelper.TABLE_COMMENTS, null, values);
        db.close();

        return result != -1;
    }

    /**
     * Busca todos os comentários para uma postagem específica, ordenados por data.
     */
    public List<Comment> getCommentsByPostId(long postId) {
        List<Comment> commentList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseHelper.COMMENT_COLUMN_POST_ID + " = ?";
        String[] selectionArgs = {String.valueOf(postId)};
        String orderBy = DatabaseHelper.COMMENT_COLUMN_ID + " ASC"; // Ordem cronológica

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_COMMENTS,
                null, // Todas as colunas
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );

        if (cursor.moveToFirst()) {
            do {
                commentList.add(cursorToComment(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return commentList;
    }

    /**
     * Exclui todos os comentários associados a um post específico.
     * Retorna o número de linhas (comentários) excluídas.
     */
    public int deleteCommentsByPostId(long postId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Define a cláusula WHERE para encontrar todos os comentários com o post_id
        String whereClause = DatabaseHelper.COMMENT_COLUMN_POST_ID + " = ?";
        String[] whereArgs = {String.valueOf(postId)};

        // Realiza a exclusão e retorna o número de linhas afetadas
        int rowsAffected = db.delete(
                DatabaseHelper.TABLE_COMMENTS,
                whereClause,
                whereArgs
        );

        db.close();

        return rowsAffected;
    }

    /**
     * Converte um Cursor do SQLite para um objeto Comment.
     */
    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();

        comment.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COMMENT_COLUMN_ID)));
        comment.setPostId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COMMENT_COLUMN_POST_ID)));
        comment.setContent(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COMMENT_COLUMN_CONTENT)));
        comment.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COMMENT_COLUMN_AUTHOR)));
        comment.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COMMENT_COLUMN_DATE)));

        return comment;
    }
}