package com.example.form.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.form.DownloadImageTask;
import com.example.form.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.form.model.DisasterModel;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    List<DisasterModel> disasterList;
    Context context;
    private static ClickListener clickListener;

    public CustomAdapter(Context context, List<DisasterModel> disasterList){
        this.context=context;
        this.disasterList =disasterList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageview;
        TextView title,desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageview=itemView.findViewById(R.id.imageview);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
        }
        public void setImageview(String imageUrl) {
            new DownloadImageTask((ImageView) imageview)
                    .execute(imageUrl);
        }
        public void setTitle(String title) {
            this.title.setText(title);
        }
        public void setDesc(String desc) {
            this.desc.setText(desc);
        }
        @Override
        public void onClick(View view) {
            try {
                clickListener.onItemClick(getAdapterPosition(), view);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View listViewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.disaster_card_view, null);
        return new MyViewHolder(listViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DisasterModel disasterModel = disasterList.get(position);
        holder.setImageview(disasterModel.getImage());
        holder.setTitle(disasterModel.getTitle());
        holder.setDesc(disasterModel.getDesc());
    }

    @Override
    public int getItemCount() {
        return disasterList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CustomAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(int position, View v) throws Exception;
    }

}
