package com.example.movierecommendation;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomRequestClass extends Request {

    private final Response.Listener successListener;

    CustomRequestClass(int method, String url, Response.Listener successListener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.successListener = successListener;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {

        List<Movies> moviessList = new ArrayList<>();

        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            JSONObject jsonObject = new JSONObject(json);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            int count = 0;

            String title, category, imageLink = null;

            while (count < itemsArray.length()) {

                JSONObject newsJson = itemsArray.getJSONObject(count);
                title = newsJson.getString("title").replaceAll("<.*?>","");

                JSONArray categories = newsJson.getJSONArray("categories");
                category = categories.getString(categories.length() - 1);

                String regex = "\\b((?:https?|ftp|file):" + "//[-a-zA-Z0-9+&@#/%?=" + "~_|!:, .;]*[-a-zA-Z0-9+" + "&@#/%=~_|])";
                String res = newsJson.getString("description");
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(res);

                while(matcher.find()) {
                    imageLink = res.substring(matcher.start(0), matcher.end(0));
                    break;
                }

                Movies movies = new Movies(title,imageLink,category,0);
                moviessList.add(movies);
                count++;
            }

            return Response.success(moviessList, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        successListener.onResponse(response);
    }
}
