package com.example.sonnv.dontbelazy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.ShowingContactActivity;
import com.example.sonnv.dontbelazy.databases.Contact;

import java.util.List;


/**
 * Created by Nttung PC on 9/21/2017.
 */

public class RVContactAdapter extends RecyclerView.Adapter<RVContactAdapter.StoryViewHolder>  {
    private Context context;
    public static final String KEY_CONTACT = "key_contact";

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvName;
        TextView tvSubject;
        ImageView ivProfile;

        public StoryViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cv_contact);
            tvName = itemView.findViewById(R.id.tv_name_cv);
            tvSubject = itemView.findViewById(R.id.tv_subject_cv);
            ivProfile = itemView.findViewById(R.id.iv_sex_cv);

        }
    }

    private List<Contact> contactList;

    public RVContactAdapter(List<Contact> contactsList) {
        this.contactList = contactsList;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_contact, viewGroup, false);
        StoryViewHolder svh = new StoryViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(final StoryViewHolder holder, final int position) {
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowingContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_CONTACT,contactList.get(position));
                intent.putExtras(bundle);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                        new Pair<View, String>(holder.ivProfile, ViewCompat.getTransitionName(holder.ivProfile)));
                context.startActivity(intent, options.toBundle());
            }
        });
        holder.tvName.setText(contactList.get(position).getName());
        holder.tvSubject.setText(contactList.get(position).getSubject());
        if (contactList.get(position).isMale()){
            holder.ivProfile.setImageResource(R.drawable.ic_male);
        }else{
            holder.ivProfile.setImageResource(R.drawable.ic_female);
        }
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

}
