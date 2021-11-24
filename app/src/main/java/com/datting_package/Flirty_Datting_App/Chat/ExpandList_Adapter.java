package com.datting_package.Flirty_Datting_App.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;

import static com.datting_package.Flirty_Datting_App.Chat.Inbox_F.expandableListView;

public class ExpandList_Adapter extends BaseExpandableListAdapter {

    Context mcontext;
    private ArrayList<Object> childtems;
    private ArrayList<String> parentItems, child;
    public static TextView listTitleTextView;
    private callBackClick mCallBack;
    public interface callBackClick {
        void sortByDateNew(String child);
    }


    public ExpandList_Adapter(Context ctx, ArrayList<String> parents, ArrayList<Object> childern, callBackClick callback) {
        this.mcontext = ctx;
        this.parentItems = parents;
        this.childtems = childern;
        this.mCallBack = callback;
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((ArrayList<String>) childtems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            convertView = layoutInflater.inflate(R.layout.expand_group_item, null);
        }
        listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgTitle);
        String filter = SharedPrefrence.getString(mcontext,"" + SharedPrefrence.shareFilterInboxKey);

        if(filter != null){
            listTitleTextView.setText(filter);
        }else{

            listTitleTextView.setText("All Connections");
        }


        if (isExpanded){
            imageView.setImageResource(R.drawable.ic_up_arrow);
        }else {
            imageView.setImageResource(R.drawable.ic_down_arrow);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        child = (ArrayList<String>) childtems.get(groupPosition);


        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            convertView = layoutInflater.inflate(R.layout.expand_list_item, null);
        }
        final TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expand_list_item_id);


        expandedListTextView.setOnClickListener(v -> {

                listTitleTextView.setText(child.get(childPosition));
                expandableListView.collapseGroup(0);
                mCallBack.sortByDateNew(child.get(childPosition));

        });


        expandedListTextView.setText(child.get(childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}


