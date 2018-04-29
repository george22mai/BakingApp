package com.bakingapp.Utilities.Retrofit;

import android.content.Context;

import com.bakingapp.R;
import com.bakingapp.Utilities.Objects.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RetrofitClient {

    private static Retrofit instance = null;

    public static Retrofit getInstance(Context context) {
        if (instance == null){
            instance = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.link_recipes))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }

    public static interface RetrofitInterface {

        @GET("baking.json")
        Call<List<Recipe>> getRecipes();

    }
}
