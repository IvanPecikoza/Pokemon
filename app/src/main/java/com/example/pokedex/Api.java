package com.example.pokedex;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api {

    public static void getJson(String url, final ReadDataHandler rdh){

        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>(){
//            private OnTaskCompleted listener;
//
//            public void MyAsyncTask(OnTaskCompleted listener) {
//                this.listener = listener;//Assigning call back interfacethrough constructor
//            }

            @Override
            protected String doInBackground(String... strings) {
                String response = "";

                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String line;
                    while((line = br.readLine()) != null){
                        response += line + "\n";
                    }
                    br.close();
                    con.disconnect(); //this is not necessary, Android will take care of this if we don't
                } catch (Exception e) {
                    response = "[]";
                }

                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                rdh.setJson (response);
                rdh.sendEmptyMessage(0);
                //listener.OnTaskCompleted();
            }
        };

        task.execute(url);

    }

}
