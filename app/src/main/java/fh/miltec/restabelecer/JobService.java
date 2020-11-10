package fh.miltec.restabelecer;

import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.fh.miltec.GlobalService;
import com.fh.miltec.ProcessaVersaoAndroidServico;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobService extends android.app.job.JobService {
    private static String TAG= JobService.class.getSimpleName();
    private static RestartServiceBroadcastReceiver restartSensorServiceReceiver;
    private static JobService instance;
    private static JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        ProcessaVersaoAndroidServico bck = new ProcessaVersaoAndroidServico();
        bck.launchService(this);
        registerRestarterReceiver();
        instance= this;
        JobService.jobParameters= jobParameters;

        return false;
    }

    private void registerRestarterReceiver() {

        // o contexto pode ser nulo se o aplicativo acabou de ser instalado e é chamado de restartsensorservice
        // https://stackoverflow.com/questions/24934260/intentreceiver-components-are-not-allowed-to-register-to-receive-intents-when
        // Decisão final: no caso de ser chamado a partir da instalação da nova versão (ou seja, do manifesto, o aplicativo é
        // nulo. Portanto, devemos usar context.registerReceiver. Caso contrário, isso irá travar e tentaremos com context.getApplicationContext

        if (restartSensorServiceReceiver == null)
            restartSensorServiceReceiver = new RestartServiceBroadcastReceiver();
        else try{
            unregisterReceiver(restartSensorServiceReceiver);
        } catch (Exception e){

            //não registrado
        }
        // dê tempo para correr
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // registramos o receptor que irá reiniciar o serviço de segundo plano se for eliminado
                // veja onDestroy of Service
                IntentFilter filter = new IntentFilter();
                filter.addAction(GlobalService.RESTART_INTENT);
                try {
                    registerReceiver(restartSensorServiceReceiver, filter);
                } catch (Exception e) {
                    try {
                        getApplicationContext().registerReceiver(restartSensorServiceReceiver, filter);
                    } catch (Exception ex) {

                    }
                }
            }
        }, 1000);

    }

    /**
     * chamado se o Android interromper o serviço de trabalho
     * @param jobParameters
     * @return
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG, "Stopping job");
        Intent broadcastIntent = new Intent(GlobalService.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        // dê tempo para correr
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                unregisterReceiver(restartSensorServiceReceiver);
            }
        }, 1000);

        return false;
    }


    /**
     * chamado quando o rastreador é interrompido por qualquer motivo
     * @param context
     */
    public static void stopJob(Context context) {
        if (instance!=null && jobParameters!=null) {
            try{
                instance.unregisterReceiver(restartSensorServiceReceiver);
            } catch (Exception e){
               //não registrado
            }
            Log.i(TAG, "Finishing job");
            instance.jobFinished(jobParameters, true);
        }
    }
}
