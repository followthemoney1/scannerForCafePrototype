package com.leaditteam.qrscanner;

/**
 * Created by leaditteam on 13.04.17.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import android.util.Log;


import com.leaditteam.qrscanner.activities.ScannerActivity;
import com.leaditteam.qrscanner.helpers.ContaiterForByedProduct;

/**
 * Created by leaditteam on 14.03.17.
 */

public class GetDataFromFirebase {
    
    public static String PATH_TO_USER;
    
    private String DATE;
    private String EMAIL;
    private String NAME_USER;
    private String PRODUCT;
    private int SUM;
    
    //    public static HashMap<Integer, ConteinerForData> main_heshmap = new HashMap<Integer, ConteinerForData>();
    public static HashMap<Integer, ContaiterForByedProduct> main_menu = new HashMap<Integer, ContaiterForByedProduct>();
    public static HashMap<Integer, ContaiterForByedProduct> main_menu_officiant = new HashMap<Integer, ContaiterForByedProduct>();
    
    
    protected FirebaseDatabase database = FirebaseDatabase.getInstance();
    protected DatabaseReference reference;
    protected ProgressDialog progress;
    
    
    private int iteration = 0;
    private int iteration_for_constructor = 0;
    private int iteration_for_mein_heshmap = 0;
    private int iteration_for_menu = 0;
    
    
    private Boolean if_refresh = false;
    private Boolean is_show_coins;
    
    private String menu_name;
    private String menu_url;
    
    private static GetDataFromFirebase ourInstance = null;
    
    
    public static GetDataFromFirebase getInstance(Activity activity) {
        if (ourInstance == null) {
            //PATH_TO_USER = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString().replace(".", "_");
        }
        return ourInstance = new GetDataFromFirebase(activity);
    }
    
    public static GetDataFromFirebase deleteInstance() {
        try {
            FirebaseAuth.getInstance().signOut();
            
        } catch (Exception e) {
        }
        return ourInstance = null;
    }
    
    private GetDataFromFirebase(Activity activity) {
        get_firebase_menu(activity);
        getFirebaseOfficiant();
    }
    
    
    protected void get_firebase_menu(final Activity activity) {
        showProgress(activity);
        reference = database.getReference().child("TRANSACTIONS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear data
                iteration_for_menu = 0;
                main_menu.clear();
                
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    
                    
                    for (DataSnapshot messageSnapshot2 : messageSnapshot.getChildren()) {
                        DATE = messageSnapshot.child("DATE").getValue().toString();
                        EMAIL = messageSnapshot.child("EMAIL").getValue().toString();
                        NAME_USER = messageSnapshot.child("NAME_USER").getValue().toString();
                        PRODUCT = messageSnapshot.child("PRODUCT").getValue().toString();
                        SUM = Integer.parseInt(messageSnapshot.child("SUM").getValue().toString());
                        
                    }
                    
                    
                    //put to main map menu
                    main_menu.put(iteration_for_menu, new ContaiterForByedProduct(DATE, EMAIL, NAME_USER, PRODUCT, SUM));
                    iteration_for_menu += 1;
                    
                }
                if (!if_refresh) {
                    if_refresh = true;
                    //go to main act
                    noShowProgress();
                    goToMainAct(activity);
                }
                noShowProgress();
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
                noShowProgress();
            }
        });
    }
    
    
    String userUid;
    protected void getFirebaseOfficiant() {
        try {
            userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){};
        if (userUid != null)
        reference = database.getReference().child("WAITERS").child(userUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear data
                iteration_for_constructor = 0;
                main_menu_officiant.clear();
                
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    
                    for (DataSnapshot messageSnapshot2 : messageSnapshot.getChildren()) {
                        DATE = messageSnapshot.child("DATE").getValue().toString();
                        EMAIL = messageSnapshot.child("EMAIL").getValue().toString();
                        NAME_USER = messageSnapshot.child("NAME_USER").getValue().toString();
                        SUM = Integer.decode(messageSnapshot.child("SUM").getValue().toString());
                        
                    }
                    
                    //put to main map menu
                    main_menu_officiant.put(iteration_for_constructor, new ContaiterForByedProduct(DATE, EMAIL, NAME_USER, null, SUM));
                    iteration_for_constructor += 1;
                    
                }
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
                noShowProgress();
            }
        });
    }
    
    
    private void goToMainAct(Activity activity) {
        Log.d("Main menu", main_menu.toString());
        activity.startActivity(new Intent(activity, ScannerActivity.class));
        activity.finish();
    }
    
    private void showProgress(Activity activity) {
        // set progress
        progress = new ProgressDialog(activity);
        progress.setTitle("Загрузка");
        progress.setMessage("Подождите пока идет загрузка данных..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }
    
    private void noShowProgress() {
        progress.dismiss();
    }
    
    
}
