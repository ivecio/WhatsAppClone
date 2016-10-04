package whatsappclone.ivecio.com.whatsappclone.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsappclone.ivecio.com.whatsappclone.Adapter.MensagemAdapter;
import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.application.ConfiguracaoFirebase;
import whatsappclone.ivecio.com.whatsappclone.helper.Base64Custom;
import whatsappclone.ivecio.com.whatsappclone.helper.Preferencias;
import whatsappclone.ivecio.com.whatsappclone.model.Conversa;
import whatsappclone.ivecio.com.whatsappclone.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String nomeUsuarioDestinatario; //destinatário
    private String idUsuarioDestinatario;
    private String idUsuarioLogado; //remetente
    private EditText editMensagem;
    private ImageButton btMensagem;
    private DatabaseReference databaseReference;
    private ListView listView;
    private ArrayAdapter<Mensagem> arrayAdapter;
    private ArrayList<Mensagem> mensagens;
    private ValueEventListener valueEventListenerMensagem;
    private Conversa conversa;
    private String nomeUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar         = (Toolbar) findViewById(R.id.tb_conversa);
        editMensagem    = (EditText) findViewById(R.id.edit_mensagem);
        btMensagem      = (ImageButton) findViewById(R.id.bt_enviar);
        listView        = (ListView) findViewById(R.id.lv_mensagens);

        //Recuperar dados do Usuário Logado
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioLogado = preferencias.getIdentificador();
        nomeUsuarioLogado = preferencias.getNome();

        //Recuperar dados enviados na Intent
        // a Classe Bundle é utilizada para enviar dados de um Intent para outro
        Bundle extra = getIntent().getExtras();
        if ( extra != null ) {

            //Recuperar dados do contato
            nomeUsuarioDestinatario = extra.getString("nome");
            idUsuarioDestinatario = Base64Custom.converterBase64( extra.getString("email") );
        }

        //Configuração da Toolbar
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar( toolbar );

        //Montagem da ListView e Adapter
        mensagens = new ArrayList<Mensagem>();
//        arrayAdapter = new ArrayAdapter<Mensagem>(
//                ConversaActivity.this,   //Contexto
//                android.R.layout.simple_list_item_1, //layout do adapter
//                mensagens  //ArrayList para o Adapter
//        );
        arrayAdapter = new MensagemAdapter(ConversaActivity.this, mensagens);
        listView.setAdapter(arrayAdapter);

        //recuperar mensagens do Firebase
        databaseReference = ConfiguracaoFirebase.getFirebase()
                .child( "mensagens" )
                .child( idUsuarioLogado )
                .child( idUsuarioDestinatario );

        //Criar Listener para as mensagens
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar Array List de mensagens
                mensagens.clear();

                //Recuperar mensagens
                for ( DataSnapshot dados : dataSnapshot.getChildren() ) {

                    //Recupera mensagem individual
                    Mensagem mensagem = dados.getValue( Mensagem.class );

                    //Adicionar na lista mensagem
                    mensagens.add( mensagem );

                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener( valueEventListenerMensagem );

        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoMensagem = editMensagem.getText().toString();

                //Testar se mensagem está preenchida
                if ( textoMensagem.isEmpty() ) {
                    //exibe mensagem de texto vazio
                    Toast.makeText(ConversaActivity.this, "Digite a mensagem para enviar", Toast.LENGTH_LONG).show();
                }else {
                    //Salvar dados no Firebase
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario( idUsuarioLogado );
                    mensagem.setMensagem( textoMensagem );

                    //salva mensagem para o remetente
                    Boolean retornoRemetente = salvarMensagemFirebase( idUsuarioLogado, idUsuarioDestinatario, mensagem );
                    if ( !retornoRemetente ) {
                        Toast.makeText(
                                ConversaActivity.this,
                                "Problema ao enviar mensagem, tente novamente.",
                                Toast.LENGTH_LONG
                                ).show();
                    }

                    //salva mensagem para destinatário
                    Boolean retornoDestinatario = salvarMensagemFirebase( idUsuarioDestinatario, idUsuarioLogado, mensagem );
                    if ( !retornoDestinatario ) {
                        Toast.makeText(
                                ConversaActivity.this,
                                "Problema ao enviar mensagem, tente novamente.",
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    //salvar conversas

                    //remetente
                    conversa = new Conversa();
                    conversa.setIdUsuario( idUsuarioDestinatario );
                    conversa.setNome( nomeUsuarioDestinatario );
                    conversa.setMensagem( textoMensagem );
                    Boolean retornoConversaRemet = salvarConversaFirebase(idUsuarioLogado, idUsuarioDestinatario, conversa);
                    if ( !retornoConversaRemet ) {
                        Toast.makeText(
                                ConversaActivity.this,
                                "Problema ao salvar conversas, tente novamente",
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    //destinatário
                    conversa = new Conversa();
                    conversa.setIdUsuario( idUsuarioLogado );
                    conversa.setNome( nomeUsuarioLogado );
                    conversa.setMensagem( textoMensagem );
                    Boolean retornoConversaDest = salvarConversaFirebase( idUsuarioDestinatario, idUsuarioLogado, conversa);
                    if ( !retornoConversaDest ) {
                        Toast.makeText(
                                ConversaActivity.this,
                                "Problema ao salvar conversas, tente novamente",
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    //apagar texto digitado
                    editMensagem.setText("");  //deixa vazio após clicado no bt_mensagem

                }

            }
        });

    }

    private Boolean salvarMensagemFirebase (String idRemetente, String idDestinatario, Mensagem mensagem) {

        try {
            databaseReference = ConfiguracaoFirebase.getFirebase().child("mensagens");
            databaseReference.child( idRemetente )
                    .child( idDestinatario )
                    .push()          //o "push" cria uma id para as mensagens armazenadas
                    .setValue( mensagem );
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private Boolean salvarConversaFirebase (String idRemetente, String idDestinatario, Conversa conversa) {

        try {
            databaseReference = ConfiguracaoFirebase.getFirebase().child("conversas");
            databaseReference.child( idRemetente )
                    .child( idDestinatario )
                    .setValue( conversa );
            return true;

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        databaseReference.addValueEventListener( valueEventListenerMensagem );
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener( valueEventListenerMensagem );
    }
}
