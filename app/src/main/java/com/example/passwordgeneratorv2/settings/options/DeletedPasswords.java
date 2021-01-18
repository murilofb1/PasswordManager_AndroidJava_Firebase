package com.example.passwordgeneratorv2.settings.options;


import android.app.VoiceInteractor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwordgeneratorv2.R;
import com.example.passwordgeneratorv2.adapters.AdapterDeletedPasswords;
import com.example.passwordgeneratorv2.adapters.AdapterPasswords;
import com.example.passwordgeneratorv2.helpers.FirebaseHelper;
import com.example.passwordgeneratorv2.models.Password;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;


public class DeletedPasswords extends AppCompatActivity implements Observer {
    private RecyclerView recyclerDeletedPasswords;
    private DeletedPasswordsHelper deletedHelper;
    private AdapterDeletedPasswords adapterPasswords;
    private MenuItem menuLockUnlock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_passwords);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deletedHelper = new DeletedPasswordsHelper();
        deletedHelper.addObserver(this);
        deletedHelper.loadList();
        //Configuring Recycler
        recyclerDeletedPasswords = findViewById(R.id.recyclerDeletedPasswords);
        recyclerDeletedPasswords.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerDeletedPasswords.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapterPasswords = new AdapterDeletedPasswords(deletedHelper.getPasswordList());
        recyclerDeletedPasswords.setAdapter(adapterPasswords);
    }

    @Override
    protected void onStop() {
        deletedHelper.removeListener();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_passwords_fragments, menu);
        menu.findItem(R.id.menuSettings).setVisible(false);
        menuLockUnlock = menu.findItem(R.id.menuLockUnlock);
        if (AdapterPasswords.isUnlocked()) {
            menuLockUnlock.setIcon(R.drawable.ic_open_padlock);
        }
        menuLockUnlock.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menuLockUnlock) {
            if (AdapterPasswords.isUnlocked()) {
                lockPasswords();
            } else {
                unlockPasswords();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals(DeletedPasswordsHelper.ARG_LIST_UPDATE)) {
            adapterPasswords.notifyDataSetChanged();
        }
    }

    public void lockPasswords() {
        AdapterPasswords.setUnlocked(false);
        menuLockUnlock.setIcon(R.drawable.ic_padlock);
        adapterPasswords.notifyDataSetChanged();
    }

    public void unlockPasswords() {
        BiometricPrompt biometricPrompt;
        BiometricPrompt.PromptInfo promptInfo;
        Executor executor = getMainExecutor();

        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                AdapterPasswords.setUnlocked(true);
                menuLockUnlock.setIcon(R.drawable.ic_open_padlock);
                adapterPasswords.notifyDataSetChanged();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock the app")
                .setDescription("Unlock the App to manage your passwords")
                .setDeviceCredentialAllowed(true)
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

}

class DeletedPasswordsHelper extends Observable {
    private List<Password> passwordList = new ArrayList<>();
    private DatabaseReference deletedReference = FirebaseHelper.getUserDatabaseReference().child("deletedPasswords");
    private ValueEventListener listenerDeletedPasswords;
    public static int ARG_LIST_UPDATE = 0;

    public List<Password> getPasswordList() {
        return passwordList;
    }

    public void loadList() {
        listenerDeletedPasswords = deletedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                passwordList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    passwordList.add(item.getValue(Password.class));
                }
                Log.i("DeletedPasswords", "loaded");
                Log.i("DeletedPasswords", "ListSize = " + passwordList.size());
                notifyUpdates(ARG_LIST_UPDATE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeListener() {
        deletedReference.removeEventListener(listenerDeletedPasswords);
    }


    private void notifyUpdates(Object arg) {
        setChanged();
        notifyObservers(arg);
    }


}