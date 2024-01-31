package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class BattleActivity extends AppCompatActivity implements View.OnClickListener {

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


        //extBattle.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        player1 = (Player) bundle.get("player1");
        player2 = (Player) bundle.get("player2");
        getImage();
        battleCards();


    }

    private void initGui() {
        winnerText = findViewById(R.id.tv_winner);
        aiPoints = findViewById(R.id.tv_ai_points);
        playerPoints = findViewById(R.id.tv_player_points);
    }
    @Override
    public void onClick(View v) {
        counter++;
        getImage();
        battleCards();
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
                final int random = new Random().nextInt(2)+1;
                if(random == 1){
                    winner = "Player";
                }else {
                    winner = "Ai";
                }
            }
        }
        winnerText.setText(winner);
        if(winner.equals("Ai")){
            player2.points++;
            aiPoints.setText(String.valueOf(player2.points));
        }
        else{
            player1.points++;
            playerPoints.setText(String.valueOf(player1.points));
        }
    }
}