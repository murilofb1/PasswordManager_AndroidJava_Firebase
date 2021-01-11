package com.example.passwordgeneratorv2.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import android.os.Build;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.security.identity.EphemeralPublicKeyNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.passwordgeneratorv2.R;
import com.example.passwordgeneratorv2.helpers.ArrayHelper;
import com.example.passwordgeneratorv2.helpers.Base64H;
import com.example.passwordgeneratorv2.helpers.FirebaseHelper;
import com.example.passwordgeneratorv2.home.HomeView;
import com.example.passwordgeneratorv2.home.HomeViewModel;
import com.example.passwordgeneratorv2.models.Password;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.passwordgeneratorv2.R.*;

public class AdapterPasswords extends RecyclerView.Adapter<AdapterPasswords.MyViewHolder> {
    private List<Password> passwordList;
    private boolean[] visiblePassword;
    private OnRecyclerItemClick recyclerItemClickListener;
    private static boolean unlocked = false;
    private Toast toast = null;


    public AdapterPasswords(List<Password> passwordList, OnRecyclerItemClick clickListener) {
        this.passwordList = passwordList;
        this.recyclerItemClickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout.recycler_senhas_layout, parent, false);
        return new MyViewHolder(view, recyclerItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!unlocked) {
            changeAllVisibility(false);
        }

        Password siteAtual = passwordList.get(position);
        Context viewContext = holder.itemView.getContext();


        boolean visible = visiblePassword[position];

        if (visible) {
            holder.txtPassword.setText(Base64H.decode(siteAtual.getPassword()));
            holder.imgBtnShowHidePassword.setImageResource(drawable.ic_password_invisible);
        } else {
            holder.txtPassword.setText(maskedPassword(Base64H.decode(siteAtual.getPassword())));
            holder.imgBtnShowHidePassword.setImageResource(drawable.ic_password_visibility);

        }

        if (siteAtual.isFavorite()) {
            holder.checkfav.setImageResource(drawable.ic_favorite);
        } else {
            holder.checkfav.setImageResource(drawable.ic_not_favorite);
        }


        holder.txtSiteName.setText(siteAtual.getSite());

        if (siteAtual.getIconLink().equals("")) {
            Glide.with(holder.itemView).load(drawable.default_image).into(holder.imgSiteIcon);
        } else {
            Glide.with(holder.itemView).load(siteAtual.getIconLink()).into(holder.imgSiteIcon);
        }

        holder.imgBtnShowHidePassword.setOnClickListener(clickListenerHandler(viewContext, null, position));
        holder.imgBtnCopyPassword.setOnClickListener(clickListenerHandler(viewContext, siteAtual.getPassword(), position));
        holder.checkfav.setOnClickListener(clickListenerHandler(viewContext, null, position));
    }

    public static boolean isUnlocked() {
        return unlocked;
    }

    public static void setUnlocked(boolean newValue) {
        unlocked = newValue;
    }

    public static void copyPassword(String password, Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData coppiedData = ClipData.newPlainText("password", password);
        manager.setPrimaryClip(coppiedData);
    }

    public void updateVisibleArray(int newSize) {
        visiblePassword = new boolean[newSize];
        visiblePassword = ArrayHelper.setAll(visiblePassword, false);

    }


    private View.OnClickListener clickListenerHandler(@NotNull Context context, @Nullable String password, @Nullable Integer position) {
        View.OnClickListener listener = v -> {
            if (unlocked) {
                if (v.getId() == id.imgBtnShowHidePassword) {
                    changeVisibility(position);
                    this.notifyDataSetChanged();
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                    }
                } else if (v.getId() == id.imgBtnCopyPassword) {
                    AdapterPasswords.this.copyPassword(password, context);
                    Password actualPassword = passwordList.get(position);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                    }
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(context, "Your " + actualPassword.getSite() + " password was copied to clipboard", Toast.LENGTH_SHORT);
                    toast.show();

                } else if (v.getId() == id.checkfav) {

                    Password senha = passwordList.get(position);
                    boolean newValue;
                    if (senha.isFavorite()) {
                        newValue = false;
                    } else {
                        newValue = true;
                    }
                    FirebaseHelper.getUserPasswordsReference()
                            .child(senha.getSite())
                            .child("favorite")
                            .setValue(newValue);
                }

            } else {
                HomeView.openBiometricAuth();

            }


        };
        return listener;
    }


    public static String maskedPassword(String password) {
        String masked = "";
        for (int i = 0; i < password.length(); i++) {
            masked += "*";
        }
        return masked;
    }

    private void changeVisibility(int positiion) {
        if (visiblePassword[positiion]) {
            visiblePassword[positiion] = false;
            Log.i("AdapterStatus", "Item " + positiion + ": false");
        } else {
            visiblePassword[positiion] = true;
            Log.i("AdapterStatus", "Item " + positiion + ": true");
        }
    }


    private void changeAllVisibility(boolean value) {
        for (int i = 0; i < visiblePassword.length; i++) {
            visiblePassword[i] = value;
        }
    }


    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txtSiteName;
        TextView txtPassword;
        ImageButton imgBtnCopyPassword;
        ImageButton imgBtnShowHidePassword;
        ImageView imgSiteIcon;
        ImageView checkfav;

        OnRecyclerItemClick recyclerItemClick;

        public MyViewHolder(@NonNull View itemView, OnRecyclerItemClick recyclerItemClick) {
            super(itemView);
            txtSiteName = itemView.findViewById(id.txt_name_recycler);
            txtPassword = itemView.findViewById(id.txt_password_recycler);
            imgBtnCopyPassword = itemView.findViewById(id.imgBtnCopyPassword);
            imgBtnShowHidePassword = itemView.findViewById(id.imgBtnShowHidePassword);
            imgSiteIcon = itemView.findViewById(id.img_icon_recycler);
            checkfav = itemView.findViewById(id.checkfav);

            this.recyclerItemClick = recyclerItemClick;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            recyclerItemClick.onItemClick(getAdapterPosition());
        }


        @Override
        public boolean onLongClick(View v) {
            recyclerItemClick.onLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnRecyclerItemClick {
        void onItemClick(int position);

        void onLongClick(int position);
    }

}
