package com.leaditteam.qrscanner.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.leaditteam.qrscanner.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;



public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private SurfaceView mySurfaceView;
    private TextView textView;
    private String PATH_TO_USER;
    private String USER_COINS;
    private String[] separated;

    private FirebaseDatabase database = FirebaseDatabase.getInstance()  ;
    private DatabaseReference reference;
    private FirebaseAuth auth;

    private ZXingScannerView mScannerView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private String HOW_MUCH_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        addActionBar();

        Spinner hintSpinner = (Spinner) findViewById(R.id.show_pop_up);
        hintSpinner.setVisibility(View.GONE);
        //set up coints to toolbar // if back need refresh
        invalidateOptionsMenu();
        
        // Here, thisActivity is the current activity
        checkMyPermission();
        
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        mScannerView.setFocusableInTouchMode(true);
        LinearLayout root = (LinearLayout) findViewById(R.id.root_lr);
        root.addView(mScannerView);

        ///delete for NDrawer
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return false;
            }
        });


    }
    
    private void checkMyPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
        
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
            
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        666);
            }
        }
    }

    private void addActionBar(){
        //add Action Bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        //add action bar
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        switch (menuItem.getItemId()) {
            case R.id.nav_settings:
                startActivity(new Intent(this, MyStatisticActivity.class));
                break;
            case R.id.nav_support:
               startActivity(new Intent(this, BoughtProducts.class));
                break;
            case R.id.nav_exit:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            case 3:
                //go_to_settings();
                break;
            default:
               // go_to_main_act();
        }
        mDrawerLayout.closeDrawer(Gravity.START);
        menuItem.setChecked(true);

    }
    
    private void add_balance() { //add balance to ref
         auth = FirebaseAuth.getInstance();
         reference = database.getReference().child("USERS").child(PATH_TO_USER).child("COINS"); //get ref

         reference.setValue(Integer.parseInt(USER_COINS)+Integer.parseInt(HOW_MUCH_ADD)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                Toast.makeText(ScannerActivity.this, "Добавлено ", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ScannerActivity.this, "Ошибка ", Toast.LENGTH_SHORT).show();

            }
        });

    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 666: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mScannerView.resumeCameraPreview(this);
        mScannerView.stopCamera();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }
    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        try {
            // Do something with the result here
            Log.v("_______________", result.getText()); // Prints scan results
            Log.v("_______________", result.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

            separated = result.getText().split(",");
            if(separated[0].equals("166734")){

            USER_COINS = separated[1];
            HOW_MUCH_ADD = separated[2];
            PATH_TO_USER = separated[3];
                //allert dialog, important
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Пополнить?");
                builder.setMessage("Пополнить " + USER_COINS + " баллов.");
                //if yes
                builder.setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add_balance();
                        addToBase(USER_COINS);
                        onResume();
                    }
                });

                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                    }
                });
                builder.show();
            }else onResume();
            // If you would like to resume scanning, call this method below:
            
        }catch (Exception e){ onResume();};
    }

    private void addToBase(final String coins) {
        try {
            auth = FirebaseAuth.getInstance();
            final String[] uName = new String[1];
            final String[] uEmail = new String[1];
            FirebaseDatabase.getInstance().getReference().child("USERS").child(PATH_TO_USER).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    uName[0] = dataSnapshot.child("FIRST_NAME").getValue().toString();
                    uEmail[0] = dataSnapshot.child("EMAIL").getValue().toString();
                    addToBaseWithTime(uName[0],uEmail[0],coins);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Toast.makeText(ScannerActivity.this, "Добавлено", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ScannerActivity.this, "Ошибка." + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private void addToBaseWithTime(String s, String d, String j){
        reference = database.getReference().child("WAITERS").child(auth.getCurrentUser().getUid()).push();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd,HH:mm");
        String formattedDate = df.format(c.getTime());
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("NAME_USER", s);
        hm.put("EMAIL", d);
        hm.put("SUM", j);
        hm.put("DATE", formattedDate);

        reference.setValue(hm);
    }
}
