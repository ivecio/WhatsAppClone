package whatsappclone.ivecio.com.whatsappclone.activity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

import android.Manifest;
import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.helper.Permissao;
import whatsappclone.ivecio.com.whatsappclone.helper.Preferencias;



public class LoginActivity extends AppCompatActivity {

    private EditText telefone;
    private EditText ddd;
    private EditText ddi;
    private EditText nome;
    private Button cadastrar;
    private String[] permNecessarias = new String[] {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //o "1" é somente para dizer de onde vem o pedido de permissão
        Permissao.validaPermissoes( 1, this, permNecessarias );

        telefone    = (EditText) findViewById(R.id.edit_telefone);
        ddd         = (EditText) findViewById(R.id.cod_ddd);
        ddi         = (EditText) findViewById(R.id.cod_ddi);
        nome        = (EditText) findViewById(R.id.editNome);
        cadastrar   = (Button) findViewById(R.id.bt_cadastrar);

        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNN-NNNN"); //define a máscara - estrutura
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskTelefone); //define a máscara como estilo
        telefone.addTextChangedListener( maskTelefone ); //define onde vai ser usada a máscara

        SimpleMaskFormatter simpleMaskDDD = new SimpleMaskFormatter(" (NN) ");
        MaskTextWatcher maskDDD = new MaskTextWatcher(ddd, simpleMaskDDD);
        ddd.addTextChangedListener( maskDDD );

        SimpleMaskFormatter simpleMaskDDI = new SimpleMaskFormatter(" +NN ");
        MaskTextWatcher maskDDI = new MaskTextWatcher(ddi, simpleMaskDDI);
        ddi.addTextChangedListener( maskDDI );

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto =
                        ddi.getText().toString() +
                        ddd.getText().toString() +
                        telefone.getText().toString();

                //aqui o telefone é armazenado perdendo todos caraterezes especiais
                String telefoneSemFormatacao = telefoneCompleto.replace("+", "");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("-", "");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("(", "");
                telefoneSemFormatacao = telefoneSemFormatacao.replace(")", "");

                //gerando token randomico
                Random randomico = new Random();
                int numeroRandomico = randomico.nextInt( 9999 - 1000) + 1000; //adiciona 1000 para sempre ter 4 numeros
                String token = String.valueOf(numeroRandomico);
                String mensagemEnvio = "WhatsApp Clone Código de Validacao: " + token;

                //salvar dados para validação - usando clase java externa
                Preferencias preferencias = new Preferencias( getApplicationContext() ); //poderia usar LoginActivity.this
                preferencias.salvarUsuarioPreferencias(nomeUsuario, telefoneSemFormatacao, token);

                //Envio de SMS - aqui volta a colocar o "+" na frente porque precisa para envio do SMS
                boolean enviadoSMS = enviaSMS( "+" + telefoneSemFormatacao, mensagemEnvio );


                /*HashMap<String, String> usuario = preferencias.getDadosUsuario();*/

            }
        });

    }

    //Envio do SMS - capta o telefone e a mensagem para mandar
    private boolean enviaSMS (String telefone, String mensagem) {

        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);

            return true;

        }catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }

    public void onRequestPermissionResult ( int requestCode, String [] permissions, int[] grantResults ) {

        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        for ( int resultado : grantResults ) {

            if ( resultado == PackageManager.PERMISSION_DENIED ) {
                alertaValidacaoPermissao();
            }

        }

    }

    private void alertaValidacaoPermissao () {

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse aplicativo, é necessário aceitar as permissões");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
