package com.theword.thedigitalword.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Size;

import com.theword.thedigitalword.R;
import com.theword.thedigitalword.model.Book;
import com.theword.thedigitalword.model.Card;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BookCardArrayAdapter extends ArrayAdapter<Book> {

    public BookCardArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    private List<Book> cardList = new ArrayList<Book>();

    static class CardViewHolder {
        ImageView image;
        TextView name;
        TextView english;
    }

    @Override
    public void add(Book object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Book getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BookCardArrayAdapter.CardViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_item_card, parent, false);
//            viewHolder = new BookCardArrayAdapter.CardViewHolder();
//            viewHolder.image = (ImageView) row.findViewById(R.id.cardbook);
//            viewHolder.name = (TextView) row.findViewById(R.id.cardbookname);
//            viewHolder.english = (TextView) row.findViewById(R.id.cardbookenglish);
//            row.setTag(viewHolder);
        } else {
            //viewHolder = (BookCardArrayAdapter.CardViewHolder) row.getTag();
        }

        Book card = getItem(position);
        // viewHolder.image.getParent()
        final ImageView image = (ImageView)convertView.findViewById(R.id.cardbook);
        final TextView name = (TextView) convertView.findViewById(R.id.cardbookname);
        final TextView english = (TextView)convertView.findViewById(R.id.cardbookenglish);

        //image.setImageBitmap(Util.decodeSampledBitmapFromResource(this.getContext().getResources(),Util.getBibleBookImage(card.getBookIcon(), getContext()),55,55)     );
        image.setImageResource(Util.getBibleBookImage(card.getBookIcon(), getContext()));
        name.setText(card.getBookName());
        name.setTextDirection(card.getDir().equalsIgnoreCase("LTR") ? TextView.TEXT_DIRECTION_LTR : TextView.TEXT_DIRECTION_RTL);
        name.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        if (card.getLang() != null && !card.getLang().trim().equalsIgnoreCase("ENG")){
            english.setText(card.getEnglishName());
        }
//        if(card.getBookIcon().equalsIgnoreCase(Util.getSelectedBook()) &&
//            name.getText().equals(card.getBookName())){
//            name.setTypeface(null, Typeface.BOLD);
//            name.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
//            if (card.getLang() != null && !card.getLang().trim().equalsIgnoreCase("ENG")) {
//                english.setTypeface(null, Typeface.BOLD);
//                english.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
//            }
//
//        }
        return convertView;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private int selectedItem;
    private String selectedBook;

    public void setSelectedItem(int position) {
        selectedItem = position;
    }

    public void setSelectedBook(String book){
        this.selectedBook = book;
    }
}
