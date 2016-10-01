package whatsappclone.ivecio.com.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Usuario on 11/09/2016.
 */
public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whatsapp.preferencia";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_IDENTIFICADOR = "identificandoUsuario";

    public Preferencias ( Context contextoParamento ) {

        contexto = contextoParamento;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE); // o MODE com valor "0" faz com que somente esse aplivativo tenha acesso ao valor salvo
        editor = preferences.edit();

    }

    public void salvarDados ( String identificador ) {

        editor.putString(CHAVE_IDENTIFICADOR, identificador);
        editor.commit();
    }

    public String getIdentificador() {

        return preferences.getString( CHAVE_IDENTIFICADOR, null);
    }
}
