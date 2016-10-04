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

import whatsappclone.ivecio.com.whatsappclone.Adapter.ContatoAdapter;
import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.activity.ConversaActivity;
import whatsappclone.ivecio.com.whatsappclone.application.ConfiguracaoFirebase;
import whatsappclone.ivecio.com.whatsappclone.helper.Preferencias;
import whatsappclone.ivecio.com.whatsappclone.model.Contato;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerContato;


    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        //Instanciar contato na ArrayList
        contatos = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        //Montar uma ListView e adapter
        listView = (ListView) view.findViewById(R.id.lv_contatos);

//        adapter = new ArrayAdapter(
//                    getActivity(),
//                    R.layout.lista_contatos,
//                    contatos
//          );

        adapter = new ContatoAdapter( getActivity(), contatos );

        listView.setAdapter( adapter );

        //Recuperar Contatos do Firebase
        Preferencias preferencias = new Preferencias( getActivity() );
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        //Instanciando o Firebase
        databaseReference = ConfiguracaoFirebase.getFirebase()
                    .child( "contatos" )
                    .child(identificadorUsuarioLogado);

        //Colocando o conteúdo de ValueEventListener
        valueEventListenerContato = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // o objeto dataSnapshot tem todos os dados recuperados para o usuário logado

                //limpar contatos sempre que um novo é adicionado - para recuperar lista inteira novamente
                contatos.clear();

                //Listar contatos do usuário
                for (DataSnapshot dados : dataSnapshot.getChildren() ) {

                    Contato contato = dados.getValue( Contato.class );
                    contatos.add( contato );
                }
                //informa o adaptador que os dados mudaram cada vez que adiciona contato
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //adicionar evento de OnClick na ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent( getActivity(), ConversaActivity.class );

                //Recupera dados a serem passados
                Contato contato = contatos.get( position );

                //passar dados para outra Activity
                intent.putExtra( "nome", contato.getNome() );
                intent.putExtra( "email", contato.getEmail() );

                startActivity( intent );
            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        //Listener para recuperar os contatos a exibir - ele fica olhando os contatos do usuário logado
        databaseReference.addValueEventListener( valueEventListenerContato );
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerContato);
    }

}
