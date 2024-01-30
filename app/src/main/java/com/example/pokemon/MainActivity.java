package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView text;
    EditText searchField;
    ImageButton searchButton;
    RequestQueue requestQueue;
    List<PokemonCard> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGui();

        requestQueue = Volley.newRequestQueue(this);
        searchButton.setOnClickListener(this);
        getAllCards();

    }

    private void initGui(){
        searchField = findViewById(R.id.et_search);
        searchButton = findViewById(R.id.btn_search);
        text = findViewById(R.id.tv_test);
    }

    @Override
    public void onClick(View v) {
        //TODO call api with searchtext
    }

    private void getAllCards(){
        String url = "https://api.tcgdex.net/v2/en/cards";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            cards = new Gson().fromJson(response, new TypeToken<List<PokemonCard>>(){}.getType());
            Log.d("Cards", String.valueOf(cards.size()));
            String str = "";
            for(PokemonCard pc: cards){
                str += pc.name + "\n";
            }
            text.setText(str);
            }, error ->{
            Toast.makeText(this, error.toString(),Toast.LENGTH_LONG).show();
        });
        requestQueue.add(request);
    }
}