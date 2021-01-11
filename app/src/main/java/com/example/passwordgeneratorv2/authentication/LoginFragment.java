package com.example.passwordgeneratorv2.authentication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.passwordgeneratorv2.R;
import com.example.passwordgeneratorv2.helpers.FirebaseHelper;
import com.example.passwordgeneratorv2.home.HomeView;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    private Button btnAbrirCadastro;
    private Button btnLogar;
    private Button btnForgotPassword;
    private EditText edtEmailLogin;
    private EditText edtSenhaLogin;
    private CheckBox checkKeepLogged;
    private static boolean keepLogin = true;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setFindViewbyID(view);
        setClickListener();
        FirebaseHelper.setHomeOpenned(false);
        return view;

    }

    private void setFindViewbyID(View view) {
        // Button
        btnAbrirCadastro = view.findViewById(R.id.btn_abrir_cadastro);
        btnLogar = view.findViewById(R.id.btn_logar);
        btnForgotPassword = view.findViewById(R.id.btnForgotPassword);

        //EditText
        edtEmailLogin = view.findViewById(R.id.edt_email_login);
        edtSenhaLogin = view.findViewById(R.id.edt_senha_login);
        //CheckBox
        checkKeepLogged = view.findViewById(R.id.checkKeepLogged);
    }

    private void abrirCadastroFragment() {
        CadastroFragment cadastroFragment = new CadastroFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameAuthActivity, cadastroFragment);
        transaction.commit();
    }

    private void setClickListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_abrir_cadastro:
                        abrirCadastroFragment();
                        break;
                    case R.id.btn_logar:
                        String ERROR_MSG = "Preencha ambos os campos primeiro";
                        String emailLogin = edtEmailLogin.getText().toString();
                        String senhaLogin = edtSenhaLogin.getText().toString();

                        if (emailLogin.isEmpty() || senhaLogin.isEmpty()) {
                            Toast.makeText(getContext(), ERROR_MSG, Toast.LENGTH_SHORT).show();
                        } else {
                            if (checkKeepLogged.isChecked()) {
                                Log.i("AppLog", "keepLogin marcado");
                                keepLogin = true;
                            } else {
                                Log.i("AppLog", "keepLogin desmarcado");
                                keepLogin = false;
                            }
                            FirebaseHelper.userLogin(emailLogin, senhaLogin, getActivity());
                        }
                        break;
                    case R.id.btnForgotPassword:
                        openDialog();
                        break;
                }
            }
        };
        btnAbrirCadastro.setOnClickListener(clickListener);
        btnLogar.setOnClickListener(clickListener);
        btnForgotPassword.setOnClickListener(clickListener);
    }

    private void openDialog() {
        EditText editText = new EditText(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(layoutParams);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Insira seu email para que possamos lhe mandar um link para resetar sua senha");
        builder.setPositiveButton("Enviar", (dialog, which) -> {
            String email = editText.getText().toString();
            if (!email.isEmpty()) {
                FirebaseHelper.getFirebaseAuth().sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Um email de redefinição de senha será enviado para você", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getContext(), "Preencha o campo de email primeiro", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.setView(editText);
        builder.show();
    }

    public static boolean keepLogged() {
        return keepLogin;
    }
}