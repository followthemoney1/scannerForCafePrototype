package com.leaditteam.qrscanner.helpers;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leaditteam.qrscanner.adapters.AdapterForListView;
import com.leaditteam.qrscanner.adapters.HintSpinnerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by leaditteam on 21.04.17.
 */

public class TimeChangeHelper {
    private ArrayList<String> DATE = new ArrayList<>();
    private ArrayList<String> EMAIL = new ArrayList<>();
    private ArrayList<String> NAME_USER = new ArrayList<>();
    private ArrayList<String> PRODUCT = new ArrayList<>();
    private ArrayList<Integer> SUM = new ArrayList<>();

    private AdapterForListView mAtapter;

    private TimeParser currentTime = DateHelper.getCurrentTime();
    private Context mContext;

    private int allSumm = 0;
    private TextView tvAllSumm;
    private int KEY;
    private Spinner hintSpinner;
    private ArrayAdapter<CharSequence> adapter;
    private HashMap<Integer, ContaiterForByedProduct> main = new HashMap<Integer, ContaiterForByedProduct>();
    public TimeChangeHelper( AdapterForListView mAtapter, Context mContext,int key, TextView tvAllSumm, Spinner spinner, ArrayAdapter<CharSequence> adapter, HashMap<Integer, ContaiterForByedProduct> main) {

        this.mAtapter = mAtapter;
        this.mContext = mContext;
        this.KEY = key;
        this.hintSpinner = spinner;
        this.adapter = adapter;
        this.main = main;
        
        if (KEY==1){
            byed_product_refresh();
        }else{
            statistic_product_refresh();
            this.tvAllSumm = tvAllSumm;
        }
    }

    public void forAMonth() {
        try {
            int j = 0;
            int i = 0;
            clear();
            do{
                if(currentTime.getMONTH() ==
                        DateHelper.parseTime(
                                (main.get(j)).getDATE()
                        ).getMONTH()) {
                    ContaiterForByedProduct conteinerUsers = main.get(j);
                    add(i++, conteinerUsers);
                }
            }while (j++ != main.size());

            set_data();
        } catch (Exception e) {
            e.printStackTrace();
            set_data();
        }
    }

    public void forAWeek() {
        try {
            int i = 0;
            int j = 0;
            clear();

            do{
                if(currentTime.getMONTH() ==
                        DateHelper.parseTime(
                                (main.get(j)).getDATE()
                        ).getMONTH()) {
                    int val = DateHelper.parseTime((main.get(j)).getDATE()).getDAY();
                    if ((currentTime.getDAY() - 7) <= val && val <= currentTime.getDAY()) {
                        ContaiterForByedProduct conteinerUsers = main.get(j);
                        add(i, conteinerUsers);
                        i++;
                    }

                }
            }while (j++!=main.size());
            set_data();

        } catch (Exception e) {
            e.printStackTrace();
            set_data();
        }
    }

    public void forThreeDays() {
        try {
            int i = 0;
            int j = 0;
            clear();

             do{
                 if(currentTime.getMONTH() ==
                         DateHelper.parseTime(
                                 (main.get(j)).getDATE()
                         ).getMONTH()){
                int val = DateHelper.parseTime((main.get(j)).getDATE()).getDAY();
                if ((currentTime.getDAY() - 2) <= val && val <= currentTime.getDAY()) {
                    ContaiterForByedProduct conteinerUsers = main.get(j);
                    add(i, conteinerUsers);
                    i++;
                }
                }

            }while (j++!=main.size());
            set_data();
        } catch (Exception e) {
            e.printStackTrace();
            set_data();
        }
    }

    public void forOneDay() {
        try {
            int i = 0;
            int j = 0;
            clear();

           do {
               if(currentTime.getMONTH() == DateHelper.parseTime((main.get(j)).getDATE()).getMONTH()) {
                   if (currentTime.getDAY() == DateHelper.parseTime((main.get(j)).getDATE()).getDAY()) {
                       ContaiterForByedProduct conteinerUsers = main.get(j);
                       add(i, conteinerUsers);
                       i++;
                   }
               }
            } while (j++!=main.size());
            set_data();
        } catch (Exception e) {
            e.printStackTrace();
           set_data();
        }
    }


