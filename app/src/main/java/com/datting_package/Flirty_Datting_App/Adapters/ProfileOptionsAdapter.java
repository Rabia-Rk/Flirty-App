package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.Models.ProfileInfoModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;

public class ProfileOptionsAdapter extends RecyclerView.Adapter<ProfileOptionsAdapter.ViewHolder> {

    Context context;
    ArrayList<ProfileInfoModel> list;
    AdapterClickListener adapterClicklistener;


    public ProfileOptionsAdapter(Context context, ArrayList<ProfileInfoModel> list, AdapterClickListener adapterClicklistener) {
        this.context = context;
        this.list = list;
        this.adapterClicklistener = adapterClicklistener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_profile_options, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        ProfileInfoModel item = list.get(i);
        viewHolder.tv.setText(item.getText());

        viewHolder.bind(i, item, adapterClicklistener);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        LinearLayout ll;
        RadioButton rbId;
        LinearLayout linearlayoutId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.textview_id);
            ll = (LinearLayout) itemView.findViewById(R.id.linearlayout_id);
            rbId = itemView.findViewById(R.id.rb_id);
            linearlayoutId = itemView.findViewById(R.id.linearlayout_id);


        }

        public void bind(final int pos, final ProfileInfoModel item, final AdapterClickListener onClickListner) {

            ll.setOnClickListener(view -> onClickListner.onItemClick(pos, item, view));
        }
    }

}
