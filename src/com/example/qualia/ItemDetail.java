package com.example.qualia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Marine on 07/02/2015.
 */
public class ItemDetail extends Activity {

    private SharedPreferences preferences;

    private String title;
    private String link;
    private String desc;
    private String date;

    private TextView textview_title;
    private TextView textview_date;
    private TextView textview_desc;
    private Button button_link;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        preferences = getApplicationContext().getSharedPreferences("READ_ITEMS", 0);
        int position = this.getIntent().getIntExtra("POS", 0);

        title = preferences.getString("TITLE_" + position, "");
        link = preferences.getString("LINK_" + position, "");
        desc = preferences.getString("DESC_" + position, "");
        date = preferences.getString("DATE_" + position, "");

        textview_title = (TextView) findViewById(R.id.detail_title);
        textview_date = (TextView) findViewById(R.id.detail_date);
        textview_desc = (TextView) findViewById(R.id.detail_desc);
        button_link = (Button) findViewById(R.id.detail_link);

        textview_title.setText("" + title);
        textview_date.setText("" + date);
        textview_desc.setText("" + desc);


        button_link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String url = "" + link;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });



    }



}