    public AdapterForListView getDataFromSingleton() {
        try {
            clear();
            for (int i = 0; i != main.size(); i++) {
                ContaiterForByedProduct conteinerUsers = main.get(i);
                add(i, conteinerUsers);
            }
            if (KEY!=1){
                tvAllSumm.setText(String.valueOf(allSumm));
                mAtapter = new AdapterForListView(DATE, EMAIL, NAME_USER, null, SUM, mContext);
            }else  mAtapter = new AdapterForListView(DATE, EMAIL, NAME_USER, PRODUCT, SUM, mContext);
            return mAtapter;
        } catch (Exception e) {
            return null;
        }

    }
    protected void set_data(){
        if (KEY!=1){
            tvAllSumm.setText(String.valueOf(allSumm));
        }
        reverse();
        mAtapter.notifyDataSetChanged();
    }
    protected void reverse() {

        SortHelper.concurrentSort(DATE,NAME_USER,SUM,EMAIL,PRODUCT);
        Collections.sort(DATE,comparator_m);
        Collections.sort(DATE,comparator_h);
        Collections.sort(DATE,comparator_d);

    }
    Comparator<String> comparator_d = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            DateHelper dt = new DateHelper();
            TimeParser oc = dt.parseTime(o1);
            TimeParser ov = dt.parseTime(o2);

            if (oc.getDAY() > ov.getDAY()){
                return -1;
            } else return 0;

        }

    };
    Comparator<String> comparator_m = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            DateHelper dt = new DateHelper();
            TimeParser oc = dt.parseTime(o1);
            TimeParser ov = dt.parseTime(o2);

            if (oc.getMIN() > ov.getMIN()){
                return -1;
            } else return 0;

        }

    };
    Comparator<String> comparator_h = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            DateHelper dt = new DateHelper();
            TimeParser oc = dt.parseTime(o1);
            TimeParser ov = dt.parseTime(o2);

            if (oc.getHOUR() > ov.getHOUR()){
                return -1;
            } else return 0;

        }

    };


    protected void clear() {
        DATE.clear();
        EMAIL.clear();
        NAME_USER.clear();
        PRODUCT.clear();
        SUM.clear();
        allSumm = 0;
    }
    protected void add(int i, ContaiterForByedProduct conteinerUsers ){
        DATE.add(i, conteinerUsers.getDATE());
        EMAIL.add(i, conteinerUsers.getEMAIL());
        NAME_USER.add(i, conteinerUsers.getNAME_USER());
        PRODUCT.add(i, conteinerUsers.getPRODUCT());
        SUM.add(i, conteinerUsers.getSUM());
        allSumm += conteinerUsers.getSUM();
    }
    protected void byed_product_refresh(){
        FirebaseDatabase.getInstance().getReference().child("USERS_WHO_BUY").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    clear();
                    for (int i = 0; i != main.size(); i++) {
                        ContaiterForByedProduct conteinerUsers = main.get(i);
                        add(i, conteinerUsers);
                    }
                    hintSpinner.setAdapter(new HintSpinnerAdapter(
                            adapter, R.layout.hint_row_item, mContext));
                    reverse();
                    if(mAtapter != null)
                    mAtapter.notifyDataSetChanged();
                } catch (Exception e){
                    e.printStackTrace();

                    reverse();
                    hintSpinner.setAdapter(new HintSpinnerAdapter(
                            adapter, R.layout.hint_row_item, mContext));
                    if(mAtapter != null)
                    mAtapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void statistic_product_refresh(){
        FirebaseDatabase.getInstance().getReference().child("OFFICIANT").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    clear();
                    for (int i = 0; i != main.size(); i++) {
                        ContaiterForByedProduct conteinerUsers = main.get(i);
                        add(i,conteinerUsers);
                    }

                    tvAllSumm.setText(String.valueOf(allSumm));

                    hintSpinner.setAdapter(new HintSpinnerAdapter(
                            adapter, R.layout.hint_row_item, mContext));
                    reverse();
                    mAtapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();

                    hintSpinner.setAdapter(new HintSpinnerAdapter(
                            adapter, R.layout.hint_row_item, mContext));

                    reverse();
                    tvAllSumm.setText(String.valueOf(allSumm));
                    mAtapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
