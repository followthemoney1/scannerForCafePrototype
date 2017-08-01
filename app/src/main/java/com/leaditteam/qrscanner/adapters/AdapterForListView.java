package com.leaditteam.qrscanner.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leaditteam.qrscanner.R;

import java.util.ArrayList;

/**
 * Created by leaditteam on 13.04.17.
 */

public class AdapterForListView extends BaseAdapter {
    private ArrayList<String> DATE;
    private ArrayList<String> EMAIL;
    private ArrayList<String> NAME_USER;
    private ArrayList<String> PRODUCT;
    private ArrayList<Integer> SUM;

    private TextView tvNamePeople;
    private TextView tvMail;
    private TextView tvNameOfProduct;
    private TextView tvPrice;
    private TextView tvData;
    private Context mContext;

    public AdapterForListView(ArrayList date, ArrayList email, ArrayList name_user, ArrayList product, ArrayList sum, Context context) {
        this.DATE = date;
        this.EMAIL = email;
        this.NAME_USER = name_user;
        this.PRODUCT = product;
        this.SUM = sum;
        this.mContext = context;
    }

    public int getCount() {
        if (DATE.size() > 0) {
        return DATE.size();}else
            return 0;
    }

    public Object getItem(int position) {
        if (DATE.size() > 0) {
            return DATE.get(position);}else
            return 0;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int id, View view, ViewGroup viewGroup) {
        View root;

        if (PRODUCT != null) {

            root = View.inflate(mContext, R.layout.list_item_singlecheck_arist, null);

            tvNameOfProduct = (TextView) root.findViewById(R.id.tv_pr);
            tvNameOfProduct.setText(PRODUCT.get(id).toString());

        } else root = View.inflate(mContext, R.layout.list_item_singlecheck_statistik, null);

        tvNamePeople = (TextView) root.findViewById(R.id.tv_name);
        tvMail = (TextView) root.findViewById(R.id.tv_main);
        tvData = (TextView) root.findViewById(R.id.tv_data);
        tvPrice = (TextView) root.findViewById(R.id.tv_price);

        tvNamePeople.setText(NAME_USER.get(id).toString());
        tvMail.setText(EMAIL.get(id).toString());
        tvData.setText(DATE.get(id).toString());
        tvPrice.setText(SUM.get(id).toString());


        return root;

    }
}