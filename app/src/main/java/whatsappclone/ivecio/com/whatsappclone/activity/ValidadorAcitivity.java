package whatsappclone.ivecio.com.whatsappclone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.helper.Preferencias;

public class ValidadorAcitivity extends AppCompatActivity {

    private EditText codigoValidacao;
    private Button validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador_acitivity);

        codigoValidacao = (EditText) findViewById(R.id.cod_validador);
        validar = (Button) findViewById(R.id.bt_validar);

        SimpleMaskFormatter simpleMaskCodigoValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigoValidacao = new MaskTextWatcher(codigoValidacao, simpleMaskCodigoValidacao);

        codigoValidacao.addTextChangedListener( mascaraCodigoValidacao );


        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Preferencias preferencias = new Preferencias( ValidadorAcitivity.this );
                HashMap<String, String> usuario = preferencias.getDadosUsuario();

                String tokenGerado = usuario.get( "token" );
                String tokenDigitado = codigoValidacao.getText().toString();

                if ( tokenDigitado.equals( tokenGerado) ) {
                    Toast.makeText(ValidadorAcitivity.this, "Token VALIDADO", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ValidadorAcitivity.this, "Token NÃO VALIDADO", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
