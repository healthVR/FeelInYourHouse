package com.healthvr.h4g2016.feelinyourhouse;

import android.util.Log;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Andrés on 27/02/2016.
 */
public class webapi {

    public webapi()
    {

    }
    public static void doPost(final String requestUrl, final String urlParameters) {

        Thread t = new Thread(new Runnable() { public void run() {
            try {
                //String urlParameters = "param1=a&param2=b&param3=c";
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;
                URL url = new URL(requestUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                conn.setUseCaches(false);
                try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                    wr.write(postData);
                    Log.d("Cliente", "Datos enviados OK");
                }
            } catch (IOException ex) {
                Log.d("Cliente","Error al enviar los datos");
                Logger.getLogger(webapi.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }});
        t.start();



    }

}
