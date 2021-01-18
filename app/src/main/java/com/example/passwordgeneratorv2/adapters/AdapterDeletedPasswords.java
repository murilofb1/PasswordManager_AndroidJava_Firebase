package com.example.passwordgeneratorv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.passwordgeneratorv2.R;
import com.example.passwordgeneratorv2.helpers.Base64H;
import com.example.passwordgeneratorv2.models.Password;

import java.util.List;

public class AdapterDeletedPasswords extends RecyclerView.Adapter<AdapterDeletedPasswords.DeletedViewHolder> {
    private List<Password> passwordList;

    public AdapterDeletedPasswords(List<Password> passwordList) {
        this.passwordList = passwordList;
    }

    @NonNull
    @Override
    public DeletedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_deleted_passwords, parent, false);
        return new DeletedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletedViewHolder holder, int position) {
        Password password = passwordList.get(position);
        holder.textSitename.setText(password.getSite());
        if (AdapterPasswords.isUnlocked()) {
            holder.textPassword.setText(Base64H.decode(password.getPassword()));
        } else {
            String maskedpassword = AdapterPasswords.maskedPassword(Base64H.decode(password.getPassword()));
            holder.textPassword.setText(maskedpassword);
        }

        Glide.with(holder.itemView)
                .load(password.getIconLink())
                .placeholder(R.drawable.default_image)
                .into(holder.imgSiteIcon);
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public class DeletedViewHolder extends RecyclerView.ViewHolder {
        TextView textSitename;
        TextView textPassword;
        ImageView imgSiteIcon;

        public DeletedViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSiteIcon = itemView.findViewById(R.id.imgRecyclerDeletedIcon);
            textSitename = itemView.findViewById(R.id.txtRecyclerDeletedName);
            textPassword = itemView.findViewById(R.id.txtRecyclerDeletedPassword);
        }
    }
}
