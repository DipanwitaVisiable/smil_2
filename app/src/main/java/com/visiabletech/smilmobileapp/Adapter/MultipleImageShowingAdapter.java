package com.visiabletech.smilmobileapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.activity.AssignmentReplyActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MultipleImageShowingAdapter extends RecyclerView.Adapter<MultipleImageShowingAdapter.PeriodViewHolder> {

    Context context;
//    ArrayList<Uri> receiveImageList;
    Bitmap bitmap;
    public MultipleImageShowingAdapter(Context context, ArrayList<Uri> receiveImageList){
        this.context = context;
//        this.receiveImageList = receiveImageList;

    }

    @NonNull
    @Override
    public PeriodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.item_multiple_image_adapter, parent, false);
        return new PeriodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeriodViewHolder holder, final int position) {

        if (AssignmentReplyActivity.imageUriStaticList.get(position) != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), AssignmentReplyActivity.imageUriStaticList.get(position));
                holder.image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        holder.iv_cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AssignmentReplyActivity.imageUriStaticList.size()>0) {
                    AssignmentReplyActivity.imageUriStaticList.remove(position);
//                    receiveImageList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

    }

        @Override
        public int getItemCount () {
            return AssignmentReplyActivity.imageUriStaticList.size();
        }

    class PeriodViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rl_for_image;
        ImageView image, iv_cancel_image;

        public PeriodViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_for_image = itemView.findViewById(R.id.rl_for_image);
            image = itemView.findViewById(R.id.image);
            iv_cancel_image = itemView.findViewById(R.id.iv_cancel_image);
        }
    }
}
