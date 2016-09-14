package whatsappclone.ivecio.com.whatsappclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.helper.Permissao;
import whatsappclone.ivecio.com.whatsappclone.helper.Preferencias;



public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    public void abrirCadastroUsuario (View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity( intent );
        
    }

}


