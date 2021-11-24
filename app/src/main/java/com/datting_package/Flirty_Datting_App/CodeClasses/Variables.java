package com.datting_package.Flirty_Datting_App.CodeClasses;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Variables {

    public static int height;
    public static int width;

    public static String downloadPref ="download_pref";
    public static String res;

    public static int varNumClickToShowAds = 3;
    public static int varFilledValNew;
    public static int per1;
    public static int varTabChange = 0;
    public static int isSearched = 0;
    public static int isSearchedAtWw = 0;

    public static int adDisplayAfter =9;


    public static int marginLeft = 10;
    public static int marginRight = 10;
    public static int marginTop = 10;
    public static int marginBottom = 10;


    public static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
    public static SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);



    public static int minAge = 15;

    public static String defalutMinAge ="15";
    public static String defalutMaxAge = "64";
    public static String defalutMaxDistance = "10000";
    public static String defalutGender ="all";
    public static String defalutSearchByStatus ="all";



    public static final boolean STREAMING_LIMIT =true;
    public static final int MAX_STREMING_TIME =60000;

    public static final boolean CALLING_LIMIT =true;
    public static final int MAX_VIDEO_CALLING_TIME =60000;
    public static final int MAX_VOICE_CALLING_TIME =60000;

    public static String openedChatId;
    public static String userName;
    public static String userPic;
    public static String userId;
    public static final int MY_API_TIMEOUT_MS = 30000;


    public static String varLiving = "";
    public static String varChildren = "";
    public static String varSmoking = "";
    public static String varDrinking = "";
    public static String varRelationship = "";
    public static String varSexuality = "";
    public static String varAboutMe = "";
    public static String userGender = "";



    public static String gifFirstpart ="https://media.giphy.com/media/";
    public static String gifSecondpart ="/100w.gif";

    public static String gifFirstpartChat ="https://media.giphy.com/media/";
    public static String gifSecondpartChat ="/200w.gif";


    // Arrays
    public static final String[] ARR_LIST_LIVING = {"No answer","By myself","Student residence","With parents","With partner","With housemate(s)"};
    public static final String[] ARR_LIST_RELATIONSHIP = {"No answer"," im in a complicated relationship", "Single"," Taken "};

    public static final String[] ARR_LIST_CHILDREN = {"No answer"," Grown up", "Already have"," No never ", "Someday"};
    public static final String[] ARR_LIST_SMOKE = {"No answer","Im a heavy smoker","I hate smoking","i dont like it","im a social smoker","I smoke occasionally"};
    public static final String[] ARR_LIST_DRINK = {"No answer"," I drink socially", "I dont drink"," Im against drinking ", "I drink a lot"};
    public static final String[] ARR_LIST_SEXUALITY = {"No answer","Bisexual", "Gay","Ask me ", "Straight"};
    public static final String[] ARR_LIST_GENDER = {"Male","Female", "I dont want to disclose"," No Way ", "Ask Me"};


    // Fragments Names
    public static final String FRAG_ABOUT = "Describe_yourself_F";
    public static final String FRAG_RELATION = "Relationship_A";
    public static final String FRAG_LIVING = "Living_F";
    public static final String FRAG_KIDS = "Kids_F";
    public static final String FRAG_SMOKE = "Smoke_F";
    public static final String FRAG_DRINK = "Drink_F";
    public static final String FRAG_GENDER = "Gender_F";


    public static String tag="hugme_";

    public static String verdana = "verdana.ttf";

    public static final int VEDIO_REQUEST_CODE = 9;

    public static final int PERMISSION_CAMERA_CODE =786;
    public static final int PERMISSION_WRITE_DATA =788;
    public static final int PERMISSION_READ_DATA =789;
    public static final int PERMISSION_RECORDING_AUDIO =790;
    public static final int CAMERA_MIC_PERMISSION_REQUEST_CODE = 795;

    public static final String LICENCEKEY ="play_store_licence_key";
    public static final String PRODUCT_ID ="android.Flirty_Datting_App.subscription1";
    public static final String PRODUCT_ID_2 ="android.Flirty_Datting_App.subscription2";
    public static final String PRODUCT_ID_3 ="android.Flirty_Datting_App.subscription3";


    public static final int BOOST_TIME =1800000;
    public static final boolean isSecureinfo = false;
}
