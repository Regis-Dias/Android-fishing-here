package fh.miltec.restabelecer;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.fh.miltec.GlobalService;
import com.fh.miltec.ProcessaVersaoAndroidServico;

public class RestartServiceBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = RestartServiceBroadcastReceiver.class.getSimpleName();
    private static JobScheduler jobScheduler;
    private RestartServiceBroadcastReceiver restartSensorServiceReceiver;

    /**
     * retorna o número do código da versão
     *
     * @param context
     * @return
     */
    public static long getVersionCode(Context context) {
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            long versionCode = System.currentTimeMillis();  //PackageInfoCompat.getLongVersionCode(pInfo);
            return versionCode;

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return 0;
    }



    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "about to start timer " + context.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            scheduleJob(context);
        } else {
            registerRestarterReceiver(context);
            ProcessaVersaoAndroidServico bck = new ProcessaVersaoAndroidServico();
            bck.launchService(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleJob(Context context) {
        if (jobScheduler == null) {
            jobScheduler = (JobScheduler) context
                    .getSystemService(context.JOB_SCHEDULER_SERVICE);
        }
        ComponentName componentName = new ComponentName(context,
                JobService.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                // setOverrideDeadline o executa imediatamente - você deve ter pelo menos uma restrição
                // https://stackoverflow.com/questions/51064731/firing-jobservice-without-constraints
                .setOverrideDeadline(0)
                .setPersisted(true).build();
        jobScheduler.schedule(jobInfo);
    }


    public static void reStartTracker(Context context) {
        // reinicie o serviço sem fim
        Log.i(TAG, "Restarting tracker");
        Intent broadcastIntent = new Intent(GlobalService.RESTART_INTENT);
        context.sendBroadcast(broadcastIntent);
    }


    private void registerRestarterReceiver(final Context context) {

        // o contexto pode ser nulo se o aplicativo acabou de ser instalado e é chamado de restartsensorservice
        // https://stackoverflow.com/questions/24934260/intentreceiver-components-are-not-allowed-to-register-to-receive-intents-when
        // Decisão final: no caso de ser chamado a partir da instalação da nova versão (ou seja, do manifesto, o aplicativo é
        // nulo. Portanto, devemos usar context.registerReceiver. Caso contrário, isso irá travar e tentaremos com context.getApplicationContext

        if (restartSensorServiceReceiver == null)
            restartSensorServiceReceiver = new RestartServiceBroadcastReceiver();
        else try{
            context.unregisterReceiver(restartSensorServiceReceiver);
        } catch (Exception e){
            // não registrado
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
                    context.registerReceiver(restartSensorServiceReceiver, filter);
                } catch (Exception e) {
                    try {
                        context.getApplicationContext().registerReceiver(restartSensorServiceReceiver, filter);
                    } catch (Exception ex) {

                    }
                }
            }
        }, 1000);

    }

}
