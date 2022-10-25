package com.example.pokedex;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context c;
    ArrayList<Pokemon> pokemoni = new ArrayList<>();
    ArrayList<Pokemon> myTeam = new ArrayList<>();
    ArrayList<Pokemon> shownPokemoni = new ArrayList<>();
    LinearLayout mainLayout;
    LinearLayout subLayout;
    ArrayList<LinearLayout> children;
    EditText search;
    Button addButton;
    ProgressBar progressBar;
    int progressStatus = 0;
    private Handler handler = new Handler();
    public static final String KEY_CONNECTIONS = "KEY_CONNECTIONS";

    String baseUrl = "https://pokeapi.co/api/v2/pokemon/";
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        c = this;
        children = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        pokemoni = (ArrayList<Pokemon>)getIntent().getSerializableExtra("pokemoni");
        if (pokemoni != null)
        {
            myTeam = (ArrayList<Pokemon>)getIntent().getSerializableExtra("myTeam");
        }
        if(pokemoni == null)
            pokemoni = setupData();
        else if (pokemoni.isEmpty())
            pokemoni = setupData();
        else
            generateData();
        setupSearch();
        setupButton();
    }

    private void setupButton()
    {
        addButton = findViewById(R.id.myTeamButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MyTeam.class);
                i.putExtra("myTeam", myTeam);
                i.putExtra("pokemoni", pokemoni);
                startActivity(i);
                finish();
//                if (myTeam.isEmpty())
//                {
//                    Log.e("team is empty","");
//                    GetData();
//
//                }
//                if(myTeamViewActive)
//                {
//                    shownPokemoni = pokemoni;
//                    myTeamViewActive = false;
//                    //Log.e("works",pokemoni.get(8).getName());
//                }
//                else
//                {
//                    myTeamViewActive = true;
//                }
//                clearAndGenerate();

            }
        });
    }

    private void setupSearch()
    {
        search = findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shownPokemoni = new ArrayList<>();
                if(search.getText().equals(""))
                {
                    shownPokemoni = pokemoni;
                }
                else
                {
                    for(Pokemon k : pokemoni)
                    {
                        if(k.getName().contains(search.getText().toString())||
                                Integer.toString(k.getNumber()).contains(search.getText().toString())||
                                k.getType1().contains(search.getText().toString())||
                                k.getType1().contains(search.getText().toString()))
                        {
                            shownPokemoni.add(k);
                        }
                    }
                    clearAndGenerate();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private ArrayList<Pokemon> setupData()
    {
        ArrayList<Pokemon> editPokemoni = new ArrayList<>();
        final boolean[] AlreadyRan = {false};
        for (int i = 0; i < 51; i++)//651
        {
            url = baseUrl + Integer.toString(i + 1 );
            Api.getJson(url, new ReadDataHandler(){
                @Override
                public void handleMessage(Message msg) {
                    String response = getJson();
                    try {
                        JSONObject obj = new JSONObject(response);
                        editPokemoni.add(new Pokemon(Pokemon.parseJSONObject(obj)));
                        new Thread(new Runnable() {
                            public void run() {
                                while (progressStatus < 100) {
                                    progressStatus = (int) editPokemoni.size() *100/ 51;
                                    // Update the progress bar and display the
                                    //current value in the text view
                                    handler.post(new Runnable() {
                                        public void run() {
                                            progressBar.setProgress(progressStatus);
                                        }
                                    });
                                    try {
                                        // Sleep for 200 milliseconds.
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (!AlreadyRan[0])
                                {
                                    AlreadyRan[0] = true;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            generateData();
                                        }
                                    });
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }
            });
        }
        //pokemoni.addAll(editPokemoni);
        //Log.e("pokemon", String.valueOf(pokemoni.get(5).getName()));
        return editPokemoni;
    }


    private void clearAndGenerate()
    {
        for(Pokemon k : pokemoni)
            {
                if(!shownPokemoni.contains(k))
                    {
                        children.get(pokemoni.indexOf(k)).setVisibility(View.GONE);
                    }
                else
                    {
                        children.get(pokemoni.indexOf(k)).setVisibility(View.VISIBLE);
                    }
            }
    }

    private void generateData()
    {
        //Log.e("size", String.valueOf(pokemoni.size()));
        //progressBar.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(this);
        mainLayout = findViewById(R.id.mainLayout);

        LinearLayout pokemoniContent;
        Button addToTeamButton, detailsButton;
        TextView number, name, type1, type2;
        ImageView img;

        boolean color = false;
        for(final Pokemon k : pokemoni)
        {
            subLayout = (LinearLayout) inflater.inflate(R.layout.pokemonlayout,mainLayout, false);
            children.add(subLayout);
            //tempChild = sublayout;

            number = subLayout.findViewById(R.id.numberText);
            name = subLayout.findViewById(R.id.nameText);
            type1 = subLayout.findViewById(R.id.type1Text);
            type2 = subLayout.findViewById(R.id.type2Text);
            img = subLayout.findViewById(R.id.imageView);
            addToTeamButton = subLayout.findViewById(R.id.addToTeamButton);
            detailsButton = subLayout.findViewById(R.id.detailsButton);

            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.e("weight", String.valueOf(k.getWeight()));
                    Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                    i.putExtra("index", pokemoni.indexOf(k));
                    i.putExtra("pokemoniForDetails", pokemoni);
                    i.putExtra("myTeamFromAll", myTeam);
                    startActivity(i);
                    finish();
                }
            });

            addToTeamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myTeam == null)
                    {
                        myTeam.add(new Pokemon(k));
                        UpdateData();
                    }
                    else if (myTeam.size() < 6)
                    {
                        myTeam.add(k);
                        UpdateData();
                    }
                    else
                    {
                        Toast toast = Toast.makeText(c, "Your team is full", Toast.LENGTH_SHORT);
                        toast.show();
                    }


                }
            });
            number.setText(Integer.toString(k.getNumber()));
            name.setText(k.getName());
            type1.setText(k.getType1());
            type2.setText(k.getType2());
            int index = k.getNumber();          //?? just use the call instead of index
            String imgPath = "pokemon" + Integer.toString(index);
            int id = getResources().getIdentifier(imgPath, "drawable", MainActivity.this.getPackageName());
            img.setImageResource(id);

            subLayout.setBackgroundColor(Color.WHITE);

            mainLayout.addView(subLayout);

        }
        Log.e("log","pregetdataline");
        GetData();
    }

    private void UpdateData()
    {
        //Log.e("moves1", String.valueOf(myTeam.get(0).getSelectedMoves()));
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

        ArrayList<Pokemon> connections = myTeam;
        String connectionsJSONString = new Gson().toJson(connections);
        Log.e("connections?", String.valueOf(connections));
        editor.putString(KEY_CONNECTIONS, connectionsJSONString);
        editor.commit();
    }

    private void GetData()
    {
        if (myTeam == null)
        {
            String connectionsJSONString = getPreferences(MODE_PRIVATE).getString(KEY_CONNECTIONS, null);
            Type type = new TypeToken< ArrayList < Pokemon >>() {}.getType();
            ArrayList < Pokemon > connections = new Gson().fromJson(connectionsJSONString, type);
            myTeam = connections;
        }
        else if (myTeam.isEmpty())
        {
            String connectionsJSONString = getPreferences(MODE_PRIVATE).getString(KEY_CONNECTIONS, null);
            Type type = new TypeToken< ArrayList < Pokemon >>() {}.getType();
            ArrayList < Pokemon > connections = new Gson().fromJson(connectionsJSONString, type);
            myTeam = connections;
        }
        else
            UpdateData();

    }
}