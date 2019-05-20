package com.softwarica.heroes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.Heroes;

class HeroesAdapter extends RecyclerView.Adapter<HeroesAdapter.HeroesViewHolder> {
    Context mContext;
    List<Heroes> heroesList;

    public HeroesAdapter(Context mContext, List<Heroes> heroesList) {
        this.mContext = mContext;
        this.heroesList = heroesList;
    }

    @NonNull
    @Override
    public HeroesAdapter.HeroesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_hero, viewGroup, false);
        return new HeroesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroesAdapter.HeroesViewHolder heroesViewHolder, int i) {

        final Heroes heroes = heroesList.get(i);
//        heroesViewHolder.image.setImage(heroes.getImage());
        String img = heroes.getImage();

        heroesViewHolder.tvName.setText(heroes.getName());
        heroesViewHolder.tvDesc.setText(heroes.getDesc());




    }

    @Override
    public int getItemCount() {
        return heroesList.size();
    }

    public class HeroesViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView tvName, tvDesc;

        public HeroesViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDesc);

        }
    }
}
