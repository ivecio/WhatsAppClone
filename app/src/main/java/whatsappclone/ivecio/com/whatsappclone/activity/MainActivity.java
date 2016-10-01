package whatsappclone.ivecio.com.whatsappclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import whatsappclone.ivecio.com.whatsappclone.Adapter.TabAdapter;
import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.application.ConfirguracaoFirebase;
import whatsappclone.ivecio.com.whatsappclone.helper.Base64Custom;
import whatsappclone.ivecio.com.whatsappclone.helper.Preferencias;
import whatsappclone.ivecio.com.whatsappclone.helper.SlidingTabLayout;
import whatsappclone.ivecio.com.whatsappclone.model.Contato;
import whatsappclone.ivecio.com.whatsappclone.model.Usuario;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle("WhatsAppClone");
        setSupportActionBar( toolbar );

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //configurar o Sliding Tab para ocupar toda a tela
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent2));

        //Configurar o adapter
        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager() );
        viewPager.setAdapter( tabAdapter );

        slidingTabLayout.setViewPager( viewPager );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ) {
            case R.id.action_sair :
                deslogarUsuario();
                return true;
            case R.id.action_add :
                adicionarContato();
                return true;
            case R.id.action_settings :
                //carrega configurações - fazer código
                return true;
            default:
                return super.onOptionsItemSelected(item); //implementação padrão, que retorna "False", porque o primeiro parametro do switch retorna "true"
        }
    }

    private void deslogarUsuario() {

        firebaseAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity( intent );
        finish(); //para destruir a MainActivity sem deixar em espera

    }

    private void adicionarContato () {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder( this );

        //Configurações da Dialog
        alertDialog.setTitle( "Novo Contato" );
        alertDialog.setMessage( "E-mail do usuário" );
        alertDialog.setCancelable(false);

        //Criar campo de texto
        final EditText texto = new EditText( this );
        alertDialog.setView( texto );  //usa-se o setView para desenhar componente na tela de dialog

        //Definir botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = texto.getText().toString();
                if ( emailContato.isEmpty() ) {
                    Toast.makeText( MainActivity.this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
                }else {

                    //Converter o que foi digitado para Base64
                    final String identificadorContato = Base64Custom.converterBase64( emailContato );

                    //Recuperar instância do Firebase
                    databaseReference = ConfirguracaoFirebase.getFirebase();
                    databaseReference = databaseReference.child("usuarios").child(identificadorContato);

                    //Fazer consulta única ao Firebase
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Verifica se algum dado foi retornado
                            if ( dataSnapshot.getValue() != null ) {

                                //Recuperar dados de contato a ser adicionado
                                Usuario usuarioContato = new Usuario();
                                usuarioContato = dataSnapshot.getValue( Usuario.class );

                                //Recuperar dados do usuario logado
                                Preferencias preferencias = new Preferencias( MainActivity.this );
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario( identificadorContato );
                                contato.setEmail( usuarioContato.getEmail() );
                                contato.setNome( usuarioContato.getNome() );

                                //Salvar dados no Firebase
                                databaseReference = ConfirguracaoFirebase.getFirebase()
                                                                            .child( "contatos" )
                                                                            .child( identificadorUsuarioLogado )
                                                                            .child( identificadorContato );
                                databaseReference.setValue( contato );


                            }else {
                                Toast.makeText( MainActivity.this , "Usuário não possui Cadastro", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }

}
