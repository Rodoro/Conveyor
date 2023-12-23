package com.example.conveyor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        getIp();
        post("");
    }

    private void staticMachine(){
        Button MainButton = findViewById(R.id.bMain);
        Button ShurupiButton = findViewById(R.id.shurupi);
        Button VintiButton = findViewById(R.id.vint);
        Button GaykiButton = findViewById(R.id.gayki);
        Button ProvodaButton = findViewById(R.id.Provoda);
        Button Reykireyki = findViewById(R.id.reyki);
        Button Pripoypripoy = findViewById(R.id.pripoy);
        Button Gvozdiprovoda = findViewById(R.id.gvozdi);
        Button MolotkiButton = findViewById(R.id.molotki);
        Button ShesterniButton = findViewById(R.id.shesterni);
        if(value.charAt(0) == '1'){
            MainButton.setText("Заказать");
        } else {
            MainButton.setText("Подготовка");
        }
        if(value.charAt(1) == '1'){
            ShurupiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleredactive));
            ShurupiButton.setText("АКТИВНО");
        } else{
            ShurupiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglered));
            ShurupiButton.setText("");
        }
        if(value.charAt(2) == '1'){
            VintiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglegreenactive));
            VintiButton.setText("АКТИВНО");
        } else{
            VintiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglegreen));
            VintiButton.setText("");
        }
        if(value.charAt(3) == '1'){
            GaykiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleyellowactive));
            GaykiButton.setText("АКТИВНО");
        } else{
            GaykiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleyellow));
            GaykiButton.setText("");
        }
        if(value.charAt(4) == '1'){
            ProvodaButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblueactive));
            ProvodaButton.setText("АКТИВНО");
        } else{
            ProvodaButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblue));
            ProvodaButton.setText("");
        }
        if(value.charAt(5) == '1'){
            Reykireyki.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblueactive));
            Reykireyki.setText("АКТИВНО");
        } else{
            Reykireyki.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblue));
            Reykireyki.setText("");
        }
        if(value.charAt(6) == '1'){
            Pripoypripoy.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblueactive));
            Pripoypripoy.setText("АКТИВНО");
        } else{
            Pripoypripoy.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblue));
            Pripoypripoy.setText("");
        }
        if(value.charAt(7) == '1'){
            Gvozdiprovoda.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblueactive));
            Gvozdiprovoda.setText("АКТИВНО");
        } else{
            Gvozdiprovoda.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblue));
            Gvozdiprovoda.setText("");
        }
        if(value.charAt(8) == '1'){
            MolotkiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblueactive));
            MolotkiButton.setText("АКТИВНО");
        } else{
            MolotkiButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblue));
            MolotkiButton.setText("");
        }
        if(value.charAt(9) == '1'){
            ShesterniButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblueactive));
            ShesterniButton.setText("АКТИВНО");
        } else{
            ShesterniButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblue));
            ShesterniButton.setText("");
        }

    }

    public void onClickshurupi(View view) {
        post("a");
    }

    public void onClickvinti(View view) {
        post("b");
    }

    public void onClickgayki(View view) {
        post("c");
    }

    public void onClickProvoda(View view) {
        post("d");
    }

    public void onClickreyki(View view) {
        post("e");
    }
    public void onClickpripoy(View view) {
        post("f");
    }
    public void onClickgvozdi(View view) {
        post("g");
    }
    public void onClickmolotki(View view) {
        post("h");
    }
    public void onClickshesterni(View view) {
        post("i");
    }
    public void onClickMain(View view) {
        post("j");
    }

    public void onClickSaveIp(View view){
        EditText editText = findViewById(R.id.edAddIp);
        if(editText.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "Вы не ввели IP!",Toast.LENGTH_LONG);
            toast.show();
        } else {
            saveIp(editText.getText().toString());
        }
    }

    private void saveIp(String ip){
        var editor = pref.edit();
        editor.putString("ip", ip);
        editor.apply();
    }

    private void getIp(){
        var ip = pref.getString("ip", "");
        if (ip != "") {
            EditText editText = findViewById(R.id.edAddIp);
            editText.setText(ip);
        }
    }

    private void post(String post) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                EditText editText = findViewById(R.id.edAddIp);
                URL url = new URL("http://" + editText.getText().toString() + "/" + post);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    connection.connect();

                    InputStream stream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while((line = reader.readLine()) != null){
                        buffer.append(line).append("\n");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(buffer.toString());
                            value = buffer.toString();
                            staticMachine();
                        }
                    });
                } else {
                    System.out.println(0);
                }} catch(MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}