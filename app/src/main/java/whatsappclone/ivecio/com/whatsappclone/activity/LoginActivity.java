package whatsappclone.ivecio.com.whatsappclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.application.ConfiguracaoFirebase;
import whatsappclone.ivecio.com.whatsappclone.helper.Base64Custom;
import whatsappclone.ivecio.com.whatsappclone.helper.Preferencias;
import whatsappclone.ivecio.com.whatsappclone.model.Usuario;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private String idUsuarioLogado;
    private DatabaseReference databaseReference;

    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        verificaUsuarioLogado();

        email       = (EditText) findViewById(R.id.edit_login_email);
        senha       = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar  = (Button) findViewById(R.id.bt_login);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            usuario = new Usuario();
                usuario.setEmail( email.getText().toString() );
                usuario.setSenha( senha.getText().toString() );
                validarLogin();


            }
        });

    }

    public void abrirCadastroUsuario(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);

    }

    private void validarLogin () {

        firebaseAuth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha())
                .addOnCompleteListener( this , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {//sucesso ao cadastrar usuário
                            Toast.makeText( LoginActivity.this, "Usuário logado", Toast.LENGTH_LONG).show();
                            Log.i("SignIn", "Deu certo");

                            //Recupera dados do usuário
                            idUsuarioLogado = Base64Custom.converterBase64( usuario.getEmail() );

                            databaseReference = ConfiguracaoFirebase.getFirebase()
                                    .child("usuarios")
                                    .child( idUsuarioLogado );

                            // criação de um Listener que consulta uma única vez
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Usuario usuario = dataSnapshot.getValue( Usuario.class );

                                    //salvar email nas preferências
                                    String identificadorUsuarioLogado = Base64Custom.converterBase64( usuario.getEmail() );
                                    Preferencias preferencias = new Preferencias( LoginActivity.this );
                                    preferencias.salvarDados( identificadorUsuarioLogado, usuario.getNome() );

                                    abrirTelaPrincipal();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        } else {
                            Toast.makeText(LoginActivity.this, "Erro no login", Toast.LENGTH_LONG).show();
                            Log.i("SignIn", "Deu errado");
                        }
                    }
                }
        );
    }

    private void abrirTelaPrincipal () {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void verificaUsuarioLogado () {

        if ( firebaseAuth.getCurrentUser() != null ) {
            abrirTelaPrincipal();
        }
    }

}

