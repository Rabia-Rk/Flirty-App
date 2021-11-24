package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.Models.LiveCommentModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;

/**
 * Created by datting_package on 3/20/2018.
 */

public class LiveCommentsAdapter extends RecyclerView.Adapter<LiveCommentsAdapter.CustomViewHolder> {

    public Context context;
    private AdapterClickListener listener;
    private ArrayList<Object> dataList;

    public LiveCommentsAdapter(Context context, ArrayList<Object> dataList, AdapterClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public LiveCommentsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_comment_layout, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        LiveCommentsAdapter.CustomViewHolder viewHolder = new LiveCommentsAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    @Override
    public void onBindViewHolder(final LiveCommentsAdapter.CustomViewHolder holder, final int i) {
        final LiveCommentModel item = (LiveCommentModel) dataList.get(i);


        holder.username.setText(item.user_name);


        holder.message.setText(item.comment);
        if (item.user_picture != null && !item.user_picture.equals("")) {
            Uri uri = Uri.parse(item.user_picture);
            holder.userPic.setImageURI(uri);
        }

        holder.bind(i, item, listener);

    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView username, message;
        SimpleDraweeView userPic;


        public CustomViewHolder(View view) {
            super(view);

            username = view.findViewById(R.id.username);
            userPic = view.findViewById(R.id.user_pic);
            message = view.findViewById(R.id.message);

        }

        public void bind(final int postion, final LiveCommentModel item, final AdapterClickListener listener) {

            itemView.setOnClickListener(v -> {
                listener.onItemClick(postion, item, v);

            });

        }


    }


}