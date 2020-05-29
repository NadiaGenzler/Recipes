package com.nadia.recipes.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.nadia.recipes.R;

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
        dialog1.findViewById(R.id.tryAgainBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainActivity.recreate();
                dialog1.dismiss();

            }
        });

        dialog1.show();

    }
}
