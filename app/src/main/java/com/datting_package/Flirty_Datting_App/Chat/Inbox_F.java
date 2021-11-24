package com.datting_package.Flirty_Datting_App.Chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.datting_package.Flirty_Datting_App.Activities.Chat_A;
import com.datting_package.Flirty_Datting_App.Activities.MainActivity;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.RootFragment;
import com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Fragments.Main_F;
import com.datting_package.Flirty_Datting_App.Fragments.User_likes_F;
import com.datting_package.Flirty_Datting_App.InAppSubscription.InApp_Subscription_A;
import com.datting_package.Flirty_Datting_App.Models.InboxModel;
import com.datting_package.Flirty_Datting_App.Models.MatchModel;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.Utils.OnSwipeTouchListener;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiLinks;
import com.datting_package.Flirty_Datting_App.VolleyPackage.ApiRequest;
import com.datting_package.Flirty_Datting_App.VolleyPackage.CallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.datting_package.Flirty_Datting_App.Chat.ExpandList_Adapter.listTitleTextView;
import static com.datting_package.Flirty_Datting_App.CodeClasses.SharedPrefrence.shareFilterInboxKey;

public class Inbox_F extends RootFragment implements View.OnClickListener {
    public static MatchAdapter matchAdapter;
    public static ExpandableListView expandableListView;
    public static List<MatchModel> listMyMatch = new ArrayList<>();
    View view;
    RecyclerView matchRecyclerview, inboxRecyclerview;
    ExpandList_Adapter listAdp;
    Context context;
    InboxAdapter adapter;
    TextView noMyMatch;
    DatabaseReference rootref;
    ArrayList<InboxModel> inboxArraylist;
    ProgressBar progressLoader;
    String inboxFilterKey;
    TextView noRecord;
    SimpleDraweeView likesImage;
    TextView likesCountTxt;
    private ArrayList<String> parentItems = new ArrayList<>();
    private ArrayList<Object> childItems = new ArrayList<>();

    public static void likeChat(final String Receiverid, Context context, final String is_like) {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference inboxChangeStatus1 = reference.child("Inbox").child(Variables.userId + "/" + Receiverid);

        inboxChangeStatus1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.child("rid").getValue().equals(Receiverid)) {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("like", is_like);
                    inboxChangeStatus1.updateChildren(result);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_inbox, null);

        context = getContext();
        matchRecyclerview = view.findViewById(R.id.match_recyclerview);
        inboxRecyclerview = view.findViewById(R.id.inbox_recyclerview);
        matchAdapter = new MatchAdapter(context, listMyMatch);
        noMyMatch = view.findViewById(R.id.no_my_match);
        progressLoader = view.findViewById(R.id.progress_loader);
        Main_F.toolbar.setVisibility(View.GONE); // Display ToolBar in MAIN_F
        noRecord = view.findViewById(R.id.no_record);


