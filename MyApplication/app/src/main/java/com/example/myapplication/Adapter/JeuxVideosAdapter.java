package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Model.JeuVideo;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collection;

public class JeuxVideosAdapter extends ArrayAdapter<JeuVideo> {

    private final ArrayList<JeuVideo> m_lsItems;

    public JeuxVideosAdapter(@NonNull Context context, int resource) {
        super(context, resource);

        //Initialise les variables de classe
        m_lsItems = new ArrayList<JeuVideo>();
    }

    @Override
    public int getPosition(JeuVideo item) {
        return m_lsItems.indexOf(item);
    }

    @Override
    public int getCount() {
        return m_lsItems.size();
    }

    @Override
    public JeuVideo getItem(int position) {
        return m_lsItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Obtient un objet Inflater
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inflate le layout XML listview_item
        View rowView = inflater.inflate(R.layout.listview_item, parent, false);

        //Référence les vues
        TextView textViewDay = (TextView) rowView.findViewById(R.id.textviewDay);
        TextView textViewDesc = (TextView) rowView.findViewById(R.id.textviewdDesc);
        TextView textviewdPlatforms = (TextView) rowView.findViewById(R.id.textviewdPlatforms);

        //Le FcstDay à afficher sur cet item de la list
        JeuVideo gameToShow = getItem(position);

        //Valorise les vues avec le FcstDay correspondant
        textViewDay.setText(gameToShow.getName());
        textViewDesc.setText(gameToShow.getSummary());

        if (gameToShow.getPlatforms() == null || gameToShow.getPlatforms().size() == 0)
            textviewdPlatforms.setText(R.string.detailsActivity_availableOn_noInfo);
        else
            textviewdPlatforms.setText(String.format(getContext().getResources().getString(R.string.detailsActivity_availableOn_x_platforms), gameToShow.getPlatforms().size()));

        return rowView;
    }

    @Override
    public void addAll(@NonNull Collection<? extends JeuVideo> collection){
        //Vide la liste actuelle
        m_lsItems.clear();

        //Ajoute les nouveaux éléments à la liste
        m_lsItems.addAll(collection);

        //Met à jour la ListView
        notifyDataSetChanged();
    }

    @Override
    public void add(@Nullable JeuVideo object) {
        super.add(object);
    }
}