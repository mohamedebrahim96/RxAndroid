package com.vacuum.app.rxandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.text);

        Observable<String> poetFirstPart = readLineFromLink55("http://developerhendy.16mb.com/p1.php");
        System.out.println("");
        Observable<String> poetLastPart = readLineFromLink55("http://developerhendy.16mb.com/p2.php");



        Observable.zip(poetFirstPart, poetLastPart, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + " .. " + s2;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                Log.e("Result ", o);
                textview.setText(o);

            }
        });


    }

    private Observable<String> readLineFromLink55(final String link) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                BufferedReader reader = null;
                try {
                    URL url = new URL(link);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    return reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        });

    }
}