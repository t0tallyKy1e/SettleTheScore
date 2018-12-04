package com.ckm.settlethescore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ckm.settlethescore.ScoresFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MyScoresRecyclerViewAdapter extends RecyclerView.Adapter<MyScoresRecyclerViewAdapter.ViewHolder> {

    private final List<ScoreData.ScoreItem> mValues;
    private final OnListFragmentInteractionListener mListener;

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
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).score);
        holder.mGameStatus.setText(mValues.get(position).gameStatus);

        switch(mValues.get(position).count % 3){
            case 0:
                holder.mGameType.setImageResource(R.drawable.scissors);
                break;
            case 1:
                holder.mGameType.setImageResource(R.drawable.dices_home);
                break;
            case 2:
                holder.mGameType.setImageResource(R.drawable.life_main);
                break;
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
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
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mGameStatus = (TextView) view.findViewById(R.id.game_status);
            mGameType = (ImageView) view.findViewById(R.id.game_type);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mGameStatus.getText() + "'";
        }
    }
}
