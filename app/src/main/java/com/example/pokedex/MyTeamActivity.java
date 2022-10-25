package com.example.pokedex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pokedex.databinding.ActivityMyTeamBinding;

import java.util.ArrayList;

public class MyTeamActivity extends AppCompatActivity {

    Context c;
    ArrayList<Pokemon> myTeam;
    LinearLayout mainLayout;
    LinearLayout subLayout;
    ArrayList<LinearLayout> children;
    EditText search;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_team);

        c = this;
        children = new ArrayList<>();
        myTeam = (ArrayList<Pokemon>)getIntent().getSerializableExtra("myTeam");
        generateData();
        setupButton();
    }

    private void setupButton()
    {
        addButton = findViewById(R.id.myTeamButton2);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyTeamActivity.this,MainActivity.class);
                i.putExtra("myTeam", myTeam);
                startActivity(i);
                finish();
            }
        });
    }

    private void generateData()
    {
        //Log.e("size", String.valueOf(pokemoni.size()));
        //progressBar.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(this);
        mainLayout = findViewById(R.id.mainLayout);

        LinearLayout pokemoniContent;
        Button removeFromTeamButton, detailsButton;
        TextView number, name, type1, type2;
        ImageView img;

        boolean color = false;
        for(final Pokemon k : myTeam)
        {
            subLayout = (LinearLayout) inflater.inflate(R.layout.pokemonlayout,mainLayout, false);
            children.add(subLayout);
            //tempChild = sublayout;

            number = subLayout.findViewById(R.id.numberText);
            name = subLayout.findViewById(R.id.nameText);
            type1 = subLayout.findViewById(R.id.type1Text);
            type2 = subLayout.findViewById(R.id.type2Text);
            img = subLayout.findViewById(R.id.imageView);
            removeFromTeamButton = subLayout.findViewById(R.id.addToTeamButton);
            detailsButton = subLayout.findViewById(R.id.detailsButton);     //change button to pokemoniContent

//            detailsButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(MainActivity.this, PokemonDetailsActivity.class);
//                    i.putExtra("index", kontakti.indexOf(k));
//                    i.putExtra("kontakti", kontakti);
//                    startActivity(i);
//                    finish();
//                }
//            });

            removeFromTeamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainLayout.removeView(children.get(myTeam.indexOf(k)));
                    children.remove(myTeam.indexOf(k));
                    myTeam.remove(k);
                }
            });
            number.setText(Integer.toString(k.getNumber()));
            name.setText(k.getName());
            type1.setText(k.getType1());
            type2.setText(k.getType2());
            int index = k.getNumber();          //?? just use the call instead of index
            String imgPath = "pokemon" + Integer.toString(index);
            int id = getResources().getIdentifier(imgPath, "drawable", MyTeamActivity.this.getPackageName());
            img.setImageResource(id);

            subLayout.setBackgroundColor(Color.WHITE);

            mainLayout.addView(subLayout);
        }
    }
}