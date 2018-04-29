package com.bakingapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bakingapp.R;
import com.bakingapp.Utilities.Adapters.RecipeAdapter;
import com.bakingapp.Utilities.Adapters.StepsAdapter;
import com.bakingapp.Utilities.Objects.Recipe;
import com.bakingapp.Utilities.Objects.Step;
import com.bakingapp.Utilities.Singleton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment
    implements RecipeAdapter.OnItemClickListener{

    @BindView(R.id.step_recycler_view) RecyclerView recyclerView;

    List<String> list = new ArrayList<>();
    LinearLayoutManager layoutManager;
    StepsAdapter adapter;

    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new StepsAdapter(list, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        list.clear();
        list.addAll(getList(getPosition(), Singleton.getInstance(getContext()).getRecipes()));
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("recipePosition", getPosition());
        bundle.putInt("stepPosition", position);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack("recipe")
                .replace(R.id.main_activity_fragment, fragment)
                .commit();
    }

    public List<String> getList(int position, List<Recipe> recipes){
        List<String> steps = new ArrayList<>();
        for (Step step : recipes.get(position).getSteps()) {
            steps.add(step.getShortDescription());
        }
        return steps;
    }

    public int getPosition(){
        if (getArguments() != null)
            return getArguments().getInt("position");
        return 0;
    }
}
