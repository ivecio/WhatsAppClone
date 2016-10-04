package whatsappclone.ivecio.com.whatsappclone.application;

import android.app.Application;

import com.google.firebase.FirebaseOptions;


public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseOptions.fromResource( this );
    }
}
