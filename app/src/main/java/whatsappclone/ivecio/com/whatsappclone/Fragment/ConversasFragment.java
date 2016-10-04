package whatsappclone.ivecio.com.whatsappclone.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsappclone.ivecio.com.whatsappclone.Adapter.ConversaAdapter;
import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.activity.ConversaActivity;
import whatsappclone.ivecio.com.whatsappclone.application.ConfiguracaoFirebase;
import whatsappclone.ivecio.com.whatsappclone.helper.Base64Custom;
import whatsappclone.ivecio.com.whatsappclone.helper.Preferencias;
import whatsappclone.ivecio.com.whatsappclone.model.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Conversa> arrayAdapter;
    private ArrayList<Conversa> conversas;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerConversas;

    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        conversas = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //montar ListView e Adapter
        listView = (ListView) view.findViewById(R.id.lv_conversas);
        arrayAdapter = new ConversaAdapter( getActivity(), conversas);
        listView.setAdapter( arrayAdapter );

        //Recuperar conversas no Firebase
        Preferencias preferencias = new Preferencias( getActivity() );
        String idUsuarioLogado = preferencias.getIdentificador();

        //Instância Firebase
        databaseReference = ConfiguracaoFirebase.getFirebase()
                .child("conversas")
                .child( idUsuarioLogado );

        //consulta no Firebase
        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar as conversas sempre que for chamado o método
                conversas.clear();

                for ( DataSnapshot dados : dataSnapshot.getChildren() ) {

                    Conversa conversa = dados.getValue( Conversa.class );
                    conversas.add ( conversa );

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //adicionando o Listener do Firebase
        databaseReference.addValueEventListener(valueEventListenerConversas);

        //adicionar evento de click na lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //recuperar conversa para a posição
                Conversa conversa = conversas.get( position );

                //Criar intent pára o ConversaActivity - igual no ContatosActivity
                Intent intent = new Intent( getActivity(), ConversaActivity.class);
                String email = Base64Custom.decodificarBase64( conversa.getIdUsuario() );
                intent.putExtra("email", email);
                intent.putExtra("nome", conversa.getNome() );
                startActivity( intent );

            }
        });

        return view;
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerConversas);
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerConversas);
    }
}
