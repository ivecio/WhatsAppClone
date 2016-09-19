package whatsappclone.ivecio.com.whatsappclone.application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Usuario on 16/09/2016.
 */
public class ConfirguracaoFirebase {

    private static DatabaseReference firebase;

    public static Firebase getFirebase(){

        if(firebase==null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }

        return firebase;
    }

}
