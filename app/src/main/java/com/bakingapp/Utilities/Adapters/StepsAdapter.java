package com.bakingapp.Utilities.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bakingapp.R;

import org.w3c.dom.Text;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    List<String> texts;
    RecipeAdapter.OnItemClickListener listener;

    public StepsAdapter(List<String> texts, RecipeAdapter.OnItemClickListener listener) {
        this.texts = texts;
        this.listener = listener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view_holder, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.bind(position, listener);
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public StepViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.name_recipe);
        }

        public void bind(final int position, final RecipeAdapter.OnItemClickListener listener){
            text.setText(texts.get(position));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }
    }

}
