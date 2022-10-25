package com.example.pokedex;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectedMovesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectedMovesFragment extends Fragment {

    private String[] selectedMoves = new String[4];
    private CustomInteraction parent;

    private void setSelectedMoves(String[] selectedMoves) {this.selectedMoves = selectedMoves;}

    private  void setParent( CustomInteraction parent) {this.parent = parent;}

    public SelectedMovesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SelectedMovesFragment newInstance(String[] selectedMoves, CustomInteraction parent) {
        SelectedMovesFragment fragment = new SelectedMovesFragment();
        fragment.setSelectedMoves(selectedMoves);
        fragment.setParent(parent);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_selected_move, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

        //super OnCreateView();
        Button move1 = view.findViewById(R.id.move1);
        Button move2 = view.findViewById(R.id.move2);
        Button move3 = view.findViewById(R.id.move3);
        Button move4 = view.findViewById(R.id.move4);

        if (selectedMoves != null)
        {
            if (selectedMoves[0] != null)
                move1.setText(selectedMoves[0]);
            if (selectedMoves[2] != null)
                move2.setText(selectedMoves[1]);
            if (selectedMoves[2] != null)
                move3.setText(selectedMoves[2]);
            if (selectedMoves[3] != null)
                move4.setText(selectedMoves[3]);

        }


        move1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                parent.selectedItem(0);
            }
        });
        move2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                parent.selectedItem(1);
            }
        });
        move3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                parent.selectedItem(2);
            }
        });
        move4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                parent.selectedItem(3);
            }
        });
    }
}