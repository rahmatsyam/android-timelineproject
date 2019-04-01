package io.github.rahmatsyam.sevimatimeline.ui.activity;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
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
import io.github.rahmatsyam.sevimatimeline.data.model.PostItem;
import io.github.rahmatsyam.sevimatimeline.data.provider.DatabaseHelper;

public class EditPostActivity extends AppCompatActivity {

    DatabaseHelper db;
    FirebaseAuth auth;

    PostItem postItem;

    // LinearLayout llkeyboard;
    CircleImageView circleProfile;
    TextView tvName, tvDate;
    EditText etFormEdit;
    Button btnPost;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_edit_post);

        auth = FirebaseAuth.getInstance();

        circleProfile = findViewById(R.id.circle_profile);
        tvName = findViewById(R.id.tv_name_profile);
        tvDate = findViewById(R.id.tv_datetime);
        etFormEdit = findViewById(R.id.et_form_edit_post);
        btnPost = findViewById(R.id.btn_editpost);
        //llkeyboard = findViewById(R.id.ll_form_editpost);


        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(circleProfile);
        tvName.setText(user.getDisplayName());

       /* llkeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) EditPostActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            }
        });*/
        bundle = getIntent().getExtras();
        if (bundle != null) {
            postItem = db.getData(bundle.getInt("ID"));
            etFormEdit.setText(postItem.getStatus());
            tvDate.setText(postItem.getDateSatus());
            final String dateStatus = new SimpleDateFormat("d/M/yyyy - HH:mm", Locale.getDefault()).format(new Date());

            btnPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.editData(postItem.getId(), etFormEdit.getText().toString(), dateStatus);
                    Toasty.success(getApplicationContext(), "Update post", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1200);
                }
            });


        }


    }
}
