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
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Models.LiveUserModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;

/**
 * Created by datting_package on 3/20/2018.
 */

public class LiveUserAdapter extends RecyclerView.Adapter<LiveUserAdapter.CustomViewHolder> {
    public Context context;
    ArrayList<LiveUserModel> dataList;
    AdapterClickListener adapterClickListener;
    int width;

    public LiveUserAdapter(Context context, ArrayList<LiveUserModel> userDatalist, AdapterClickListener adapterClickListener) {

        this.context = context;
        this.dataList = userDatalist;

        width = (Variables.width / 2) - 20;
        this.adapterClickListener = adapterClickListener;

    }

    @Override
    public LiveUserAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_layout, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(width, Variables.width - 300));
        LiveUserAdapter.CustomViewHolder viewHolder = new LiveUserAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(final LiveUserAdapter.CustomViewHolder holder, final int i) {

        final LiveUserModel item = dataList.get(i);

        holder.bind(i, item, adapterClickListener);


        holder.name.setText(item.getUser_name());
        if (item.getUser_picture() != null && !item.getUser_picture().equals("")) {
            Uri uri = Uri.parse(item.getUser_picture());
            holder.image.setImageURI(uri);
        }


    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView name, age, distanceTxt;
        public SimpleDraweeView image;


        public CustomViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.username);
            image = view.findViewById(R.id.image);
            distanceTxt = view.findViewById(R.id.distance_txt);

        }

        public void bind(final int pos, final LiveUserModel item,
                         final AdapterClickListener adapterClickListener) {

            itemView.setOnClickListener(v -> adapterClickListener.onItemClick(pos, item, v));

        }


    }


}