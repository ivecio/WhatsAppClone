package whatsappclone.ivecio.com.whatsappclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.model.Contato;

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private Context context; //esse é outro, não é o de dentro do construtor
    private ArrayList<Contato> contatos;


    /**
     * Constructor
     *
     * @param c  The current context.
     * @param -- The resource ID for a layout file containing a TextView to use when
     *                 instantiating views. - retirado o resource para esse adaptador
     * @param objects  The objects to represent in the ListView.
     */
    public ContatoAdapter(Context c, ArrayList<Contato> objects) {
        super(c, 0 , objects);
        this.context = c;
        this.contatos = objects;
    }

    //método getView é chamado para implementação do método View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        //verifica se lista está preenchida
        if ( contatos != null ) {

            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( context.LAYOUT_INFLATER_SERVICE );

            //montar a View a partir do arquivo .xml
            view = inflater.inflate( R.layout.lista_contatos, parent, false );

            //recuperar elementos para visualização
            TextView textView = (TextView) view.findViewById(R.id.tv_nome);

            Contato contato = contatos.get( position );
            textView.setText( contato.getNome() );
        }

        return view;
    }
}
