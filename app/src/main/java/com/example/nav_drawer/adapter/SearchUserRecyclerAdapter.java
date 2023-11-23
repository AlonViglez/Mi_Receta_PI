package com.example.nav_drawer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nav_drawer.AndroidUtil;
import com.example.nav_drawer.ChatroomModel;
import com.example.nav_drawer.R;
import com.example.nav_drawer.UserModel;
import com.example.nav_drawer.viewdoc.Activity_Chat;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelviewHolder> {

    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, FirestoreRecyclerOptions<ChatroomModel> chatroomOptions, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelviewHolder holder, int position, @NonNull UserModel model) {
        holder.usernameText.setText(model.getNombre());
        /**if (model.getId().equals(FirebaseUtil.currentUserId())){
            holder.usernameText.setText(model.getNombre()+ "(Yo)");
        }*/

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Activity_Chat.class);
            AndroidUtil.passUserModelAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }


    @NonNull
    @Override
    public UserModelviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserModelviewHolder(view);
    }

    class UserModelviewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        ImageView profilepic;

        public UserModelviewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            profilepic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
