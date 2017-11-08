package com.example.sonnv.dontbelazy.adapter.notes;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.notes.EditNote;
import com.example.sonnv.dontbelazy.databases.Note;
import com.example.sonnv.dontbelazy.utils.Constants;
import com.example.sonnv.dontbelazy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by cuonghx2709 on 9/26/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.StoryViewHolder> {
    private Context context;
    private List<Note> list;
    public static final String Key_NOTE = "123456789";
    private Activity activity;

    public NoteAdapter(Context context, List<Note> list, Activity activity){
        this.context = context;
        this.activity = activity;
        this.list = list;
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        View view;

        public StoryViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.cv_item_note);
        }
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note, viewGroup, false);

        NoteAdapter.StoryViewHolder svh = new NoteAdapter.StoryViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(final StoryViewHolder holder, final int position) {
        View view = holder.view;
        final Note note = list.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat out = new SimpleDateFormat("HH:mm, dd/MM/yyyy");

        TextView tvTimeNote = view.findViewById(R.id.tv_time_note);
        TextView tvNote = view.findViewById(R.id.tv_note);
        CardView item = view.findViewById(R.id.cv_item_note);
        ImageView imageView = view.findViewById(R.id.iv_pick_alarm);

        LinearLayout linearLayout = view.findViewById(R.id.ln_item_note);


       if (note.getTime() != null && note.isNotifi()){
           Date time = Utils.getDate(simpleDateFormat,note.getTime());
           tvTimeNote.setText(out.format(time));
           imageView.setImageResource(R.drawable.ic_access_alarm_blu_24dp);
       }else {
           imageView.setImageResource(R.drawable.ic_access_alarm_black_24dp);
       }
        tvNote.setText(note.getText());


        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditNote.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Key_NOTE, note);


                intent.putExtras(bundle);
                if (Build.VERSION.SDK_INT >= 21) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
                    context.startActivity(intent, options.toBundle());
                }else {
                    context.startActivity(intent);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        if (list == null){
            return 0;
        }
        return list.size();
    }
}
