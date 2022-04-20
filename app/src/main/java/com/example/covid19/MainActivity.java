package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.covid19.Models.Model;
import com.example.covid19.Utilities.ApiUtilities;
import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    
    CountryCodePicker countryCodePicker;
    TextView mtodaytotal, mtotal, mactive, mtodayactive, mrecovered, mtodayrecovered, mdeaths, mtodaydeaths;
    
    String country;
    TextView mfilter;
    Spinner spinner;
    String[] types = {"cases", "deaths", "recovered", "active"};
    private List<Model> modelClassList;
    private List<Model> modelClassList2;
    
    PieChart mpiechart;
    private RecyclerView recyclerView;
    com.example.covid19.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countryCodePicker = findViewById(R.id.ccp); 
        mtodaytotal = findViewById(R.id.todayTotal); 
        mtotal = findViewById(R.id.totalCase); 
        mactive = findViewById(R.id.active_case); 
        mtodayactive = findViewById(R.id.todayActive); 
        mrecovered = findViewById(R.id.recoveredCase); 
        mtodayrecovered = findViewById(R.id.todayRecovered); 
        mdeaths = findViewById(R.id.totalDeaths); 
        mtodaydeaths = findViewById(R.id.todayDeath);
        
        mpiechart = findViewById(R.id.pie_chart);
        spinner = findViewById(R.id.spinner);
        mfilter = findViewById(R.id.filter);
        recyclerView = findViewById(R.id.recyclerview);
        modelClassList = new ArrayList<>();
        modelClassList2 = new ArrayList<>();
        
        
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinner.setAdapter(arrayAdapter);

        ApiUtilities.getAPIInterface().getCountryData().enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                modelClassList2.addAll(response.body());
//                adapter.notify
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {

            }
        });
        
//        adapter = new Adapter(getApplicationContext(), modelClassList2);
        adapter = new Adapter(getApplicationContext(), modelClassList2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        
        countryCodePicker.setAutoDetectedCountry(true);
        country = countryCodePicker.getSelectedCountryName();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country = countryCodePicker.getSelectedCountryName();
                fetchData();
            }
        });
        
        fetchData();
        
    }

    private void fetchData() {
        
        ApiUtilities.getAPIInterface().getCountryData().enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                modelClassList.addAll(response.body());
                for(int i = 0; i < modelClassList.size(); ++i){
                    if(modelClassList.get(i).getCountry().equals(country)){
                        mactive.setText((modelClassList.get(i).getActive()));
                        mtodaydeaths.setText((modelClassList.get(i).getTodayDeaths()));
                        mtodayrecovered.setText((modelClassList.get(i).getTodayRecovered()));
                        mtodaytotal.setText((modelClassList.get(i).getTodayCases()));
                        mtotal.setText((modelClassList.get(i).getCases()));
                        mdeaths.setText((modelClassList.get(i).getDeaths()));
                        mrecovered.setText((modelClassList.get(i).getRecovered()));
                        
                        int active, total, recovered, deaths;
                        active = Integer.parseInt(modelClassList.get(i).getActive());
                        total = Integer.parseInt(modelClassList.get(i).getCases());
                        recovered = Integer.parseInt(modelClassList.get(i).getRecovered());
                        deaths = Integer.parseInt(modelClassList.get(i).getDeaths());
                        
                        updateGraph(active, total, recovered, deaths);
                        
                        
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {

            }
        });
        
        
        
        
    }

    private void updateGraph(int active, int total, int recovered, int deaths) {
        mpiechart.clearChart();
        mpiechart.addPieSlice(new PieModel("Confirm", total, Color.parseColor("#FFB701")));
        mpiechart.addPieSlice(new PieModel("Active", active, Color.parseColor("#FF4CAF50")));
        mpiechart.addPieSlice(new PieModel("Recovered", recovered, Color.parseColor("#38ACCD")));
        mpiechart.addPieSlice(new PieModel("Deaths", deaths, Color.parseColor("#F55C47")));
        mpiechart.startAnimation();


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String item = types[i];
        mfilter.setText(item);
        adapter.filter(item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}