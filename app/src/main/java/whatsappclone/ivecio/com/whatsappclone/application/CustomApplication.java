package whatsappclone.ivecio.com.whatsappclone.application;

import android.app.Application;

import com.google.firebase.FirebaseOptions;

/**
 * Created by Usuario on 16/09/2016.
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseOptions.fromResource( this );
    }
}
