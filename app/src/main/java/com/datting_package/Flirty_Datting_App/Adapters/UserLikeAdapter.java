package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;

/**
 * Created by datting_package on 3/20/2018.
 */

public class UserLikeAdapter extends RecyclerView.Adapter<UserLikeAdapter.CustomViewHolder >{
    public Context context;
    ArrayList<NearbyUsersModel> dataList;
    AdapterClickListener adapterClickListener;
    int width;
    public UserLikeAdapter(Context context, ArrayList<NearbyUsersModel> userDatalist, AdapterClickListener adapterClickListener) {
        this.context = context;
        this.dataList=userDatalist;
        width=(Variables.width/2)-20;
        this.adapterClickListener=adapterClickListener;

    }

    @Override
    public UserLikeAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(width, Variables.width-300));
        UserLikeAdapter.CustomViewHolder viewHolder = new UserLikeAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView name,age, distanceTxt;
        public SimpleDraweeView image;
        public FrameLayout leftOverlay, rightOverlay;



        public CustomViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.username);
            image=view.findViewById(R.id.image);
            distanceTxt =view.findViewById(R.id.distance_txt);
            leftOverlay =view.findViewById(R.id.left_overlay);
            rightOverlay =view.findViewById(R.id.right_overlay);
        }

        public void bind(final int pos,final NearbyUsersModel item,
                         final AdapterClickListener adapterClickListener) {

            itemView.setOnClickListener(v -> {
                    adapterClickListener.onItemClick(pos,item,v);

            });

        }


    }


    @Override
    public void onBindViewHolder(final UserLikeAdapter.CustomViewHolder holder, final int i) {

        final NearbyUsersModel item=dataList.get(i);

        holder.bind(i,item,adapterClickListener);


        holder.name.setText(item.getFirst_name());
        holder.distanceTxt.setText(item.getDistance());

        if(item.getImage1()!=null && !item.getImage1().equals("")) {
            Uri uri = Uri.parse(item.getImage1());
            holder.image.setImageURI(uri);
        }

        holder.rightOverlay.setVisibility(View.GONE);
        holder.leftOverlay.setVisibility(View.GONE);


   }



}