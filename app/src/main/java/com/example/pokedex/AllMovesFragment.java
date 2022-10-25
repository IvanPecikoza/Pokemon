package com.example.pokedex;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllMovesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllMovesFragment extends Fragment {

    private List<String> moves;
    private CustomInteraction parent;

    private void setMoves(List<String> moves) {this.moves = moves;}

    private  void setParent( CustomInteraction parent) {this.parent = parent;}


    public AllMovesFragment() {
        // Required empty public constructor
    }

    public static AllMovesFragment newInstance(List<String> moves, CustomInteraction parent) {
        AllMovesFragment fragment = new AllMovesFragment();
        fragment.setMoves(moves);
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
        View v = inflater.inflate(R.layout.fragment_all_moves, container,false);
        LinearLayout layout = v.findViewById(R.id.moveslayout);
        for(int i = 0; i< moves.size(); i++)
        {
            TextView t = new TextView(getContext());
            t.setText(moves.get(i));
            int finalI = i;                                             //copy so i can pass it to the interface
            t.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    parent.selectedItem(finalI);
                }
            });
            layout.addView(t);
        }
        Log.e("amf","oncreateview");
        return v;
    }
}