package com.example.qualia;

import android.app.ListActivity;
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
    static final String KEY_TITLE = "title";
    static final String KEY_LINK = "link";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();



        // Parsing xml
        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(URL);
        Document doc = parser.getDocFromString(xml);

        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

        for (int i = 0; i < nl.getLength(); i++) {

            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);

            map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
            map.put(KEY_LINK, parser.getValue(e, KEY_LINK));

            menuItems.add(map);
        }


        ListAdapter adapter = new SimpleAdapter(this, menuItems,R.layout.list_items,
                new String[] {KEY_TITLE, KEY_LINK }, new int[] {
                R.id.title, R.id.link });

        setListAdapter(adapter);
        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });
    }


}
