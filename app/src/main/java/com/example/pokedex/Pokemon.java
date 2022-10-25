package com.example.pokedex;

import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


public class Pokemon implements Serializable{
    private int number;
    private String name;
    private String type1;
    private String type2;
    private int height;
    private int weight;
//    private int hp;
//    private int attack;
//    private int defense;
//    private int specialAttack;
//    private int specialDefense;
//    private int speed;
    private static int[] stats = new int[6];
    private List<String> moves = new ArrayList<String>();
    private static String[] selectedMoves = new String[4];

    public Pokemon() {

    }

    public Pokemon(Pokemon p){
        name = p.name;
        number = p.number;
        type1 = p.type1;
        type2 = p.type2;
        moves = p.moves;
        height = p.height;
        weight = p.weight;
        stats = p.getStats();
        selectedMoves = p.getSelectedMoves();
    }

    public Pokemon(int number, String name, String type1, String type2, List<String> moves, int height, int weight, String [] selectedMoves) {
        this.number = number;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.moves = moves;
        this.weight = weight;
        this.height = height;
        Pokemon.selectedMoves = selectedMoves;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public void setHeight(int height) { this.height = height; }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void addMove(String move) { this.moves.add(move); }

    public int[] getStats() {
        return stats;
    }

    public void setSelectedMove(int index, String move) { selectedMoves[index] = move;}

    public String[] getSelectedMoves() { return selectedMoves;}

    public static Pokemon parseJSONObject(JSONObject object)
    {
        Pokemon pokemon = new Pokemon();
        try{
            if(object.has("name"))
            {
                pokemon.setName(object.getString("name"));
            }
            if (object.has("id"))
            {
                pokemon.setNumber(object.getInt("id"));
            }
            if (object.has("height"))
            {
                pokemon.setHeight(object.getInt("height"));
                //Log.e("get", String.valueOf(object.getInt("height")));

            }
            if (object.has("weight"))
            {
                pokemon.setWeight(object.getInt("weight"));
            }
            if (object.has("moves"))
            {
                JSONArray array = (JSONArray) object.get("moves");
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject move = (JSONObject) array.get(i);
                    if (move.has("move"))
                    {
                        JSONObject moveOfAMove = (JSONObject) move.get("move");
                        if (moveOfAMove.has("name"))
                        {
                            pokemon.addMove(moveOfAMove.getString("name"));
                        }
                    }
                }
            }

            if (object.has("types"))
            {
                JSONArray array = (JSONArray) object.get("types");
                JSONObject type = (JSONObject) array.get(0);
                if (type.has("type"))
                {
                    JSONObject typeOfAType = (JSONObject) type.get("type");         //that's just how they made it in the api...
                    if (typeOfAType.has("name"))
                    {
                        pokemon.setType1(typeOfAType.getString("name"));
                    }
                }
                if (array.length()>1)
                {
                    type = (JSONObject) array.get(1);
                    if (type.has("type"))
                    {
                        JSONObject typeOfAType = (JSONObject) type.get("type");
                        if (typeOfAType.has("name"))
                        {
                            pokemon.setType2(typeOfAType.getString("name"));
                        }
                    }
                }
                else
                    pokemon.setType2("");
            }
            if (object.has("stats"))
            {
                JSONArray array = (JSONArray) object.get("stats");
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject stat = (JSONObject) array.get(i);
                    if (stat.has("base_stat"))
                    {
                        pokemon.stats[i] = stat.getInt("base_stat");
                    }
                }
            }

        }catch (Exception e){
            //to do
        }
        return pokemon;
    }
}
