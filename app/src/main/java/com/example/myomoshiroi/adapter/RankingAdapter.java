package com.example.myomoshiroi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myomoshiroi.R;
import com.example.myomoshiroi.model.UserRanking;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {
    private Context mCtx;
    private List<UserRanking> userList;
    private final int limit = 10;
    String avatarName;

    public RankingAdapter(Context mCtx, List<UserRanking> userList) {
        this.mCtx = mCtx;
        this.userList = userList;
    }
    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_ranking, parent, false);
        return new RankingViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        UserRanking user = userList.get(position);
        holder.textViewName.setText(user.fullname);
        holder.textViewPoints.setText(String.valueOf(user.getPoint())+"xp");
        avatarName = user.avatar;
        GetAvatarName(holder);
    }
    @Override
    public int getItemCount() {
        if(userList.size() > limit){
            return limit;
        }
        else
        {
            return userList.size();
        }
    }
    class RankingViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewPoints;
        ImageView profileAvatar;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textView_Name);
            textViewPoints = itemView.findViewById(R.id.textView_Points);
            //textViewRank = itemView.findViewById(R.id.textView_Level);
            profileAvatar = itemView.findViewById(R.id.profileAvatar);
        }
    }
    public void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
    }
    private void GetAvatarName(@NonNull RankingViewHolder holder){
        switch (avatarName) {
            case "avatar_bulb": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_bulb);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cactus": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_cactus);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_chick": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_chick);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cup": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_cup);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_hat": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_hat);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_pinaple": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_pinaple);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_android": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_android);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_cliprobot": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_cliprobot);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_head": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_head);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_monitor": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_monitor);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_robot": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_robot);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            case "avatar_skull": {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.avatar_skull);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
            default: {
                InputStream imageStream = holder.profileAvatar.getResources().openRawResource(R.raw.profile);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                holder.profileAvatar.setImageBitmap(bitmap);
                break;
            }
        }
    }
}
