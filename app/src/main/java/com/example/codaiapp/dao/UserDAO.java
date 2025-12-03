package com.example.codaiapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.codaiapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {

    private final DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean registerUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_NOME, user.getNome());
        values.put(DatabaseHelper.COLUMN_EMAIL, user.getEmail());

        String hashedPassword = BCrypt.hashpw(user.getSenha(), BCrypt.gensalt());
        values.put(DatabaseHelper.COLUMN_PASSWORD, hashedPassword);

        values.put(DatabaseHelper.COLUMN_BIO, user.getBio());
        values.put(DatabaseHelper.COLUMN_ARTICLES_READ_COUNT, user.getArticlesReadCount());
        values.put(DatabaseHelper.COLUMN_FORUM_POSTS_COUNT, user.getForumPostsCount());
        values.put(DatabaseHelper.COLUMN_COMMENTS_COUNT, user.getCommentsCount());

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }

    public User login(String email, String passwordTextPlano) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User userEncontrado = null;

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                null,
                DatabaseHelper.COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            String hashDoBanco = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));

            if (BCrypt.checkpw(passwordTextPlano, hashDoBanco)) {
                userEncontrado = new User();
                userEncontrado.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                userEncontrado.setNome(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOME)));
                userEncontrado.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL)));
                userEncontrado.setBio(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIO)));
                userEncontrado.setArticlesReadCount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ARTICLES_READ_COUNT)));
                userEncontrado.setForumPostsCount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FORUM_POSTS_COUNT)));
                userEncontrado.setCommentsCount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMMENTS_COUNT)));
            }
        }

        cursor.close();
        db.close();

        return userEncontrado;
    }

    public boolean updateProfile(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_NOME, user.getNome());
        values.put(DatabaseHelper.COLUMN_BIO, user.getBio());

        String whereClause = DatabaseHelper.COLUMN_EMAIL + " = ?";
        String[] whereArgs = {user.getEmail()};

        int rowsAffected = db.update(
                DatabaseHelper.TABLE_USERS,
                values,
                whereClause,
                whereArgs
        );
        db.close();

        return rowsAffected > 0;
    }

    public User getUserDetails(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NOME,
                DatabaseHelper.COLUMN_EMAIL,
                DatabaseHelper.COLUMN_PASSWORD,
                DatabaseHelper.COLUMN_BIO,
                DatabaseHelper.COLUMN_ARTICLES_READ_COUNT,
                DatabaseHelper.COLUMN_FORUM_POSTS_COUNT,
                DatabaseHelper.COLUMN_COMMENTS_COUNT
        };

        String selection = DatabaseHelper.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null, null, null
        );

        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
            user.setNome(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL)));

            user.setBio(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIO)));
            user.setArticlesReadCount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ARTICLES_READ_COUNT)));
            user.setForumPostsCount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FORUM_POSTS_COUNT)));
            user.setCommentsCount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMMENTS_COUNT)));
        }

        cursor.close();
        db.close();
        return user;
    }

    public boolean updatePassword(String email, String novaSenha) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String novoHash = BCrypt.hashpw(novaSenha, BCrypt.gensalt());
        values.put(DatabaseHelper.COLUMN_PASSWORD, novoHash);

        String whereClause = DatabaseHelper.COLUMN_EMAIL + " = ?";
        String[] whereArgs = { email };

        int rowsAffected = db.update(
                DatabaseHelper.TABLE_USERS,
                values,
                whereClause,
                whereArgs
        );

        db.close();
        return rowsAffected > 0;
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_ID},
                DatabaseHelper.COLUMN_EMAIL + " = ?",
                new String[]{ email },
                null, null, null
        );

        boolean exists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }

    // NOVO MÉTODO ADICIONADO PARA BUSCAR O NOME POR ID
    public String getUserNameById(long userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String userName = null;

        String[] columns = {DatabaseHelper.COLUMN_NOME};
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null, null, null
        );

        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOME));
        }

        cursor.close();
        db.close();
        return userName;
    }
}