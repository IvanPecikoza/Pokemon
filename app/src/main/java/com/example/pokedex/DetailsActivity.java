package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements CustomInteraction{

    ArrayList<Pokemon> pokemoni;
    ArrayList<Pokemon> sviPokemoni;
    ArrayList<Pokemon> myTeamFromAll;
    Pokemon pokemon;
    String typesText = "";
    private ImageView img;
    private TextView id;
    private TextView name;
    private TextView types;
    private TextView height;
    private TextView weight;
    private TextView hp;
    private TextView attack;
    private TextView defense;
    private TextView specialAttack;
    private TextView specialDefense;
    private TextView speed;
    private Button button;
    Boolean toggle = false;             //false == myteam view///// true == all pokemon view
    int index;
    int moveIndex;
    int selectedMoveIndex;
    boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initComponents();
        setupButton();
        update = false;
        Bundle extras = getIntent().getExtras();
//        moveIndex = Integer.parseInt(null);
//        selectedMoveIndex = Integer.parseInt(null);
        pokemoni = (ArrayList<Pokemon>) getIntent().getSerializableExtra("pokemoniForDetails");
        sviPokemoni = (ArrayList<Pokemon>) getIntent().getSerializableExtra("allPokemoni");
        myTeamFromAll = (ArrayList<Pokemon>) getIntent().getSerializableExtra("myTeamFromAll");
        index = extras.getInt("index");
        if (sviPokemoni == null)
        {
            toggle = true;
        }

        String idText = "number: " + Integer.toString(pokemoni.get(index).getNumber());
        id.setText(idText);
        String nameText = "name: " + pokemoni.get(index).getName();
        name.setText(nameText);
        String typesText = "types: " + pokemoni.get(index).getType1() + "  " + pokemoni.get(index).getType2();
        types.setText(typesText);
        String heightText = "height: " + Float.toString(pokemoni.get(index).getHeight()/(float)10) + "m";
        height.setText(heightText);
        String weightText = "weight: " + Float.toString(pokemoni.get(index).getWeight()/(float)10) + "kg";
        weight.setText(weightText);
        int[] stats = new int[6];
        stats = pokemoni.get(index).getStats();
        String hpText = "hp: " + Integer.toString(stats[0]);
        hp.setText(hpText);
        String attackText = "attack: " + Integer.toString(stats[1]);
        attack.setText(attackText);
        String defenseText = "defense: " + Integer.toString(stats[2]);
        defense.setText(defenseText);
        String specialAttackText = "special attack: " + Integer.toString(stats[3]);
        specialAttack.setText(specialAttackText);
        String specialDefenseText = "special defense: " + Integer.toString(stats[4]);
        specialDefense.setText(specialDefenseText);
        String speedText = "speed: " + Integer.toString(stats[5]);
        speed.setText(speedText);

        String imgPath = "pokemon" + Integer.toString(pokemoni.get(index).getNumber());
        int id = getResources().getIdentifier(imgPath, "drawable", DetailsActivity.this.getPackageName());
        img.setImageResource(id);

        initFragment(pokemoni.get(index).getMoves(),pokemoni.get(index).getSelectedMoves());

    }

    void initComponents()
    {
        img = findViewById(R.id.imageView2);
        id = findViewById(R.id.idTextView);
        name = findViewById(R.id.nameTextView);
        types = findViewById(R.id.typesTextView);
        height = findViewById(R.id.heightTextView);
        weight = findViewById(R.id.WeightTextView);
        hp = findViewById(R.id.hpTextView);
        attack = findViewById(R.id.attackTextView);
        defense = findViewById(R.id.defenseTextView);
        specialAttack = findViewById(R.id.specialAttackTextView);
        specialDefense = findViewById(R.id.specialDefenseTextView);
        speed = findViewById(R.id.speedTextView);
        button = findViewById(R.id.pokedexButton);
    }

    private void initFragment(List<String> moves, String[] selectedMoves)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (toggle)
        {
            AllMovesFragment amf = AllMovesFragment.newInstance(moves,this);
            ft.add(R.id.fragmentcontainer, amf);
        }
        else
        {
            SelectedMovesFragment smf = SelectedMovesFragment.newInstance(selectedMoves,this);
            ft.add(R.id.fragmentcontainer, smf);
        }
        ft.commit();
    }

    private void swap(int fragmentIndex)
    {
        Log.e("swap", String.valueOf(toggle));
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!toggle)
        {
            AllMovesFragment amf = AllMovesFragment.newInstance(pokemoni.get(index).getMoves(),this);
            ft.replace(R.id.fragmentcontainer, amf);
            selectedMoveIndex = fragmentIndex;
            toggle = true;
        }
        else if (!(sviPokemoni == null))
        {
            SelectedMovesFragment smf = SelectedMovesFragment.newInstance(pokemoni.get(index).getSelectedMoves(),this);
            ft.replace(R.id.fragmentcontainer, smf);
            moveIndex = fragmentIndex;
            pokemoni.get(index).setSelectedMove(selectedMoveIndex, pokemoni.get(index).getMoves().get(moveIndex));
            Log.e("selectedMoves", Arrays.toString(pokemoni.get(index).getSelectedMoves()));
            toggle = false;
        }
        ft.commit();
    }

    private void setupButton()
    {
        button = findViewById(R.id.pokedexButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this,MainActivity.class);
                if (sviPokemoni == null)
                {
                    sviPokemoni = pokemoni;
                    update = true;
                }
                else
                {
                    myTeamFromAll = pokemoni;
                    Log.e("mtfa1", String.valueOf(myTeamFromAll.get(0).getSelectedMoves()));
                }
                i.putExtra("pokemoni", sviPokemoni);
                i.putExtra("myTeam", myTeamFromAll);
                //i.putExtra("update", update);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void selectedItem(int fragmentIndex) {
        swap(fragmentIndex);
    }

//    @Override
//    public void onPause()
//    {
//        super.onPause();
//        toggle = false;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (sviPokemoni == null)
//            toggle = true;
//        else
//            toggle = false;
//    }
}