package com.bakingapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bakingapp.R;
import com.bakingapp.Utilities.Adapters.RecipeAdapter;
import com.bakingapp.Utilities.Objects.Recipe;
import com.bakingapp.Utilities.Retrofit.RetrofitClient;
import com.bakingapp.Utilities.Singleton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesFragment extends Fragment
    implements RecipeAdapter.OnItemClickListener{

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    List<Recipe> recipes = new ArrayList<>();
    GridLayoutManager layoutManager;
    RecipeAdapter adapter;
    Singleton util = Singleton.getInstance(getContext());

    public RecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecipeAdapter(recipes, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (view.findViewById(R.id.tablet_layout) != null){
            layoutManager.setSpanCount(3);
            Toast.makeText(getContext(), "tablet", Toast.LENGTH_SHORT).show();
        }

        RetrofitClient.RetrofitInterface retrofitInterface = RetrofitClient.getInstance(getContext()).create(RetrofitClient.RetrofitInterface.class);

        retrofit2.Call<List<Recipe>> call = retrofitInterface.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Recipe>> call, Response<List<Recipe>> response) {
                util.setRecipes(response.body());
                recipes.clear();
                recipes.addAll(util.getRecipes());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<List<Recipe>> call, Throwable t) {
                Log.d("RETROFIT ERROR", t.toString());
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack("recipes")
                .replace(R.id.main_activity_fragment, fragment)
                .commit();
    }
}
