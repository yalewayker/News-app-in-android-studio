package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    SearchView searchView;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.news_recycler_view);
        progressIndicator = findViewById(R.id.progress_bar);
        searchView = findViewById(R.id.search_view);
        spinner=findViewById(R.id.spinner);
        btn1= findViewById(R.id.btn_1);
        btn2= findViewById(R.id.btn_2);
        btn3= findViewById(R.id.btn_3);
        btn4= findViewById(R.id.btn_4);
        btn5= findViewById(R.id.btn_5);
        btn6= findViewById(R.id.btn_6);
        btn7= findViewById(R.id.btn_7);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.language_options,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

// Set a click listener to show the language options when the spinner is pressed
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spinner.performClick();
                return true;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("GENERAL",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        setupRecyclerView();
        getNews("GENERAL",null);
    }

    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }
void changeInProgress(boolean show){
        if(show)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
}


void getNews(String category,String query){
        changeInProgress(true);
    NewsApiClient newsApiClient = new NewsApiClient("f24db9fdc6d1412a815355d4ec693986");
    newsApiClient.getTopHeadlines(
            new TopHeadlinesRequest.Builder()
                    .language("en")
                    .category(category)
                    .q(query)
                    .build(),
            new NewsApiClient.ArticlesResponseCallback() {
                @Override
                public void onSuccess(ArticleResponse response) {
                    runOnUiThread(()->{
                       changeInProgress(false);
                       articleList=response.getArticles();
                       adapter.updateData(articleList);
                       adapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.i("GOT Failure",throwable.getMessage());
                }
            }
    );
}


    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        String category = btn.getText().toString();
        getNews(category,null);
    }
}
