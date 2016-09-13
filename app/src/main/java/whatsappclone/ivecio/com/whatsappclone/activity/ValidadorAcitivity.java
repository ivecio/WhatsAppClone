package whatsappclone.ivecio.com.whatsappclone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import whatsappclone.ivecio.com.whatsappclone.R;

public class ValidadorAcitivity extends AppCompatActivity {

    private EditText codigo;
    private Button validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador_acitivity);

        codigo = (EditText) findViewById(R.id.cod_validador);
        validar = (Button) findViewById(R.id.bt_validar);


        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }
}
