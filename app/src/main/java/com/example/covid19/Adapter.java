package com.example.covid19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.Models.Model;

import java.text.NumberFormat;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    int m = 1;
    Context context;

    public Adapter(Context context, List<Model> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    List<Model> countryList;

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        Model model = countryList.get(position);
        if(m == 1){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(model.getCases())));
        } else if(m == 2){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(model.getRecovered())));
        } else if(m == 3){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(model.getDeaths())));
        } else{
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(model.getActive())));
        }

        holder.country.setText(model.getCountry());

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cases, country;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cases = itemView.findViewById(R.id.countrycase);
            country = itemView.findViewById(R.id.countryname);

        }
    }

    public void filter(String charText){
        if(charText.equals("cases")) m = 1;
        else if(charText.equals("recovered")) m = 2;
        else if(charText.equals("deaths")) m = 3;
        else m = 4;

        notifyDataSetChanged();
    }


}
