package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.R;

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.CustomViewHolder > {

    public Context context;
    ArrayList<String> gifList;

    AdapterClickListener adapterClicklistener;

    public GifAdapter(Context context, ArrayList<String> urllist, AdapterClickListener adapterClicklistener) {
        this.context = context;
        this.gifList =urllist;
        this.adapterClicklistener = adapterClicklistener;

    }

    @Override
    public GifAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gif_layout,null);
        GifAdapter.CustomViewHolder viewHolder = new GifAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return gifList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView gifImage;

        public CustomViewHolder(View view) {
            super(view);
            gifImage =view.findViewById(R.id.gif_image);
        }

        public void bind(final int pos, final String item, final AdapterClickListener listener) {

            itemView.setOnClickListener(v->{
                    listener.onItemClick(pos,item,v);

            });


        }

    }


    @Override
    public void onBindViewHolder(final GifAdapter.CustomViewHolder holder, final int i) {
        holder.bind(i, gifList.get(i), adapterClicklistener);
        Glide.with(context)
                .load(Variables.gifFirstpart + gifList.get(i)+ Variables.gifSecondpart)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).placeholder(ContextCompat.getDrawable(context,R.drawable.image_placeholder)).centerCrop())
                .into(holder.gifImage);
    }

}
