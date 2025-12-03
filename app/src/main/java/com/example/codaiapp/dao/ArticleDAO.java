package com.example.codaiapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.codaiapp.model.Article;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {

    private final DatabaseHelper dbHelper;

    public ArticleDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Insere um novo artigo no banco de dados.
     */
    public boolean insertArticle(Article article) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.ARTICLE_COLUMN_TITLE, article.getTitle());
        values.put(DatabaseHelper.ARTICLE_COLUMN_EXCERPT, article.getExcerpt());
        values.put(DatabaseHelper.ARTICLE_COLUMN_CONTENT, article.getContent()); // <-- CONTEÚDO ADICIONADO
        values.put(DatabaseHelper.ARTICLE_COLUMN_AUTHOR, article.getAuthor());
        values.put(DatabaseHelper.ARTICLE_COLUMN_DATE, article.getDate());
        values.put(DatabaseHelper.ARTICLE_COLUMN_CATEGORY, article.getCategory());
        values.put(DatabaseHelper.ARTICLE_COLUMN_READ_TIME, article.getReadTime());
        values.put(DatabaseHelper.ARTICLE_COLUMN_VIEW_COUNT, article.getViewCount());
        values.put(DatabaseHelper.ARTICLE_COLUMN_IS_FEATURED, article.isFeatured() ? 1 : 0);

        long result = db.insert(DatabaseHelper.TABLE_ARTICLES, null, values);
        db.close();

        return result != -1;
    }

    /**
     * Busca todos os artigos no banco de dados para exibição na lista.
     */
    public List<Article> getAllArticles() {
        List<Article> articleList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String orderBy = DatabaseHelper.ARTICLE_COLUMN_ID + " DESC";

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_ARTICLES,
                null,
                null,
                null,
                null,
                null,
                orderBy
        );

        if (cursor.moveToFirst()) {
            do {
                boolean isFeatured = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_IS_FEATURED)) == 1;

                Article article = new Article();

                // Setando o ID (Novo)
                article.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_ID)));

                // Mapeamento dos campos
                article.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_TITLE)));
                article.setExcerpt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_EXCERPT)));
                article.setContent(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_CONTENT))); // <-- CONTEÚDO ADICIONADO
                article.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_AUTHOR)));
                // Como não temos setters para o restante, faremos a atribuição direta se necessário ou usamos um construtor,
                // mas para flexibilidade, manteremos o padrão de setters onde possível (necessário ter setters para todos os campos no Article.java).

                // Usando o construtor:
                /*
                Article article = new Article(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_EXCERPT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_CONTENT)), // Novo
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_READ_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_VIEW_COUNT)),
                        isFeatured
                );
                article.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_ID)));
                */

                articleList.add(article);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return articleList;
    }

    // NOVO MÉTODO: Obter um único artigo pelo ID
    public Article getArticleById(long articleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Article article = null;

        String selection = DatabaseHelper.ARTICLE_COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(articleId) };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_ARTICLES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            boolean isFeatured = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_IS_FEATURED)) == 1;

            article = new Article(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_EXCERPT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_CONTENT)), // Conteúdo completo
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_CATEGORY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_READ_TIME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ARTICLE_COLUMN_VIEW_COUNT)),
                    isFeatured
            );
            article.setId(articleId); // Atribui o ID
        }

        cursor.close();
        db.close();
        return article;
    }
    public boolean incrementViewCount(long articleId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Usamos db.execSQL() para executar a instrução SQL de UPDATE
        // que incrementa o valor atual da coluna.
        String sql = "UPDATE " + DatabaseHelper.TABLE_ARTICLES +
                " SET " + DatabaseHelper.ARTICLE_COLUMN_VIEW_COUNT + " = " +
                DatabaseHelper.ARTICLE_COLUMN_VIEW_COUNT + " + 1 " +
                " WHERE " + DatabaseHelper.ARTICLE_COLUMN_ID + " = ?";

        boolean success = false;
        try {
            // O segundo argumento de execSQL são os argumentos de seleção (articleId)
            db.execSQL(sql, new String[]{String.valueOf(articleId)});
            success = true;
        } catch (Exception e) {
            // Em caso de erro (ex: coluna não existe), a exceção é capturada
            e.printStackTrace();
        } finally {
            db.close();
        }
        return success;
    }
    public boolean deleteArticle(long articleId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Define a cláusula WHERE para excluir apenas o artigo com o ID especificado
        String whereClause = DatabaseHelper.ARTICLE_COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(articleId) };

        // Executa a operação DELETE
        int rowsDeleted = db.delete(
                DatabaseHelper.TABLE_ARTICLES,
                whereClause,
                whereArgs
        );

        db.close();

        // Retorna true se pelo menos uma linha foi excluída
        return rowsDeleted > 0;
    }
}