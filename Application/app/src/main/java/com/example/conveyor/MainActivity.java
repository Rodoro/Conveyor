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
        Button RedButton = findViewById(R.id.bColorRed);
        Button GreenButton = findViewById(R.id.bColorGreen);
        Button YellowButton = findViewById(R.id.bColorYellow);
        Button BlueButton = findViewById(R.id.bColorBlue);
        if(value.charAt(0) == '1'){
            MainButton.setText("Выключить");
        } else {
            MainButton.setText("Включить");
        }
        if(value.charAt(1) == '1'){
            RedButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleredactive));
            RedButton.setText("АКТИВНО");
        } else{
            RedButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglered));
            RedButton.setText("");
        }
        if(value.charAt(2) == '1'){
            GreenButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglegreenactive));
            GreenButton.setText("АКТИВНО");
        } else{
            GreenButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectanglegreen));
            GreenButton.setText("");
        }
        if(value.charAt(3) == '1'){
            YellowButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleyellowactive));
            YellowButton.setText("АКТИВНО");
        } else{
            YellowButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleyellow));
            YellowButton.setText("");
        }
        if(value.charAt(4) == '1'){
            BlueButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblueactive));
            BlueButton.setText("АКТИВНО");
        } else{
            BlueButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangleblue));
            BlueButton.setText("");
        }
    }

    public void onClickColorRed(View view) {
        post("b");
    }

    public void onClickColorGreen(View view) {
        post("c");
    }

    public void onClickColorYellow(View view) {
        post("d");
    }

    public void onClickColorBlue(View view) {
        post("e");
    }

    public void onClickMain(View view) {
        post("a");
    }

    public void onClickSaveIp(View view){
        EditText editText = findViewById(R.id.edAddIp);
        if(editText.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "Вы не ввели IP",Toast.LENGTH_LONG);
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
        if(ip != null){
            if(ip != "") {
                EditText editText = findViewById(R.id.edAddIp);
                editText.setText(ip);
            }
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