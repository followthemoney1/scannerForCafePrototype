package com.leaditteam.qrscanner.activities;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leaditteam.qrscanner.GetDataFromFirebase;
import com.leaditteam.qrscanner.R;

public class LoginActivity extends AppCompatActivity {
    
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference;
    private FirebaseAuth auth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        final EditText email = (EditText) findViewById(R.id.log_in_email);
        final EditText pass = (EditText) findViewById(R.id.log_in_pass);
        TextView login = (TextView) findViewById(R.id.textView_login_autorizationFragment);
        final View circle = (View) findViewById(R.id.circle);
        final CardView card = (CardView) findViewById(R.id.card_v);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(circle, card);
                login(email.getText().toString(), pass.getText().toString());
            }
        });
    }
    
    private void login(String login, String pass) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(login, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Логин успешен", Toast.LENGTH_LONG).show();
                    GetDataFromFirebase.getInstance(LoginActivity.this);
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        
    }
    
    private void animation(final View view, CardView card) {
        
        int cx = card.getMeasuredWidth();
        int cy = card.getMeasuredHeight();
        
        // get the final radius for the clipping circle
        int finalRadius = Math.max(card.getWidth(), card.getHeight()) / 2;
        view.animate().scaleYBy(cy).scaleXBy(cx).setDuration(900).alpha(0f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setAlpha(0.3f);
                view.setVisibility(View.VISIBLE);
                
            }
            
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setScaleX(1);
                view.setScaleY(1);
                view.setAlpha(0.5f);
                view.setVisibility(View.INVISIBLE);
            }
            
            @Override
            public void onAnimationCancel(Animator animation) {
                
            }
            
            @Override
            public void onAnimationRepeat(Animator animation) {
                
            }
        }).start();
        
    }
}
