package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.R;

public class ViewPagerAdpNew extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    List<String> listProfileImg;
    public ViewPagerAdpNew(Context context, List<String> listProfileImg) {
        this.context = context;
        this.listProfileImg = listProfileImg;
    }


    @Override
    public int getCount() {
        return listProfileImg.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view==(RelativeLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_view_prof,container,false);

        ImageView imageView = (ImageView) view.findViewById(R.id.view_prof_item_iv_id);

        try{
           Picasso.get().load(listProfileImg.get(position)).fit().centerCrop()
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(imageView);

        }catch (Exception b){
            Functions.toastMsg(context,"Err " + b.toString());
        }



        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);

    }

}
