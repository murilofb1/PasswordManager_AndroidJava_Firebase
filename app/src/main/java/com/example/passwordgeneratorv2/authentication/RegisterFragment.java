package com.example.passwordgeneratorv2.authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwordgeneratorv2.R;
import com.example.passwordgeneratorv2.helpers.Base64H;
import com.example.passwordgeneratorv2.helpers.FirebaseHelper;
import com.example.passwordgeneratorv2.models.UserModel;


public class RegisterFragment extends Fragment {
    private Button btnAbrirLogin;
    private Button btnSignUp;
    private EditText edtNewUserName;
    private EditText edtNewUserEmail;
    private EditText edtNewUserPassword;
    private EditText edtNewUserPassword2;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadastro, container, false);
        setFindViewbyID(view);
        setClickListener();
        return view;
    }

    private void setFindViewbyID(View view) {
        //Button
        btnAbrirLogin = view.findViewById(R.id.btn_abrir_login);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        //EditText
        edtNewUserName = view.findViewById(R.id.edtNewUserName);
        edtNewUserEmail = view.findViewById(R.id.edtNewUserEmail);
        edtNewUserPassword = view.findViewById(R.id.edtNewUserPassword);
        edtNewUserPassword2 = view.findViewById(R.id.edtNewUserPassword2);
    }

    private void abrirLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameAuthActivity, loginFragment);
        transaction.commit();
    }

    private void setClickListener() {
        View.OnClickListener clickListener = v -> {
            if (v.getId() == R.id.btn_abrir_login) {
                abrirLoginFragment();
            } else if (v.getId() == R.id.btnSignUp) {
                String strNewUserName = edtNewUserName.getText().toString();
                String strNewUserEmail = edtNewUserEmail.getText().toString();
                String strNewUserPassword = edtNewUserPassword.getText().toString();
                String strNewUserPassword2 = edtNewUserPassword2.getText().toString();
                if (!strNewUserName.isEmpty() && !strNewUserEmail.isEmpty() && !strNewUserPassword.isEmpty() && !strNewUserPassword2.isEmpty()) {
                    if (strNewUserPassword.equals(strNewUserPassword2)) {
                        strNewUserPassword = Base64H.encode(strNewUserPassword);
                        UserModel userModel = new UserModel(strNewUserName, strNewUserEmail, strNewUserPassword);
                        FirebaseHelper.registerUser(userModel, this.getActivity());
                    } else {
                        Toast.makeText(getActivity(), "The passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Fill all the fields first", Toast.LENGTH_SHORT).show();
                }
            }

        };
        btnAbrirLogin.setOnClickListener(clickListener);
        btnSignUp.setOnClickListener(clickListener);
    }
}