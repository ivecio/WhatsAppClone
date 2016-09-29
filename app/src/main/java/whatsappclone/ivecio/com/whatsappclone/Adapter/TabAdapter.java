package whatsappclone.ivecio.com.whatsappclone.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import whatsappclone.ivecio.com.whatsappclone.Fragment.ContatosFragment;
import whatsappclone.ivecio.com.whatsappclone.Fragment.ConversasFragment;

public class TabAdapter extends FragmentStatePagerAdapter {


    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    private String [] tituloAbas = {"CONVERSAS", "CONTATOS"};


    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch ( position ) {
            case 0:
                fragment = new ConversasFragment();
                break;
            case 1:
                fragment = new ContatosFragment();
                break;
        }
        return fragment;
    }

    /**
     * Return the number of views available - número de abas.
     */
    @Override
    public int getCount() {
        //retorna quantidade de abas
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tituloAbas[ position ];
    }
}
