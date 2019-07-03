package com.example.andriod.book;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andriod.book.databinding.ActivityBookDetailBinding;


public class BookDetail extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Book book = getIntent().getParcelableExtra("Book");
        ActivityBookDetailBinding binding = ActivityBookDetailBinding.inflate(getLayoutInflater(),
                R.layout.activity_book_detail);
        binding.setBook(book);
    }



}

