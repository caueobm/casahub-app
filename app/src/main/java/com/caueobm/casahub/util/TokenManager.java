package com.caueobm.casahub.util;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {

    private static final String PREF_NAME = "auth_pref";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // Em um app real, trate essa exceção de forma mais robusta
            // Talvez usando SharedPreferences normais como fallback (menos seguro) ou notificando o usuário
            // Por simplicidade, vamos deixar assim por enquanto.
            // sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE); // Fallback menos seguro
        }
    }

    public void saveAuthToken(String token) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_AUTH_TOKEN, token);
            editor.apply();
        }
    }

    public String getAuthToken() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
        }
        return null;
    }

    public void clearAuthToken() {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KEY_AUTH_TOKEN);
            editor.apply();
        }
    }

    public boolean isLoggedIn() {
        return getAuthToken() != null;
    }
}