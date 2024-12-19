package com.example.applicationproject.View_Controller.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public class CheckingNetWork {

    public static boolean isNetworkAvailable(Context context){
        if (context == null){
            return false;
        }else{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null){
                return false;
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
                Network network = connectivityManager.getActiveNetwork();
                if (network == null){
                    return false;
                }
                NetworkCapabilities networkInfo = connectivityManager.getNetworkCapabilities(network);
                return networkInfo != null && (networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            }else{
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnectedOrConnecting();
            }
        }
    }
}
