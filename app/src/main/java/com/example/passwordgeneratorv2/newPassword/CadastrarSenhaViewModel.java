package com.example.passwordgeneratorv2.newPassword;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.passwordgeneratorv2.helpers.FirebaseHelper;
import com.example.passwordgeneratorv2.models.Password;
import com.example.passwordgeneratorv2.models.WebsiteModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Observable;


public class CadastrarSenhaViewModel extends Observable {
    private ArrayList<Password> spinnerPasswordList = new ArrayList<>();
    private ArrayList<WebsiteModel> modelsList = new ArrayList<>();
    private ValueEventListener spinnerEventListener;
    private static DatabaseReference spinnerItensReference;
    private static Query spinnerItensQuery;
    public static final String SPINNER_LIST_ARG = "slArg";
    private static boolean isLoaded = false;

    public ArrayList<WebsiteModel> getSpinnerPasswordList() {
        if (spinnerEventListener == null) {
            //spinnerItensReference = FirebaseHelper.getUserIconsReference();
            spinnerItensQuery = FirebaseHelper.getUserIconsReference().orderByChild("beingUsed").equalTo(false);
            spinnerEventListener = spinnerItensQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() == null) {
                        Log.i("spinnerStatus","null");
                        if (!isLoaded) {
                            FirebaseHelper.loadDefaultIcons();
                            isLoaded = true;
                        }
                    } else {
                        Log.i("spinnerStatus","notNull");
                        isLoaded = true;
                        modelsList.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            modelsList.add(item.getValue(WebsiteModel.class));
                        }
                        modelsList.add(new WebsiteModel("New item", "", ""));
                    }

                    notifyChange(SPINNER_LIST_ARG);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //return spinnerPasswordList;
        return modelsList;
    }


    private void notifyChange(String arg) {
        setChanged();
        notifyObservers(arg);
    }

}

