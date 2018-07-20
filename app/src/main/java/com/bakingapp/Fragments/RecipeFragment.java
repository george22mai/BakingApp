package com.bakingapp.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
    int selectedStep;

    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new StepsAdapter(list, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        list.clear();
        list.addAll(getList(getPosition(), Singleton.getInstance(getContext()).getRecipes()));
        adapter.notifyDataSetChanged();

        if (savedInstanceState != null){
            if (view.findViewById(R.id.video_fragment) == null)
                Toast.makeText(getContext(), "null video fragment", Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putInt("recipePosition", getPosition());
            bundle.putInt("stepPosition", savedInstanceState.getInt("SELECTED_STEP"));
            StepFragment fragment = new StepFragment();
            fragment.setArguments(bundle);
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .addToBackStack("recipe")
//                    .replace(R.id.video_fragment, fragment)
//                    .commit();
        }

        return view;
    }

    @Override
    public void onItemClick(int position) {
        selectedStep = position;
        Bundle bundle = new Bundle();
        bundle.putInt("recipePosition", getPosition());
        bundle.putInt("stepPosition", position);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(bundle);
        if (getView().findViewById(R.id.tablet_layout_recipe) == null)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("recipe")
                    .replace(R.id.main_activity_fragment, fragment)
                    .commit();
        else
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("recipe")
                    .replace(R.id.video_fragment, fragment)
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
            return getArguments().getInt("POSITION");
        return 0;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("recipe");
        outState.putInt("SCROLL_POSITION", layoutManager.findFirstCompletelyVisibleItemPosition());
        outState.putInt("POSITION", getPosition());
        outState.putInt("SELECTED_STEP", selectedStep);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", savedInstanceState.getInt("POSITION"));
            bundle.putInt("SCROLL_POSITION", savedInstanceState.getInt("SCROLL_POSITION"));
            bundle.putInt("SELECTED_STEP", savedInstanceState.getInt("SELECTED_STEP"));
            RecipeFragment fragment = new RecipeFragment();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("recipes")
                    .replace(R.id.main_activity_fragment, fragment)
                    .commit();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_fragment, fragment).commit();
        }
        super.onViewStateRestored(savedInstanceState);
    }
}
