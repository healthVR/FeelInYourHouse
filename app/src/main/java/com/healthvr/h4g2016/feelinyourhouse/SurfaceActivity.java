package com.healthvr.h4g2016.feelinyourhouse;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import com.healthvr.h4g2016.feelinyourhouse.gl.VideoTextureRenderer;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

public class SurfaceActivity extends Activity implements TextureView.SurfaceTextureListener
{
    private static final String LOG_TAG = "SurfaceTest";

    private TextureView surface;
    private MediaPlayer player;
    private VideoTextureRenderer renderer;

    private int surfaceWidth;
    private int surfaceHeight;
    private String vidAddress = "https://archive.org/download/008924/008924_512kb.mp4";
    //private String vidAddress = "http://192.168.1.100:8001/?action=stream&ignored.mjpg";

    // Creamos el objeto para acceder al servicio de sensores
  /*  private SensorManager mSensorManager;

    public      int acelerometro_anterior_x = 0;
    public      int acelerometro_anterior_y = 0;
    public      int acelerometro_anterior_z = 0;
    public      int giroscopio_anterior = 0;
    public      int gravedad_anterior_x = 0;
    public      int gravedad_anterior_y = 0;
    public      int gravedad_anterior_z = 0;*/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Quitar titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*setContentView(R.layout.activity_main);

        surface = (TextureView) findViewById(R.id.surface);
        surface.setSurfaceTextureListener(this);*/

     /*   mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

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
        Log.e(LOG_TAG, "onCreate ");*/
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (surface.isAvailable())
            startPlaying();
            Log.e(LOG_TAG, "onResume ");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (player != null)
            player.release();
        if (renderer != null)
            renderer.onPause();
        Log.e(LOG_TAG, "onPause ");
    }

    private void startPlaying()
    {
        renderer = new VideoTextureRenderer(this, surface.getSurfaceTexture(), surfaceWidth, surfaceHeight);
        player = new MediaPlayer();
        try
        {
            player.setDataSource(vidAddress);

            player.setLooping(true);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.setSurface(new Surface(renderer.getVideoTexture()));
                    renderer.setVideoSize(player.getVideoWidth(), player.getVideoHeight());
                    player.start();
                }
            });
            player.prepareAsync();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not open input video!");
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
    {
        surfaceWidth = height;
        surfaceHeight = width;
        startPlaying();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
    {
        Log.e(LOG_TAG, "onSurfaceTextureDestroyed ");
        // Método para parar la escucha de los sensores
       /* mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));*/
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

   /* @Override
    public void onAccuracyChanged(Sensor sensor, int entero)
    {
        //To change body of implemented methods use File | Settings | File Templates.
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
                        Log.e(LOG_TAG, "Pos. giroscopio " + giroscopio);
                        giroscopio_anterior = giroscopio;
                    }

                    break;
                case Sensor.TYPE_GRAVITY:
                    int gravedad_x = Math.round(event.values[0]);
                    int gravedad_y = Math.round(event.values[1]);
                    int gravedad_z = Math.round(event.values[2]);

                    if (gravedad_x != gravedad_anterior_x ||
                            gravedad_y != gravedad_anterior_y ||
                            gravedad_z != gravedad_anterior_z) {
                        //Log.e(LOG_TAG, "gravedad");
                        gravedad_anterior_x = gravedad_x;
                        gravedad_anterior_y = gravedad_y;
                        gravedad_anterior_z = gravedad_z;
                    }
                    break;
            }
        }
    }*/
}
