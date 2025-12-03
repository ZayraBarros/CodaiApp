package com.example.codaiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "UserSession";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String KEY_USER_ID = "UserId"; // NOVO: Chave para o ID
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_EMAIL = "UserEmail";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Cria a sessão de login, salvando ID, nome e email.
     */
    public void createLoginSession(long userId, String name, String email){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putLong(KEY_USER_ID, userId); // NOVO: Salvando o ID
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

    /**
     * Obtém o ID do usuário logado. Retorna -1 se não houver ID salvo.
     */
    public long getUserId(){
        // Retorna -1 como valor padrão se o ID não for encontrado (usuário não logado)
        return pref.getLong(KEY_USER_ID, -1);
    }

    public String getUserName(){
        return pref.getString(KEY_USER_NAME, "Visitante");
    }

    public String getUserEmail(){
        return pref.getString(KEY_USER_EMAIL, null);
    }
}