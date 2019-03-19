package com.opentable.moviereviews;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.opentable.moviereviews.data.Result;
import com.opentable.moviereviews.ui.MoviesAdapter;
import com.opentable.moviereviews.viewmodel.MoviesViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MoviesAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ListView used to display the movie reviews
        final ListView listView = (ListView) findViewById(R.id.movie_list_view);
        final MoviesViewModel model = ViewModelProviders.of(this).get(MoviesViewModel.class);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //Using LiveData, the ViewModel updates the item list
        // and the UI logic below uses an observe listener to detect and react to those changes
        model.getItemList().observe(this,
                new Observer<ArrayList<Result>>() {
                    @Override
                    public void onChanged(@Nullable ArrayList<Result> items) {
                        if (itemsAdapter == null) {
                            itemsAdapter = new MoviesAdapter(getApplicationContext(), items);
                            // Assign adapter to ListView
                            listView.setAdapter(itemsAdapter);
                        } else {
                            itemsAdapter.clear();
                            itemsAdapter.addAll(items);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }
        );

        //handle error state, detected via the LiveData set up in the ViewModel for this purpose
        model.getStatus().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                //throw error message
                displayErrorDialog("Error", throwable.getMessage());
            }
        });

        FloatingActionButton fab = findViewById(R.id.refresh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                model.refreshItemList();
            }
        });
    }

    private void displayErrorDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            displayErrorDialog(getString(R.string.action_about),
                    "The purpose of this application is to demonstrate a simple REST-ful API");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
