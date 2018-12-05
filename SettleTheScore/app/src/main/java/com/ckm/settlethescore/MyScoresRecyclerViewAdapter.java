package com.ckm.settlethescore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ckm.settlethescore.ScoresFragment.OnListFragmentInteractionListener;

import java.util.List;
import java.util.Random;

public class MyScoresRecyclerViewAdapter extends RecyclerView.Adapter<MyScoresRecyclerViewAdapter.ViewHolder> {

    private final List<ScoreData.ScoreItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    private static String[] sampleDiceScores = {"Marvin rolled a 20 out of 20",
            "Chris rolled a 5 out of 100",
            "Kyle rolled a 4 out of 5 "};
    private static String[] sampleLifeScores = {"Chris: 20 life, Kyle: 18 life",
            "Marvin: 2 life, Kyle: 18 life",
            "Kyle: 20 life, Marvin: 8 life",
            "Chris: 6 life, Marvin: 15 life"};
    private static String[] sampleRPSScores = {"Kyle Won: Paper beats Rock",
            "Chris Won: Rock beats Scissors",
            "Marvin Won: Scissors beats paper" };

    public MyScoresRecyclerViewAdapter(List<ScoreData.ScoreItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Random rand = new Random(System.currentTimeMillis());
        holder.mItem = mValues.get(position);
        if(mValues.get(position).game_type != null){
            switch(mValues.get(position).game_type){
                case "ROCK_PAP_SCI":
                    holder.mGameType.setImageResource(R.drawable.scissors);
                    holder.mGameStatus.setText(sampleRPSScores[rand.nextInt(sampleRPSScores.length)]);
                    break;
                case "DICE":
                    holder.mGameType.setImageResource(R.drawable.dices_home);
                    holder.mGameStatus.setText(sampleDiceScores[rand.nextInt(sampleDiceScores.length)]);
                    break;
                case "LIFE":
                    holder.mGameType.setImageResource(R.drawable.life_main);
                    holder.mGameStatus.setText(sampleLifeScores[rand.nextInt(sampleLifeScores.length)]);
                    break;
                default:
                    holder.mGameType.setImageResource(R.drawable.life_main);
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mGameStatus;
        public final ImageView mGameType;
        public ScoreData.ScoreItem mItem;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mGameStatus = (TextView) view.findViewById(R.id.game_status);
            mGameType = (ImageView) view.findViewById(R.id.game_type);
        }

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), RocPapSci.class);
            context.startActivity(intent);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mGameStatus.getText() + "'";
        }
    }
}
