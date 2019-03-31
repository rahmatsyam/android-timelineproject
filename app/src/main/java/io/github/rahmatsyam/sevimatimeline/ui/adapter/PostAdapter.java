package io.github.rahmatsyam.sevimatimeline.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.github.rahmatsyam.sevimatimeline.R;
import io.github.rahmatsyam.sevimatimeline.data.model.PostItem;
import io.github.rahmatsyam.sevimatimeline.data.provider.DatabaseHelper;
import io.github.rahmatsyam.sevimatimeline.ui.activity.EditPostActivity;
import io.github.rahmatsyam.sevimatimeline.ui.activity.MainActivity;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mContext;
    private List<PostItem> postItems;


    private DatabaseHelper db;


    private static final CharSequence[] items = {"Edit", "Delete"};
    private int choice;

    public PostAdapter(Context context, List<PostItem> postItemList) {
        this.mContext = context;
        this.postItems = postItemList;

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_row_post, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder viewHolder, final int position) {

        final PostItem postItem = postItems.get(position);
        db = new DatabaseHelper(mContext);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        Glide.with(mContext)
                .load(user.getPhotoUrl())
                .into(viewHolder.ivProfile);

        viewHolder.tvStatus.setText(postItem.getStatus());
        viewHolder.tvNameProfile.setText(user.getDisplayName());
        viewHolder.tvDate.setText(postItem.getDateSatus());

        viewHolder.btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                final int checkedItem = -1;
                builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (items[which] == "Edit") {
                            choice = 1;
                        } else {
                            choice = 2;
                        }

                    }
                });

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (choice) {
                            case 1:
                                Intent intent = new Intent(mContext, EditPostActivity.class);
                                intent.putExtra("ID", postItem.getId());
                                mContext.startActivity(intent);
                                break;
                            case 2:
                                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("Delete this post?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                db.deleteData(postItem.getId());
                                                dialog.dismiss();
                                                notifyDataSetChanged();
                                                Toasty.info(mContext, "Post Deleted", Toast.LENGTH_SHORT).show();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                       mContext.startActivity(new Intent(mContext, MainActivity.class));
                                                    }
                                                },1200);
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
                                break;
                            default:
                                dialog.dismiss();
                                break;
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }


    class PostViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivProfile;
        ImageButton btSetting;
        TextView tvNameProfile;
        TextView tvStatus;
        TextView tvDate;

        PostViewHolder(View view) {
            super(view);
            ivProfile = view.findViewById(R.id.iv_profile);
            tvNameProfile = view.findViewById(R.id.tv_namepro);
            tvStatus = view.findViewById(R.id.tv_status);
            tvDate = view.findViewById(R.id.tv_date);
            btSetting = view.findViewById(R.id.btn_setting);
        }


    }


    @Override
    public int getItemCount() {
        return postItems.size();
    }

}
