package com.example.pokemon;

import java.io.Serializable;
import java.util.ArrayList;

public class Card implements Serializable {
    public String category;
    public String id;
    public String illustrator;
    public String image;
    public String localId;
    public String name;
    public String rarity;
    public Set set;
    public Variants variants;
    public ArrayList<Integer> dexId;
    public int hp;
    public ArrayList<String> types;
    public String evolveFrom;
    public String stage;
    public ArrayList<Ability> abilities;
    public ArrayList<Attack> attacks;
    public ArrayList<Weakness> weaknesses;
    public Legal legal;
}

     class Ability implements Serializable{
        public String type;
        public String name;
        public String effect;
    }

     class Attack implements Serializable{
        public ArrayList<String> cost;
        public String name;
        public String effect;
        public String damage;
    }

     class CardCount implements Serializable{
        public int official;
        public int total;
    }

     class Legal implements Serializable{
        public boolean standard;
        public boolean expanded;
    }

     class Set implements Serializable{
        public CardCount cardCount;
        public String id;
        public String logo;
        public String name;
        public String symbol;
    }

     class Variants implements Serializable{
        public boolean firstEdition;
        public boolean holo;
        public boolean normal;
        public boolean reverse;
        public boolean wPromo;
    }

     class Weakness implements Serializable{
        public String type;
        public String value;
    }

