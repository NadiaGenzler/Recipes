package com.example.recipes.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.recipes.MainActivity;
import com.example.recipes.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NetworkConnection {

    public Context context;
    public Activity mainActivity;

    public NetworkConnection(Context context,Activity mainActivity){
        this.context=context;
        this.mainActivity=mainActivity;
    }

    public  boolean getInternetStatus() {

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected) {
            showNoInternetDialog();
        }
        return isConnected;
    }


    public void showNoInternetDialog(){
        final Dialog dialog1 = new Dialog(context);
        dialog1.setContentView(R.layout.no_network_layout);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.findViewById(R.id.btnSpinAndWinRedeem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainActivity.recreate();
                dialog1.dismiss();

            }
        });
        dialog1.show();
    }
}
