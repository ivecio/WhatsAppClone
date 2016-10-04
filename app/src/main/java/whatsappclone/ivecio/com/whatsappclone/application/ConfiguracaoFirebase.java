package whatsappclone.ivecio.com.whatsappclone.application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Usuario on 16/09/2016.
 */
public class ConfiguracaoFirebase {

    private static DatabaseReference firebase;

    public static DatabaseReference getFirebase(){

        if(firebase==null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }

        return firebase;
    }

}