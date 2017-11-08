package com.example.sonnv.dontbelazy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.subjects.ShowingSubjectActivity;

/**
 * Created by SONNV on 9/7/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.StoryViewHolder> {
    private Context context;

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        CardView cv;

        public StoryViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
        }
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        StoryViewHolder svh = new StoryViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(final StoryViewHolder holder, final int position) {
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ShowingSubjectActivity.class));
            }
        });
    }


    @Override
    public int getItemCount() {
        return 30;
    }
}
