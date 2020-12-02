package com.antoniuswicaksana.project_pbp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button signUp,signin;
    private EditText email_ed,pass_ed;
    private static String CHANNEL_ID = "Channel 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        signin=findViewById(R.id.btn_signIn);
        signUp=findViewById(R.id.btn_signUp);
        email_ed=findViewById(R.id.edit_email);
        pass_ed=findViewById(R.id.edit_pass);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=email_ed.getText().toString();
                String pass=pass_ed.getText().toString();
                if(email.isEmpty() || !email.contains("@")){
                    Toast.makeText(login.this,"Email invalid",Toast.LENGTH_SHORT).show();
                }else if(pass.isEmpty()){
                    Toast.makeText(login.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                }else if(pass.length()<6){
                    Toast.makeText(login.this,"Password to short",Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(login.this,"Sign Up Unsuccessful",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(login.this,"Sign up Success",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=email_ed.getText().toString();
                String pass=pass_ed.getText().toString();
                if(email.isEmpty() || !email.contains("@")){
                    Toast.makeText(login.this,"Email invalid",Toast.LENGTH_SHORT).show();
                }else if(pass.isEmpty()){
                    Toast.makeText(login.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                }else if(pass.length()<6){
                    Toast.makeText(login.this,"Password to short",Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //Log.d(TAG, "signInWithEmail:success");
                                        //FirebaseUser user = mAuth.getCurrentUser();
                                        //updateUI(user);
                                        Toast.makeText(login.this,"Login Succsessful",Toast.LENGTH_LONG).show();
                                        createNotificationChannel();
                                        addNotification();
                                        Intent intent = new Intent(login.this,welcome.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });

    }
    private void createNotificationChannel () {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name ="Channel 1";
            String description ="This is Channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, login.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Hello")
                .setContentText("Selamat anda berhasil Sign In!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notificationIntent = new Intent( this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivities(this, 0, new Intent[]{notificationIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }

}
