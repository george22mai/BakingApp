package com.bakingapp.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SubMenu;
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
        final View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecipeAdapter(recipes, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (view.findViewById(R.id.tablet_layout) != null){
            layoutManager.setSpanCount(3);
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
                //fillMenu();
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
        bundle.putInt("POSITION", position);
        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                //addToBackStack("recipes")
                .replace(R.id.main_activity_fragment, fragment, "recipes")
                .commit();
    }

    public void fillMenu(){
            NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            List<Recipe> recipes = util.getRecipes();
            SubMenu subMenu = menu.addSubMenu("Recipes");
            for (int i = 0; i < recipes.size(); i++)
                subMenu.add(0, i, 1, recipes.get(i).getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //fillMenu();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("SCROLL_POSITION", layoutManager.findFirstCompletelyVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null)
            recyclerView.scrollToPosition(savedInstanceState.getInt("SCROLL_POSITION"));
        super.onViewStateRestored(savedInstanceState);
    }
}
