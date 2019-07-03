package com.example.andriod.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText edTitle = findViewById(R.id.etTitle);
        final EditText edAuthor = findViewById(R.id.etAuthor);
        final EditText edPublisher = findViewById(R.id.etPublisher);
        final EditText edIsbn = findViewById(R.id.etIsbn);
        final Button  button= findViewById(R.id.btnSearch);
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edTitle.getText().toString().trim();
                String author = edAuthor.getText().toString().trim();
                String publisher = edPublisher.getText().toString().trim();
                String isbn = edIsbn.getText().toString().trim();
                if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()){
                    String message = getString(R.string.no_search_data);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }else {
                    URL queryURL = ApiUtil.buildUrl(title, author, publisher, isbn);
                    Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                    intent.putExtra("Query", queryURL);
                    startActivity(intent);
                }
            }
        });

    }
}
