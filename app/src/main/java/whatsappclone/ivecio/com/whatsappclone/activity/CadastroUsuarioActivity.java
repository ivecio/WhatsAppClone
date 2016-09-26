package whatsappclone.ivecio.com.whatsappclone.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;

    private Usuario usuario;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome        = (EditText) findViewById(R.id.edit_cadastro_nome);
        email       = (EditText) findViewById(R.id.edit_cadastro_email);
        senha       = (EditText) findViewById(R.id.edit_cadastro_senha);
        botaoCadastrar = (Button) findViewById(R.id.bt_cadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setNome( nome.getText().toString() );
                usuario.setEmail( email.getText().toString() );
                usuario.setSenha( senha.getText().toString() );
                cadastrarUsuario();

            }
        });
    }

    private void cadastrarUsuario () {

        //pegar contexto
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha() )
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {//sucesso ao cadastrar

                            usuario.setId( task.getResult().getUser().getUid() );
                            usuario.salvar();

                            finish(); //com o finish, ele encerra a tela e volta para a anterior

                            Toast.makeText(CadastroUsuarioActivity.this, "Usuário Cadastrado", Toast.LENGTH_LONG).show();
                            Log.i("CreateUser", "Deu certo");

                        } else {
                            Toast.makeText(CadastroUsuarioActivity.this, "Usuário não cadastrado", Toast.LENGTH_LONG).show();
                            Log.i("CreateUser", "Deu errado");

                        }
                    }
                });
    }

}
