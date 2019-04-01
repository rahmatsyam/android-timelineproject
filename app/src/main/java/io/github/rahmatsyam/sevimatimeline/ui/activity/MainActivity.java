package io.github.rahmatsyam.sevimatimeline.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.rahmatsyam.sevimatimeline.R;
import io.github.rahmatsyam.sevimatimeline.data.model.PostItem;
import io.github.rahmatsyam.sevimatimeline.data.provider.DatabaseHelper;
import io.github.rahmatsyam.sevimatimeline.ui.adapter.PostAdapter;
import io.github.rahmatsyam.sevimatimeline.ui.util.EmptyRecyclerView;

public class MainActivity extends AppCompatActivity {

    //RecyclerView recyclerView;
    EmptyRecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<PostItem> list = new ArrayList<>();
    DatabaseHelper db;

    CircleImageView circleProfil;
    TextView tvName, tvEmail;
    FirebaseAuth auth;
    CardView cardPost;
    ImageButton btSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        circleProfil = findViewById(R.id.img_profil);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        cardPost = findViewById(R.id.card_post);
        btSignOut = findViewById(R.id.btn_sign_out);

        recyclerView = findViewById(R.id.recylerview);


        FirebaseUser user = auth.getCurrentUser();

        assert user != null;
        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(circleProfil);

        tvName.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());

        list = db.getAll();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(findViewById(R.id.empty_view));

        cardPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentPost();
            }
        });

        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want sign out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                signOut();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        });

    }

    private void intentPost() {
        startActivity(new Intent(this, PostActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = db.getAll();
        adapter = new PostAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(findViewById(R.id.empty_view));

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (auth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();

    }
}
