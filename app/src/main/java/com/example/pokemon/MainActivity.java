package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import okhttp3.Call;

interface CardCallback {
    void onCardReceived(Card card);
}

interface PlayerCreationCallback {
    void onPlayersCreated();
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CardCallback, PlayerCreationCallback {


    EditText searchField;
    LinearLayout searchBar;
    ImageButton searchButton;
    Button battleButton;
    Spinner searchResultSpinner;
    public static RequestQueue requestQueue;
    List<EveryCard> cards;
    EveryCard everyCard;
    Card card;
    Player player;
    Player player1;
    Player player2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGui();

        requestQueue = Volley.newRequestQueue(this);
        searchButton.setOnClickListener(this);
        battleButton.setOnClickListener(this);
        getAllCards();

    }


    private void initGui() {
        searchField = findViewById(R.id.et_search);
        searchButton = findViewById(R.id.btn_search);
        searchResultSpinner = findViewById(R.id.sp_search_result);
        searchBar = findViewById(R.id.ll_searchbar);
        battleButton = findViewById(R.id.btn_battle);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {
            //TODO add list to dropdown menu
            if (cards == null) return;

            List<EveryCard> searchList = new ArrayList<>();
            for (EveryCard pc : cards) {
                if (pc.name.toLowerCase().contains(searchField.getText().toString().toLowerCase())) {
                    searchList.add(pc);
                }
            }
            createSpinner(searchList);
        } else if (v.getId() == R.id.btn_battle) {
            createPlayers(false, () -> {
                // First player creation complete, now create the second player
                createPlayers(true, () -> {
                    // Both player creations complete, proceed with intent
                    Intent intent = new Intent(v.getContext(), BattleActivity.class);
                    intent.putExtra("player1", player1);
                    intent.putExtra("player2", player2);
                    startActivity(intent);
                });
            });
        }

    }

    private void createSpinner(List<EveryCard> pcList) {

        List<String> pcNames = pcList.stream().map(EveryCard -> EveryCard.name + ":" + EveryCard.id).
                collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, pcNames);
        searchResultSpinner.setAdapter(adapter);
        searchResultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean firstTime = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long l) {
                if (firstTime) {
                    firstTime = false;
                    return;
                }

                String id = pcNames.get(index).split(":")[1];
                Intent intent = new Intent(view.getContext(), CardActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);

                //startActivity(new Intent(view.getContext(),
                //        CardActivity.class).putExtra("id",pcNames.get(index).split("|")[1]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllCards() {
        String url = "https://api.tcgdex.net/v2/en/cards";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            cards = new Gson().fromJson(response, new TypeToken<List<EveryCard>>() {
            }.getType());
            Log.d("Cards", String.valueOf(cards.size()));
            searchBar.setVisibility(View.VISIBLE);
        }, error -> {
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        });
        requestQueue.add(request);
    }

    private void createPlayers(boolean ai, PlayerCreationCallback callback) {
        player = new Player();
        player.points = 0;

        // Counter for tracking received cards
        AtomicInteger cardsReceived = new AtomicInteger(0);

        for (int i = 0; i < 10; i++) {
            final int random = new Random().nextInt(17225);
            everyCard = cards.get(random);
            getCard(everyCard.id, card1 -> {player.cards.add(card);

                if (cardsReceived.incrementAndGet() == 10){
                    if (ai) {
                        player.ai = true;
                        player2 = player;
                    } else {
                        player.ai = false;
                        player1 = player;
                    }

                    callback.onPlayersCreated();
                }
            });
        }
    }

    public void onCardReceived(Card card){}
    public void onPlayersCreated(){}
    private void getCard(String id, CardCallback callback) {
        String url = "https://api.tcgdex.net/v2/en/cards/" + id;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            card = new Gson().fromJson(response, Card.class);
            callback.onCardReceived(card);
        }, error -> {
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        });
        MainActivity.requestQueue.add(request);
    }

}
