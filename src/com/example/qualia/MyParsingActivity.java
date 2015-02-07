package com.example.qualia;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.HashMap;

public class MyParsingActivity extends ListActivity {

    static final String URL = "http://www.lemonde.fr/videos/rss_full.xml";

    //nodes
    static final String KEY_ITEM = "item";
    static final String KEY_LINK = "link";
    static final String KEY_TITLE = "title";
    static final String KEY_DESC = "description";
    static final String KEY_DATE = "pubDate";

    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        preferences =  getApplicationContext().getSharedPreferences("READ_ITEMS", 0);

        ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

        // Parsing xml
        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(URL);
        Document doc = parser.getDocFromString(xml);

        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

        for (int i = 0; i < nl.getLength(); i++) {

            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);

            //Save results in the preferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("LINK_"+i, parser.getValue(e, KEY_LINK));
            editor.putString("TITLE_"+i, parser.getValue(e, KEY_TITLE));
            editor.putString("DESC_"+i, parser.getValue(e, KEY_DESC));
            editor.putString("DATE_"+i, parser.getValue(e, KEY_DATE));
            editor.commit();


            // Add to the map for the listView
             map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
             map.put(KEY_DESC, "" + parser.getValue(e, KEY_DESC).substring(0,100) + "...");
             menuItems.add(map);
        }


        ListAdapter adapter = new SimpleAdapter(this, menuItems,R.layout.list_items,
                new String[] {KEY_TITLE, KEY_DESC }, new int[] {
                R.id.title, R.id.desc});

        setListAdapter(adapter);
        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                preferences =  getApplicationContext().getSharedPreferences("READ_ITEMS", 0);
                Intent intent = new Intent(MyParsingActivity.this,ItemDetail.class);
                intent.putExtra("POS", position);
                startActivity(intent);



            }
        });
    }


}
