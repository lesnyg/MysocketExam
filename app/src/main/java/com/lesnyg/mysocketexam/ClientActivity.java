package com.lesnyg.mysocketexam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {
    private String html = "";
    private Handler mHandler;
    private Socket socket;
    private BufferedReader mIn;
    private BufferedWriter mOut;
    private String ip = "172.30.1.47";
    private int port = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        try {
            setSocket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkUpdate.start();

        final EditText et_msg = findViewById(R.id.et_msg);
        final TextView tv_recevied = findViewById(R.id.tv_recevied);
        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_msg.getText().toString() != null || !et_msg.getText().toString().equals("")) {
                    PrintWriter out = new PrintWriter(mOut, true);
                    String return_msg = et_msg.getText().toString();
                    out.println(return_msg);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Thread checkUpdate = new Thread() {
        public void run() {
            try {
                String line;
                Log.w("ChattingStart", "Start Thread");
                while (true) {
                    Log.w("Chatting is running", "chatting is running");
                    line = mIn.readLine();
                    html = line;
                    mHandler.post(showUpdate);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    private Runnable showUpdate = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(ClientActivity.this, "Coming word" + html, Toast.LENGTH_SHORT).show();
        }
    };

    public void setSocket(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
            mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            mOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}