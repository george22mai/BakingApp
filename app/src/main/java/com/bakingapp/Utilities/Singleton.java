package com.bakingapp.Utilities;

import android.content.Context;

import com.bakingapp.R;
import com.bakingapp.Utilities.Objects.Recipe;

import java.util.List;

public class Singleton {

    List<Recipe> recipes;

    private static Singleton instance = new Singleton();

    public static Singleton getInstance(Context context) {
        return instance;
    }

    public void setRecipes(List<Recipe> list){
        recipes = list;
    }

    public List<Recipe> getRecipes(){
        return recipes;
    }
}
