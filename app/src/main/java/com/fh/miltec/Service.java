package com.fh.miltec;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import fh.miltec.util.MensagemSocket;
import fh.miltec.util.NotificacaoServico;
import  java.lang.reflect.*;

public class Service extends android.app.Service {
    protected static final int NOTIFICATION_ID = 1007;
    private static String TAG = "Service";
    private static Service mCurrentService;
    private int counter = 0;

    private static final String NOME_PREFERENCE = "ActivityPreferenciaUsuario";

    public Service() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            restartForeground();
        }
        mCurrentService = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "restarting Service !!");
        counter = 0;

        // ele foi eliminado pelo Android e agora foi reiniciado. Devemos ter certeza de ter reiniciado tudo
        if (intent == null) {
            ProcessaVersaoAndroidServico bck = new ProcessaVersaoAndroidServico();
            bck.launchService(this);
        }

        // certifique-se de chamar startForeground em onStartCommand, caso contrário
        // quando ocultamos a notificação na tela, ela não reiniciará no Android 6 e 7
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            restartForeground();
        }

        startTimer();

        // return start sticky so if it is killed by android, it will be restarted with Intent null
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
      * inicia o processo em primeiro plano. Normalmente, isso é feito quando a tela apaga
      * ISTO É EXIGIDO NO ANDROID 8:
            * "O sistema permite que os aplicativos chamem Context.startForegroundService ()
            * mesmo quando o aplicativo está em segundo plano.
            * No entanto, o aplicativo deve chamar o método startForeground () desse serviço em cinco segundos
      * após a criação do serviço. "
    **/
    public void restartForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "restarting foreground");
            try {
                NotificacaoServico notification = new NotificacaoServico();
                startForeground(NOTIFICATION_ID, notification.setNotification(this, "Service notification", "This is the service's notification", R.drawable.ic_sleep));
                Log.i(TAG, "restarting foreground successful");
                startTimer();
            } catch (Exception e) {
                Log.e(TAG, "Error in notification " + e.getMessage());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(GlobalService.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }


    /**
     * isso é chamado quando o processo é encerrado pelo Android
     *
     * @param rootIntent
     */

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "onTaskRemoved called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(GlobalService.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        // do not call stoptimertask because on some phones it is called asynchronously
        // after you swipe out the app and therefore sometimes
        // it will stop the timer after it was restarted
        // stoptimertask();
    }


    /**
     * estático para evitar que vários temporizadores sejam criados quando o serviço é chamado várias vezes
     *
     */
    private static Timer timer;
    private static TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        Log.i(TAG, "Starting timer");

        //set a new Timer - if one is already running, cancel it to avoid two running at the same time
        stoptimertask();
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        Log.i(TAG, "Scheduling...");
        //schedule the timer, to wake up every 1 second
        //timer.schedule(timerTask, 1000, 1000);

        Configuracao conf = new Configuracao(getBaseContext());
        conf.lerPrefereciasConfiguracao();
        final int delay = conf.intervalo * 1000;

        timer.schedule(timerTask, delay, delay);
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        Log.i(TAG, "initialising TimerTask");
        timerTask = new TimerTask() {
            public  void run() {

                if(usuarioJaLogou()) {
                    enviarMensagem(String.format("%02d", getBaseContext().getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE).getInt("id", 0))
                            + "02", false);
                }
                //Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }



    private void enviarMensagem(String mensagem, Boolean feedback) {
        try {
            MensagemSocket ms = new MensagemSocket(mensagem);
            ms.enviar(getBaseContext());
            if (ms.getErro()) {
                if(feedback)
                    Log.i("in timer", "Erro ao enviar a mensagem. ");
            }
            else {
                if(feedback)
                    Log.i("in timer", "Mensagem enviada com sucesso.");
            }
        } catch (Exception e) {
            //e.printStackTrace();

        }
    }

    private boolean usuarioJaLogou()
    {
        LoginAutomatico la = new LoginAutomatico(getBaseContext());
        if(la.recuperaLoginAutomaticoPreferencia()) {
            return true;
        }
        else {
            return false;
        }

    }



    /**
     * not needed
     */
    public void stoptimertask() {
        //pare o cronômetro, se ainda não estiver nulo
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static Service getmCurrentService() {
        return mCurrentService;
    }

    public static void setmCurrentService(Service mCurrentService) {
        Service.mCurrentService = mCurrentService;
    }

}

