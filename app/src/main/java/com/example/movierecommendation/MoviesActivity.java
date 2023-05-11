package com.example.movierecommendation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity implements movieAdaptor.onclicklisten {

    ArrayList<Movies> movie = new ArrayList<>();
    ArrayList<Movies> recommendationList = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressDialog spinner;
    private movieAdaptor mAdaptar;

    private Button logOut, recommend;
    int r = 0;
    String string, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        logOut = findViewById(R.id.logout);
        recommend = findViewById(R.id.switcher);

        setRecyclerView();

        setSpinner();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSharedPreferences();
                Intent intent = new Intent(MoviesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences s = getSharedPreferences("loginStatus", 0);
        user = s.getString("user", "");

        fetchData();

        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r = 1;

                SharedPreferences sp = getSharedPreferences("d",0);
                String s=sp.getString(user,"88");

                for (int i = 0; i < movie.size(); i++){
                    if(movie.get(i).Categ.equals(s)){
                        recommendationList.add(movie.get(i));
                    }
                }

                mAdaptar.updateList(recommendationList,1);
            }
        });

    }

    private void fetchData() {
        spinner.show();
        String[] urls = new String[]{"https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.filmfestivals.com%2Fchannel%2Ffilm%2Fanimation%2Ffeed", "https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.filmfestivals.com%2Fchannel%2Ffilm%2Fhollywood%2Ffeed", "https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.filmfestivals.com%2Ftaxonomy%2Fterm%2F30%252B31%252B32%252B33%252B34%252B35%252B36%252B6774%252B590757%2F0%2Ffeed"};

        for (int i = 0; i < 3; i++) {
            String url = urls[i];
            CustomRequestClass myCustomRequest = new CustomRequestClass(Request.Method.GET,
                    url,
                    new Response.Listener<ArrayList<Movies>>() {
                        @Override
                        public void onResponse(ArrayList<Movies> newsList) {
                            movie.addAll(newsList);
                            mAdaptar.updateList(movie,0);
                            spinner.hide();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError response) {
                            spinner.show();
                            Toast.makeText(MoviesActivity.this, "Error Encountered", Toast.LENGTH_SHORT).show();
                        }
                    });

            MyApplication myApplication = new MyApplication(MoviesActivity.this);
            myApplication.addToRequestQueue(myCustomRequest, "FetchNews");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().cancelAllRequests("FetchNews");
    }

    public void setSharedPreferences() {

        SharedPreferences sharedPreferences = this.getSharedPreferences("loginStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedIn", false);
        editor.apply();
    }

    public void setRecyclerView() {

        recyclerView = findViewById(R.id.Recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdaptar = new movieAdaptor(this, this);
        recyclerView.setAdapter(mAdaptar);
    }

    public void setSpinner() {

        spinner = new ProgressDialog(MoviesActivity.this);
        spinner.setMessage("Please wait...It is downloading");
        spinner.setIndeterminate(false);
        spinner.setCancelable(false);
        spinner.show();
    }

    @Override
    public void likepress(int position) {

        string = movie.get(position).Categ;
        movie.get(position).setLikeStatus(1);
        SharedPreferences sp = getSharedPreferences("d",0);
        SharedPreferences s = getSharedPreferences("loginStatus", 0);
        user = s.getString("user", "");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(user, string);
        editor.apply();
    }

    @Override
    public void disLike(int position) {
        movie.get(position).setLikeStatus(1);
        SharedPreferences sp = getSharedPreferences("d",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(user, "");
        editor.apply();
    }

}

