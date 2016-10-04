package whatsappclone.ivecio.com.whatsappclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.model.Conversa;

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private Context context;
    private ArrayList<Conversa> conversas;
    private Conversa conversa;

    /**
     * Constructor
     *
     * @param c  The current context.
     *
     * @param objects  The objects to represent in the ListView.
     */
    public ConversaAdapter(Context c, ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.context = c;
        this.conversas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        //verifica se lista está preenchida
        if ( conversas != null ) {

            //inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);

            //montando a View
            view = inflater.inflate(R.layout.lista_conversas, parent, false);

            //recuperar elementos na tela - feitos no Layout
            TextView nome = (TextView) view.findViewById(R.id.text_nome);
            TextView ultimaMensagem = (TextView) view.findViewById(R.id.text_ultima_conversa);

            //setar valores nos componentes da tela
            conversa = conversas.get( position );
            nome.setText( conversa.getNome() );
            ultimaMensagem.setText( conversa.getMensagem() );

        }

        return view;
    }
}
