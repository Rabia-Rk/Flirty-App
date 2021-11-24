package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.datting_package.Flirty_Datting_App.Models.InfoListModel;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.R;

public class ListCustomAdapter extends BaseAdapter {

    private Context context;
    public static ArrayList<InfoListModel> modelArrayList;
    AdapterClickListener adapterClickListener;

    public ListCustomAdapter(Context context, ArrayList<InfoListModel> modelArrayList, AdapterClickListener adapterClickListener) {

        this.context = context;
        this.modelArrayList = modelArrayList;
        this.adapterClickListener = adapterClickListener;

    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        InfoListModel infoList_model = modelArrayList.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_listview, null, true);

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_id);
            holder.tvAnimal = (TextView) convertView.findViewById(R.id.textview_id);
            holder.ll = (LinearLayout) convertView.findViewById(R.id.linearlayout_id);

            convertView.setTag(holder);
        }else {
          holder = (ViewHolder)convertView.getTag();
        }

        holder.tvAnimal.setText(modelArrayList.get(position).getText());

        holder.checkBox.setChecked(modelArrayList.get(position).getSelected());

        holder.checkBox.setTag(R.integer.btnplusview, convertView);
        holder.checkBox.setTag( position);
        holder.bind(position, infoList_model, adapterClickListener);

        return convertView;
    }

    private class ViewHolder {

        public CheckBox checkBox;
        public TextView tvAnimal;
        public LinearLayout ll;

        public void bind(final  int pos , final InfoListModel item , final AdapterClickListener onClickListner){

            ll.setOnClickListener(v ->  {
                    onClickListner.onItemClick(pos,item,v);

            });
        }
    }
}
