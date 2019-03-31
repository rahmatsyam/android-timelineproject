package io.github.rahmatsyam.sevimatimeline.ui.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.github.rahmatsyam.sevimatimeline.R;
import io.github.rahmatsyam.sevimatimeline.data.provider.DatabaseHelper;

public class PostActivity extends AppCompatActivity {

    FirebaseAuth auth;

    private SQLiteDatabase db = null;
    DatabaseHelper dbHelper = null;
    CircleImageView circleProfile;
    TextView tvNameProfile;
    EditText etFormPost;
    Button btnPost;
    LinearLayout llCallKeyboard;


    private static final String STATUS = "status";
    private static final String DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        setContentView(R.layout.activity_post);

        auth = FirebaseAuth.getInstance();


        circleProfile = findViewById(R.id.circle_profile);
        tvNameProfile = findViewById(R.id.tv_name_profile);
        etFormPost = findViewById(R.id.et_form_input_post);
        btnPost = findViewById(R.id.btn_post);
        llCallKeyboard = findViewById(R.id.ll_form_post);

        FirebaseUser user = auth.getCurrentUser();

        assert user != null;
        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(circleProfile);
        tvNameProfile.setText(user.getDisplayName());


       /* llCallKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) PostActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            }
        });*/

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postStatus();
            }
        });
    }

    private void postStatus() {


        String status = etFormPost.getText().toString();
        String dateStatus = new SimpleDateFormat("d/M/yyyy - HH:mm", Locale.getDefault()).format(new Date());


        if (status.equals("")) {
            Toasty.info(getApplicationContext(), "Please insert status before", Toast.LENGTH_SHORT).show();
        } else {

            ContentValues cv = new ContentValues();

            cv.put(STATUS, status);
            cv.put(DATE, dateStatus);

            if (db.insert("timeline", STATUS, cv) > 0 || db.insert("timeline", DATE, cv) > 0) {
                Toasty.success(getApplicationContext(), "Success post", Toast.LENGTH_SHORT).show();
                etFormPost.getText().clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1200);
            } else {
                Toasty.info(getApplicationContext(), "Invalid post", Toast.LENGTH_SHORT).show();

            }
        }


    }
}
