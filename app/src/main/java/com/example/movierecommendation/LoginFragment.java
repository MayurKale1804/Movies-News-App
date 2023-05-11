package com.example.movierecommendation;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class LoginFragment extends Fragment {
    DBhandler handler;
    private Switch remember;
    String user_Name, pass_word;
    private Button registerBtn, loginBtn;
    private EditText userName, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        handler = new DBhandler(getActivity());

        findIds(view);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterFragment()).addToBackStack(null) .commit();
            }
        });

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    setSharedPreferencesTrue();
                }
                else {
                    setSharedPreferencesFalse();
                }
            }
        });
        return view;
    }

    public void findIds(View view) {
        loginBtn    = view.findViewById(R.id.loginBtn);
        userName = view.findViewById(R.id.userName);
        password = view.findViewById(R.id.password);
        registerBtn = view.findViewById(R.id.registerBtn);
        remember = view.findViewById(R.id.rememberSwitch);
    }

    public void login() {
        user_Name = userName.getText().toString();
        pass_word = password.getText().toString();

        if(handler.confirmUser(user_Name, pass_word) == true) {
            Intent intent = new Intent(getActivity(), MoviesActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getActivity(), "You are not Registered!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setSharedPreferencesTrue() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedIn", true);
        editor.putString("user", user_Name);
        editor.apply();
    }

    public void setSharedPreferencesFalse() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedIn", false);
        editor.apply();
    }
}