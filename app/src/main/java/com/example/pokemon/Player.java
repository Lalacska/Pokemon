package com.example.pokemon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    public List<Card> cards;
    public boolean ai;
    public Integer points;

    public Player() {
        cards = new ArrayList<>(); // Initialize the cards list
    }
}
