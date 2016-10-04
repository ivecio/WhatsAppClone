package whatsappclone.ivecio.com.whatsappclone.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import whatsappclone.ivecio.com.whatsappclone.R;
import whatsappclone.ivecio.com.whatsappclone.helper.Preferencias;
import whatsappclone.ivecio.com.whatsappclone.model.Mensagem;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    /**
     * Constructor
     *
     * @param c  The current context.
     * @param objects  The objects to represent in the ListView.
     */
    public MensagemAdapter(Context c, ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=null;

        if ( mensagens != null) {

            //recuperar mensagens
            Mensagem mensagem = mensagens.get(position);

            //Recupera usuário logado
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioLogado = preferencias.getIdentificador();

            //Inicia objeto para montagem do Layout
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montar a View
            if (idUsuarioLogado.equals(mensagem.getIdUsuario())) {
                view = layoutInflater.inflate(R.layout.item_mensagem_direita, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }

            //Recupera elementos para exibição
            TextView textView = (TextView) view.findViewById(R.id.tv_mensagem);
            textView.setText( mensagem.getMensagem() );
        }

        return view;
    }
}
