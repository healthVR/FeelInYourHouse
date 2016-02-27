package com.healthvr.h4g2016.feelinyourhouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;



public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Creamos el objeto para acceder al servicio de sensores
    private SensorManager mSensorManager;
    private double difX = 0.001;
    private double difY = 0.001;
    private double difZ = 0.001;
    private WebView myWebView,myWebView2;
    private webapi mywebapi;
    public      int acelerometro_anterior_x = 0;
    public      int acelerometro_anterior_y = 0;
    public      int acelerometro_anterior_z = 0;
    public      int giroscopio_anterior = 0;
    public      float gravedad_anterior_x = 0;
    public      float gravedad_anterior_y = 0;
    public      float gravedad_anterior_z = 0;
    private static final String LOG_TAG = "SurfaceTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //mywebapi = new webapi();
        myWebView = (WebView) this.findViewById(R.id.webView);
        myWebView.loadUrl("http://192.168.1.102:8001/?action=stream");
        myWebView.setInitialScale(500);
        myWebView2 = (WebView) this.findViewById(R.id.webView2);
        myWebView2.loadUrl("http://192.168.1.102:8001/?action=stream");
        myWebView2.setInitialScale(500);
        webapi.doPost("http://192.168.1.102:8000/macro/defaultPosition", "");
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // iniciar sensores
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
        Log.e(LOG_TAG, "onCreate ");

    }
    @Override
    // Metodo que escucha el cambio de los sensores
    public void onSensorChanged(SensorEvent event) {

// Cada sensor puede lanzar un thread que pase por aqui
// Para asegurarnos ante los accesos simult‡neos sincronizamos esto
        synchronized (this) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    int acelerometro_x = Math.round(event.values[0]);
                    int acelerometro_y = Math.round(event.values[1]);
                    int acelerometro_z = Math.round(event.values[2]);

                    if (acelerometro_x != acelerometro_anterior_x ||
                            acelerometro_y != acelerometro_anterior_y ||
                            acelerometro_z != acelerometro_anterior_z) {
                        //Log.e(LOG_TAG, "acelerometro");
                        acelerometro_anterior_x = acelerometro_x;
                        acelerometro_anterior_y = acelerometro_y;
                        acelerometro_anterior_z = acelerometro_z;
                    }
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    //int giroscopio = Math.round(event.values[0]);
                    // Creo objeto para saber como esta la pantalla
                    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                    int giroscopio = display.getRotation();
                    // El objeto devuelve 3 estados 0, 1, 2 y 3
                    // 0 = No hay rotación
                    // 1 = Horizontal izq.
                    // 2 = Vertical inv.
                    // 3 = Horizontal der.

                    // COMPROBAR LA CONDICIÓN DE ESTE IFFFFFFFFFFFFFFFFF
                    //**
                    //**
                    //**
                    //**
                    if (giroscopio != 0) {
                        //if (giroscopio != giroscopio_anterior) {
                       // Log.e(LOG_TAG, "Pos. giroscopio " + giroscopio);
                        giroscopio_anterior = giroscopio;
                    }

                    break;
                case Sensor.TYPE_GRAVITY:
                    float gravedad_x = event.values[0];
                    float gravedad_y = event.values[1];
                    float gravedad_z = event.values[2];

                    //if ((Math.abs(gravedad_x) - Math.abs(gravedad_anterior_x)) > difX)
                    if(gravedad_x != gravedad_anterior_x)
                    {
                        gravedad_anterior_x = gravedad_x;
                       // webapi.doPost("http://192.168.1.102:8001/GPIO/3/pulseAngle/"+Math.round(gravedad_x*100), "");
                        if(gravedad_x > 0)
                        {
                            webapi.doPost("http://192.168.1.102:8000/macros/up","");
                        }
                        else
                        {
                            webapi.doPost("http://192.168.1.102:8000/macros/down","");
                        }
                      /*  int scrollY = myWebView.getScrollX()+ Math.round(gravedad_x)*5;

                        myWebView.scrollBy(scrollY,0);
                        myWebView2.scrollBy(scrollY, 0);*/
                    }
                    if(gravedad_y != gravedad_anterior_y)
                    {
                        gravedad_anterior_y = gravedad_y;
                        Log.e(LOG_TAG, "Gravedad Y" + gravedad_y);
                        //Thread t = new Thread(new Runnable() { public void run() {
                         //  webapi.doPost("http://192.168.1.102:8001/GPIO/3/pulseAngle/"+Math.round(gravedad_y*100), "");
                        //}});
                        //t.start();
                        /* NBO BORRAR SOLUCION DE EMERGENCIA
                        int scrollY = myWebView.getScrollX()+ Math.round(gravedad_y*10);
                        myWebView.scrollTo(scrollY, myWebView.getScrollY());
                        myWebView2.scrollTo(scrollY, myWebView.getScrollY());
                        */
                        if(gravedad_y > 0)
                        {
                            webapi.doPost("http://192.168.1.102:8000/macros/left","");
                        }
                        else
                        {
                            webapi.doPost("http://192.168.1.102:8000/macros/right","");
                        }
                    }
                    /*if(gravedad_z != gravedad_anterior_z)
                    {
                        gravedad_anterior_z = gravedad_z;
                        Log.e(LOG_TAG, "Gravedad Z" + gravedad_z);
                    }*/
                    break;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int entero)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}