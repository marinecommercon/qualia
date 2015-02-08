package com.example.qualia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import dao.Link;
import dao.LinkDao;

/**
 * Created by Marine on 07/02/2015.
 */
public class ItemDetail extends Activity {

    private TextView textview_title;
    private TextView textview_date;
    private TextView textview_desc;
    private Button button_link;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        int linkId = this.getIntent().getIntExtra("ID", 0);

        final Link linkItem = MyParsingActivity.linkDao.queryBuilder()
                .where(LinkDao.Properties.Id.eq(linkId))
                .unique();

        textview_title = (TextView) findViewById(R.id.detail_title);
        textview_date = (TextView) findViewById(R.id.detail_date);
        textview_desc = (TextView) findViewById(R.id.detail_desc);
        button_link = (Button) findViewById(R.id.detail_link);

        textview_title.setText("" + linkItem.getTitle());
        textview_date.setText("" + linkItem.getDate());
        textview_desc.setText("" + linkItem.getDesc());


        button_link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String url = "" + linkItem.getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });



    }



}