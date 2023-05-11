package com.example.movierecommendation;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

public class movieAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Movies> moviesList = new ArrayList<>();
    Context context;

    String s,r;
    int viewType = 0;
    boolean likeStatus = false;
    public interface onclicklisten{
        void likepress(int position);
        void disLike(int position);
    }
    onclicklisten listener;
    public movieAdaptor(Context context,onclicklisten listener) {
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if(viewType == 0){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
            return new viewHolder(itemView, listener);
        }
        else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.type2, parent, false);
            return new viewHolder2(itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(viewType == 0){
            return  0;
        }
        return 1;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {

        Movies movies = moviesList.get(position);
        if(holder.getItemViewType() == 0 && holder instanceof viewHolder){
            ((viewHolder)holder).title.setText(movies.name);
            ((viewHolder)holder).category.setText(movies.Categ);

            ImageRequest request = new ImageRequest(movies.imglink,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            ((viewHolder)holder).imageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            ((viewHolder)holder).imageView.setImageResource(R.drawable.ic_launcher_background);
                        }
                    });

            MyApplication myApplication = new MyApplication(context);
            myApplication.addToRequestQueue(request,"FetchNews");

            ((viewHolder)holder).likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.likepress(position);

                    if(likeStatus == false){
                        likeStatus = true;
                        ((viewHolder)holder).likeBtn.setImageResource(R.drawable.baseline_thumb_up_alt_24);
                    }
                    else{
                        likeStatus = false;
                        ((viewHolder)holder).likeBtn.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                    }
                }
            });

            ((viewHolder)holder).disLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.disLike(position);
                    if (likeStatus == false) {
                        likeStatus = true;
                        ((viewHolder) holder).disLikeBtn.setImageResource(R.drawable.baseline_thumb_down_alt_24);
                    } else {
                        likeStatus = false;
                        ((viewHolder) holder).disLikeBtn.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
                    }
                }
            });
        }

        else if(holder.getItemViewType() == 1 && holder instanceof viewHolder2){

            ((viewHolder2)holder).title2.setText(movies.name);
            ((viewHolder2)holder).categ2.setText(movies.Categ);

            ImageRequest request = new ImageRequest(movies.imglink,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            ((viewHolder2)holder).imageView2.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            ((viewHolder2)holder).imageView2.setImageResource(R.drawable.ic_launcher_background);
                        }
                    });
            MyApplication myApplication=new MyApplication(context);
            myApplication.addToRequestQueue(request,"FetchNews");
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, category;
        ImageView imageView;
        ImageButton likeBtn, disLikeBtn;
        public viewHolder(View view, onclicklisten listen) {
            super(view);

            title = view.findViewById(R.id.moviename1);
            category = view.findViewById(R.id.categ1);
            imageView = view.findViewById(R.id.movieimg1);
            likeBtn = view.findViewById(R.id.like1);
            disLikeBtn = view.findViewById(R.id.dislike);
        }

    }

    public class viewHolder2 extends RecyclerView.ViewHolder {
        TextView title2, categ2;
        ImageView imageView2;
        public viewHolder2(View view) {
            super(view);
            title2 = view.findViewById(R.id.moviename2);
            categ2 = view.findViewById(R.id.categ2);
            imageView2 = view.findViewById(R.id.movieimg2);
        }
    }

    void updateList(ArrayList<Movies> moviesList, int viewType) {
        this.viewType = viewType;
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }

}
