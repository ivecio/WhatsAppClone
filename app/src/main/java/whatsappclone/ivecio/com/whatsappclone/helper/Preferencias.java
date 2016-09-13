package whatsappclone.ivecio.com.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Usuario on 11/09/2016.
 */
public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whatsapp.preferencia";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_NOME = "nome";
    private String CHAVE_TELEFONE = "telefone";
    private String CHAVE_TOKEN = "token";

    public Preferencias ( Context contextoParamento ) {

        contexto = contextoParamento;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE); // o MODE com valor "0" faz com que somente esse aplivativo tenha acesso ao valor salvo
        editor = preferences.edit();

    }

    public void salvarUsuarioPreferencias ( String nome, String telefone, String token ) {

        editor.putString(CHAVE_NOME, nome);
        editor.putString(CHAVE_TELEFONE, telefone);
        editor.putString(CHAVE_TOKEN, token);
        editor.commit();
    }

    //cria uma lista com 1. índice e 2. valor
    public HashMap<String, String> getDadosUsuario() {

        HashMap<String, String> dadosUsuario = new HashMap<>();

        dadosUsuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME, null)); //caso não tenha nada, valor va ser nulo
        dadosUsuario.put(CHAVE_TELEFONE, preferences.getString(CHAVE_TELEFONE, null));
        dadosUsuario.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN, null));

        return dadosUsuario;
    }

}