        matchRecyclerview.setClipToPadding(false);
        matchRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        matchRecyclerview.setHasFixedSize(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        inboxRecyclerview.setHasFixedSize(false);
        inboxRecyclerview.setLayoutManager(linearLayoutManager);


        matchRecyclerview.setAdapter(matchAdapter);

        myMatch();

        expandableListView = view.findViewById(R.id.expandable_layout);
        expandableListView.setClickable(true);

        setexpandlist();


        inboxRecyclerview.setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeTop() {

                expandableListView.collapseGroup(0);
            }

            @Override
            public void onSwipeRight() {

            }

            @Override
            public void onSwipeLeft() {
            }

            @Override
            public void onSwipeBottom() {
                expandableListView.expandGroup(0);
            }

        });


        expandableListView.setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeTop() {
                expandableListView.collapseGroup(0);
            }

            @Override
            public void onSwipeRight() {

            }

            @Override
            public void onSwipeLeft() {
            }

            @Override
            public void onSwipeBottom() {
                expandableListView.expandGroup(0);
            }

        });


        likesImage = view.findViewById(R.id.likes_image);
        likesCountTxt = view.findViewById(R.id.likes_count_txt);


        view.findViewById(R.id.likes_count_layout).setOnClickListener(this);


        displayInbox();


        return view;
    }

    private void setexpandlist() {

        setchilditems();
        setGroupParents();

        listAdp = new ExpandList_Adapter(getActivity(), parentItems, childItems, new ExpandList_Adapter.callBackClick() {
            @Override
            public void sortByDateNew(String child) {

                SharedPrefrence.saveString(context, "" + child, "" + shareFilterInboxKey);

                setchilditems();


                if (child.equals("Date")) {

                    sortByDate();
                } else if (child.equals("Favorites")) {

                    sortByLike();
                } else if (child.equals("Visits")) {

                    sortByVisits();
                } else if (child.equals("Chats")) {

                    sortByDate();
                }

            }
        });

        expandableListView.setAdapter(listAdp);


        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> false);

    }

    private void setchilditems() {

        childItems.clear();

        String localFilter = SharedPrefrence.getString(context, SharedPrefrence.shareFilterInboxKey);
        if (localFilter == null) {
            localFilter = "All Connections";
        }

        ArrayList<String> child = new ArrayList<>();

        child.add("All Connections");
        child.add("Online");
        child.add("Visits");
        child.add("Date");
        child.add("Favorites");

        child.remove(localFilter);

        childItems.add(child);
    }

    private void setGroupParents() {
        parentItems.clear();
        parentItems.add("All Connections");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.likes_count_layout:
                if (MainActivity.purductPurchase)
                    openUserList();
                else
                    openSubscriptionView();
                break;

            default:
                break;
        }

    }

    public void openSubscriptionView() {

        InApp_Subscription_A inappSubscriptionA = new InApp_Subscription_A();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.Main_F, inappSubscriptionA)
                .addToBackStack(null)
                .commit();

    }

    public void openUserList() {

        User_likes_F userLikesF = new User_likes_F(bundle -> myMatch(), false);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("like_count", likesCountTxt.getText().toString());
        userLikesF.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Main_F, userLikesF).commit();

    }

    // API to get All My Match
    public void myMatch() {
        String fbId = SharedPrefrence.getString(context, SharedPrefrence.uId);

        Functions.showLoader(context, false, false);

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fbId);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.callApi(
                context,
                "" + ApiLinks.myMatch,
                parameters,
                new CallBack() {
                    @Override
                    public void getResponse(String requestType, String response) {

                        Functions.cancelLoader();

                        parseMatchData(response);

                    }
                }


        );


    }

    public void displayInbox() {

        String userId = Functions.getInfo(context, "fb_id");
        String firstName = Functions.getInfo(context, "first_name");
        String userPic = Functions.getInfo(context, "image1");


        Variables.userId = userId;
        Variables.userName = firstName;
        Variables.userPic = userPic;
        rootref = FirebaseDatabase.getInstance().getReference();
        inboxArraylist = new ArrayList<>();

        adapter = new InboxAdapter(context, inboxArraylist, new AdapterClickListener() {
            @Override
            public void onItemClick(int postion, Object model, View view) {

                InboxModel inboxModel = (InboxModel) model;


                Intent myIntent = new Intent(context, Chat_A.class);
                myIntent.putExtra("receiver_id", inboxModel.getRid());
                myIntent.putExtra("receiver_name", inboxModel.getName());
                myIntent.putExtra("receiver_pic", inboxModel.getPic());
                myIntent.putExtra("is_block", inboxModel.getBlock());
                myIntent.putExtra("match_api_run", "0");
                context.startActivity(myIntent);
            }

            @Override
            public void onLongItemClick(int postion, Object model, View view) {

            }
        });

        inboxRecyclerview.setAdapter(adapter);


        userId = SharedPrefrence.getSocialInfo(context, SharedPrefrence.uLoginDetail, "fb_id");

        ValueEventListener eventListener;
        Query inboxQuery;
        progressLoader.setVisibility(View.VISIBLE);
        inboxQuery = rootref.child("Inbox").child(userId).orderByChild("timestamp");
        inboxQuery.keepSynced(true);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inboxArraylist.clear();
                progressLoader.setVisibility(View.GONE);

                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        InboxModel model = ds.getValue(InboxModel.class);

                        if (model.getLike() != null) {
                            if (model.getLike().equals("1")) {
                                // If Like
                                inboxArraylist.add(0, model);
                                adapter.notifyDataSetChanged();
                            } else {
                                inboxArraylist.add(model);
                                adapter.notifyDataSetChanged();
                            }

                        }

                    }

                }


                if (inboxArraylist.size() == 0) {
                    showNorecordText();
                } else {
                    view.findViewById(R.id.no_record).setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inboxQuery.addValueEventListener(eventListener);


    }

    public void showNorecordText() {
        view.findViewById(R.id.no_record).setVisibility(View.VISIBLE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {


            myMatch();


            Variables.varTabChange = 1; // one

            inboxFilterKey = SharedPrefrence.getString(context, "" + shareFilterInboxKey);

            if (listTitleTextView != null) {
                listTitleTextView.setText("" + inboxFilterKey);
            }


            if (inboxFilterKey != null) {
                if (inboxFilterKey.equals("Date")) {
                    sortByDate();
                } else if (inboxFilterKey.equals("Favorites")) {

                    sortByLike();
                } else if (inboxFilterKey.equals("Visits")) {
                    sortByVisits();
                } else if (inboxFilterKey.equals("Chats")) {
                    sortByDate();
                }
            }


        }


    }

    public void parseMatchData(String loginData) {
        try {
            JSONObject jsonObject = new JSONObject(loginData);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                listMyMatch.clear();
                JSONArray msg = jsonObject.getJSONArray("msg");
                for (int i = 0; i < msg.length(); i++) {
                    JSONObject userdata = msg.getJSONObject(i);
                    JSONObject usernameObj = userdata.getJSONObject("effect_profile_name");

                    MatchModel model = new MatchModel(
                            "" + userdata.getString("effect_profile"),
                            "" + usernameObj.getString("image1"),
                            "" + usernameObj.getString("first_name") + " " + usernameObj.getString("last_name")
                    );


                    listMyMatch.add(model);
                }
                matchAdapter.notifyDataSetChanged();

                if (listMyMatch.isEmpty()) {
                    noMyMatch.setVisibility(View.VISIBLE);
                } else {

                    noMyMatch.setVisibility(View.GONE);
                }


                JSONObject myLikes = jsonObject.optJSONObject("myLikes");
                if (myLikes != null) {
                    int count = myLikes.optInt("total");
                    String image1 = myLikes.optString("image1");

                    if (count > 0) {
                        likesCountTxt.setText(count + " Likes");

                        if (image1 != null && !image1.equals("")) {
                            Uri uri = Uri.parse(image1);
                            likesImage.setImageURI(uri);
                        }


                        view.findViewById(R.id.likes_count_layout).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.likes_count_layout).setOnClickListener(this);

                    } else {
                        view.findViewById(R.id.likes_count_layout).setVisibility(View.GONE);
                    }
                }


            }
        } catch (JSONException e) {
            Functions.toastMsg(context, "" + e.toString());
            e.printStackTrace();
        }


    }

    // Method to sort by Date
    public void sortByDate() {
        String userId = SharedPrefrence.getSocialInfo(context, SharedPrefrence.uLoginDetail, "fb_id");

        ValueEventListener eventListener;
        Query inboxQuery;
        progressLoader.setVisibility(View.VISIBLE);
        inboxQuery = rootref.child("Inbox").child(userId).orderByChild("sort");

        inboxQuery.keepSynced(true);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inboxArraylist.clear();
                progressLoader.setVisibility(View.GONE);

                if (dataSnapshot.exists()) {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);

                    inboxRecyclerview.setLayoutManager(linearLayoutManager);

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        InboxModel model = ds.getValue(InboxModel.class);
                        inboxArraylist.add(model);
                        adapter.notifyDataSetChanged();

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inboxQuery.addValueEventListener(eventListener);


    }

    // Method to get data Order by like
    public void sortByLike() {
        String userId = SharedPrefrence.getSocialInfo(context, SharedPrefrence.uLoginDetail, "fb_id");

        ValueEventListener eventListener;
        Query inboxQuery;
        progressLoader.setVisibility(View.VISIBLE);
        inboxQuery = rootref.child("Inbox").child(userId).orderByChild("timestamp");
        inboxQuery.keepSynced(true);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inboxArraylist.clear();
                progressLoader.setVisibility(View.GONE);

                if (dataSnapshot.exists()) {

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setReverseLayout(false);
                    linearLayoutManager.setStackFromEnd(false);

                    inboxRecyclerview.setLayoutManager(linearLayoutManager);


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        InboxModel model = ds.getValue(InboxModel.class);
                        if (model.getLike().equals("1")) {
                            inboxArraylist.add(0, model);
                            adapter.notifyDataSetChanged();
                        } else {
                            inboxArraylist.add(model);
                            adapter.notifyDataSetChanged();
                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inboxQuery.addValueEventListener(eventListener);


    }

    // Method to sort by Msg Read
    public void sortByVisits() {
        String userId = SharedPrefrence.getSocialInfo(context, SharedPrefrence.uLoginDetail, "fb_id");

        ValueEventListener eventListener;
        Query inboxQuery;
        progressLoader.setVisibility(View.VISIBLE);
        inboxQuery = rootref.child("Inbox").child(userId).orderByChild("timestamp");
        inboxQuery.keepSynced(true);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inboxArraylist.clear();
                progressLoader.setVisibility(View.GONE);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        InboxModel model = ds.getValue(InboxModel.class);
                        if (model.getStatus().equals("0")) {
                            // If msg not Read
                            inboxArraylist.add(0, model);
                            adapter.notifyDataSetChanged();
                        } else {
                            // If msg read
                            inboxArraylist.add(model);
                            adapter.notifyDataSetChanged();
                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inboxQuery.addValueEventListener(eventListener);


    }


}
