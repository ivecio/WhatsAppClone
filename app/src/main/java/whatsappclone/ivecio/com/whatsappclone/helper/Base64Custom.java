package whatsappclone.ivecio.com.whatsappclone.helper;


import android.util.Base64;

public class Base64Custom {

    public static String converterBase64( String texto ) {

        String textoConvertido = Base64.encodeToString( texto.getBytes(), Base64.DEFAULT ); //converte texto em Base 64
        return textoConvertido.trim(); //o .trim remove espaços gerados na String
    }

    public static String decodificarBase64 ( String textoCodificado ) {

        byte [] byteDecodificado = Base64.decode( textoCodificado, Base64.DEFAULT );
        return byteDecodificado.toString();
    }

}
