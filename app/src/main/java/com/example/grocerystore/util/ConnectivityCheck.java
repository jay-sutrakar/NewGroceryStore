package com.example.grocerystore.util;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;


public class ConnectivityCheck extends Application {
    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;
    private NetworkCapabilities capabilities;
    private static ConnectivityCheck instance;

    public ConnectivityCheck() {
    }

    public static ConnectivityCheck getInstance(){
        if(instance == null )
            instance = new ConnectivityCheck();
        return instance;

    }

    public boolean isConnected(Context context){
        cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if(capabilities != null)
                return true;
        }else{
            activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        return false;
    }
}
