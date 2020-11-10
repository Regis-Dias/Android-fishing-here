package com.fh.miltec;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ProcessaVersaoAndroidServico {
    public static final String TAG = ProcessaVersaoAndroidServico.class.getSimpleName();
    private static Intent serviceIntent = null;

    public ProcessaVersaoAndroidServico() {
    }


    private void setServiceIntent(Context context) {
        if (serviceIntent == null) {
            serviceIntent = new Intent(context, Service.class);
        }
    }
    /**
     * lançando o serviço
     */
    public void launchService(Context context) {
        if (context == null) {
            return;
        }
        setServiceIntent(context);
        // dependendo da versão do Android, podemos lançar o serviço simples (versão <O)
        // ou começamos um serviço de primeiro plano
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
        Log.d(TAG, "ProcessMainClass: start service go!!!!");
    }
}
