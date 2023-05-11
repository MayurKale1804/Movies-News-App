package com.example.movierecommendation;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    DBhandler handler;
    private Button registerBtn1;
    String userName, password, confirmPassword;
    private EditText userName1, password1, confirmpassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        findIds(view);

        registerBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = userName1.getText().toString();
                password = password1.getText().toString();
                confirmPassword = confirmpassword.getText().toString();

                if(userName.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
                else {
                    registrationProcess();
                }
            }
        });
        return view;
    }

    public void findIds(View view) {
        handler = new DBhandler(getContext());
        userName1 = view.findViewById(R.id.userName1);
        password1 = view.findViewById(R.id.password1);
        registerBtn1 = view.findViewById(R.id.registerBtn1);
        confirmpassword = view.findViewById(R.id.confirmpassword);
    }

    public void setSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedIn", true);
        editor.apply();
    }

    public void registrationProcess() {

        if(password.equals(confirmPassword)) {
            boolean checkUser = handler.checkUserName(userName);
            if(checkUser == false) {

                boolean insert = handler.insertData(userName, password);

                if(insert == true) {
                    setSharedPreferences();
                    Toast.makeText(getActivity(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null) .commit();

                }
                else {
                    Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getActivity(), "User Name already exists", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_SHORT).show();
        }
    }
}
