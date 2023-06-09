package com.app.amstfirebaseappdesafio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PerfilUsuario extends AppCompatActivity {
    TextView txt_id, txt_name, txt_email, txt_phone;
    ImageView imv_photo;
    DatabaseReference db_reference;

    EditText editTextTweet;

    String tweet;
    String autor;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String currentDate = dateFormat.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        Intent intent = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            int statusBarColor = ContextCompat.getColor(this, com.facebook.login.R.color.com_facebook_button_background_color);
            window.setStatusBarColor(statusBarColor);
        }

        HashMap<String, String> info_user = (HashMap<String, String>) intent.getSerializableExtra("info_user");
        System.out.println("Informacion" + info_user);
        txt_id = findViewById(R.id.txt_userId);
        txt_name = findViewById(R.id.txt_nombre);
        txt_email = findViewById(R.id.txt_correo);
        imv_photo = findViewById(R.id.imv_foto);
        txt_phone = findViewById(R.id.txt_phone);
        editTextTweet = findViewById(R.id.editTexttweet);

        txt_id.setText(info_user.get("user_id"));
        txt_name.setText(info_user.get("user_name"));
        txt_email.setText(info_user.get("user_email"));
        txt_phone.setText(info_user.get("user_phone"));
        String photo = info_user.get("user_photo");
        Picasso.get().load(photo).into(imv_photo);

        autor = info_user.get("user_name");

        iniciarBaseDeDatos();
        leerTweets();
    }

    public void enviarTweet(View view){
        if(editTextTweet.getText().toString() == null){
            Toast.makeText(getApplicationContext(), "No ha escrito ningun tweet", Toast.LENGTH_SHORT).show();
        }
        else{
            tweet = editTextTweet.getText().toString();
            escribirTweets(autor,tweet);
        }
    }

    public void iniciarBaseDeDatos() {
        db_reference = FirebaseDatabase.getInstance().getReference().child("Grupos");
    }
    public void leerTweets() {
        db_reference.child("Grupo1").child("tweets")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            System.out.println(snapshot);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.out.println(error.toException());
                    }
                });
    }

    public void escribirTweets(String autor, String tweetmsg){
        String tweet = tweetmsg;
        Map<String, String> hola_tweet = new HashMap<String, String>();
        hola_tweet.put("autor", autor);
        hola_tweet.put("fecha", currentDate);
        DatabaseReference tweets = db_reference.child("Grupo4").child("tweets");
        tweets.setValue(tweet);
        tweets.child(tweet).child("autor").setValue(autor);
        tweets.child(tweet).child("fecha").setValue(currentDate);
    }

    public void cerrarSesion(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("msg", "cerrarSesion");
        startActivity(intent);
    }

}
