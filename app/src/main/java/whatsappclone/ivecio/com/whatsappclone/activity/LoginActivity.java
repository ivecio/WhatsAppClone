package whatsappclone.ivecio.com.whatsappclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.application.ConfirguracaoFirebase;


public class LoginActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText email;
    private EditText senha;
    private Button botaoLogar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseReference = ConfirguracaoFirebase.getFirebase();

        email       = (EditText) findViewById(R.id.edit_login_email);
        senha       = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar  = (Button) findViewById(R.id.bt_login);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    public void abrirCadastroUsuario(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }
}

