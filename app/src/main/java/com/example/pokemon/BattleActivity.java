package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class BattleActivity extends AppCompatActivity implements View.OnClickListener {

    TextView popup;
    TextView winnerText;
    TextView aiPoints;
    TextView playerPoints;
    Button nextBattle;
    Player player1;
    Player player2;
    Integer counter = 0;
    String winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        initGui();


        Bundle bundle = getIntent().getExtras();
        player1 = (Player) bundle.get("player1");
        player2 = (Player) bundle.get("player2");
        nextBattle.setOnClickListener(this);
        getImage();
        battleCards();


    }

    private void initGui() {
        winnerText = findViewById(R.id.tv_winner);
        aiPoints = findViewById(R.id.tv_ai_points);
        playerPoints = findViewById(R.id.tv_player_points);
        nextBattle = findViewById(R.id.btn_next);
    }

    @Override
    public void onClick(View v) {
        if(counter < player1.cards.size()){
            getImage();
            battleCards();
        }else {
            onButtonShowPopupWindowClick(v);
        }
    }

    private void getImage() {
        ImageView aiCardImage = findViewById(R.id.img_ai_card);
        Picasso.get().load(player2.cards.get(counter).image + "/high.jpg").into(aiCardImage);

        ImageView playerCardImage = findViewById(R.id.img_player_card);
        Picasso.get().load(player1.cards.get(counter).image + "/high.jpg").into(playerCardImage);
    }

    private void battleCards() {
        if (player1.cards.get(counter).hp == 0 && player2.cards.get(counter).hp != 0) {
            winner = "Ai";
        } else if (player2.cards.get(counter).hp == 0 && player1.cards.get(counter).hp != 0) {
            winner = "Player";
        } else {
            if (player1.cards.get(counter).hp > player2.cards.get(counter).hp) {
                winner = "Player";
            } else if (player1.cards.get(counter).hp < player2.cards.get(counter).hp) {
                winner = "Ai";
            } else {
                final int random = new Random().nextInt(2) + 1;
                if (random == 1) {
                    winner = "Player";
                } else {
                    winner = "Ai";
                }
            }
        }
        winnerText.setText(winner);
        if (winner.equals("Ai")) {
            player2.points++;
            aiPoints.setText(String.valueOf(player2.points));
        } else {
            player1.points++;
            playerPoints.setText(String.valueOf(player1.points));
        }
        counter++;
    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        popup = findViewById(R.id.tv_popup);
        popup.setText("Hello Wolrd");

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        //setWinnerText();

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void setWinnerText(){
        if(player1.points > player2.points){
            popup.setText("The winner is Player!");
        }
        else if (player1.points < player2.points){
            popup.setText("The winner is Ai!");
        }
        else{
            popup.setText("No one wins. It's a draw.");
        }
    }
}