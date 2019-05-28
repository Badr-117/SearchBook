package com.example.booklist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BooksDescription extends AppCompatActivity {
    private TextView title, author, description, category, date, buyBtn;
    private ImageView thumbnail;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

        String dataTitle = getIntent().getStringExtra("bookTitle");
        String dataAuthor = getIntent().getStringExtra("bookAuthor");
        String dataDescription = getIntent().getStringExtra("bookDescription");
        String dataImageUrl = getIntent().getStringExtra("bookImageUrl");
        String dataDate = getIntent().getStringExtra("bookDate");
        String dataCategory = getIntent().getStringExtra("bookCategory");
        final String dataRead = getIntent().getStringExtra("bookRead");



        title = (TextView) findViewById(R.id.desc_title);
        title.setText(dataTitle);

        author = (TextView) findViewById(R.id.desc_author);
        author.setText(dataAuthor);

        category = (TextView) findViewById(R.id.desc_category);
        category.setText(dataCategory);

        date = (TextView) findViewById(R.id.desc_date);
        date.setText(dataDate);

        description = (TextView) findViewById(R.id.books_description);
        description.setText(dataDescription);

        thumbnail = (ImageView) findViewById(R.id.desc_image);
        Picasso.with(context).load(dataImageUrl).into(thumbnail);

        buyBtn = (TextView) findViewById(R.id.buy_tv);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Uri readUri = Uri.parse(dataRead);
               Intent buyIntent = new Intent(Intent.ACTION_VIEW, readUri);
               startActivity(buyIntent);
            }
        });


    }
}
