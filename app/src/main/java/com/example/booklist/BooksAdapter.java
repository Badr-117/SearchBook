package com.example.booklist;

import android.app.Activity;
import android.media.Image;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BooksAdapter extends ArrayAdapter<Books> {

    private static final String LOG_TAG = BooksAdapter.class.getSimpleName();



    public BooksAdapter(Activity context, ArrayList<Books> books){
        super(context, 0 , books);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Books currentBook = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.title_tv);
        title.setText(currentBook.getTitle());

        TextView author = (TextView) listItemView.findViewById(R.id.author_tv);
        author.setText(currentBook.getAuthor());

        TextView category = (TextView) listItemView.findViewById(R.id.category_tv);
        category.setText(currentBook.getCategory());

        ImageView image = (ImageView) listItemView.findViewById(R.id.image_iv);
        if(currentBook != null){
            Picasso.with(getContext()).load(currentBook.getImageUrl()).into(image);
        }





        return listItemView;
    }

}
