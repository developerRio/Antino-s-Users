package com.originalstocks.antinomctask.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.originalstocks.antinomctask.R;
import com.originalstocks.antinomctask.model.UsersModel;

import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {

    private Context mContext;
    private List<UsersModel> usersList;

    public UserRecyclerAdapter(Context mContext, List<UsersModel> usersList) {
        this.mContext = mContext;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.users_list_item, null);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {

        UsersModel usersModel = usersList.get(position);

        holder.userAgeTextView.setText(usersModel.getUserAge());
        holder.userNameTextView.setText(usersModel.getUserName());
        holder.userLocationTextView.setText(usersModel.getUserLocation());

        // setting up image via Glide
        Glide.with(mContext)
                .load(usersModel.getUserImageLink())
                .placeholder(R.drawable.m_one_grade)
                .into(holder.userImageView);

        holder.userCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked on position = " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CircularImageView userImageView;
        TextView userNameTextView, userAgeTextView, userLocationTextView;
        MaterialCardView userCardView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userImageView = itemView.findViewById(R.id.user_image_view);
            userNameTextView = itemView.findViewById(R.id.user_name_text_view);
            userAgeTextView = itemView.findViewById(R.id.age_text_view);
            userLocationTextView = itemView.findViewById(R.id.location_text_view);
            userCardView = itemView.findViewById(R.id.user_card_view);

        }
    }

}
