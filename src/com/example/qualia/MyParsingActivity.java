package com.example.qualia;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import dao.*;
import database.MyDatabase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class MyParsingActivity extends ListActivity {

    private MyDatabase dbImp;
    public static SQLiteDatabase db;
    private DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static RssDao rssDao;
    public static LinkDao linkDao;

    static final String URL = "http://www.lemonde.fr/videos/rss_full.xml";

    //nodes
    static final String KEY_ITEM = "item";
    static final String KEY_LINK = "link";
    static final String KEY_TITLE = "title";
    static final String KEY_DESC = "description";
    static final String KEY_DATE = "pubDate";

    private Cursor cursor;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        preferences =  getApplicationContext().getSharedPreferences("READ_ITEMS", 0);

        ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

        importBdd();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "qualiadb", null);
        db = helper.getWritableDatabase();

        //init Daos
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        rssDao = MyParsingActivity.daoSession.getRssDao();
        linkDao = MyParsingActivity.daoSession.getLinkDao();

        long iD_rss = 0;


        MyParsingActivity.db.beginTransaction();
        try {
            iD_rss = MyParsingActivity.rssDao.insert(new Rss(null, "mon premier rss"));
            MyParsingActivity.linkDao.insert(new Link(null, "link 1", null, null, null, iD_rss));
            MyParsingActivity.linkDao.insert(new Link(null, "link 2", null, null, null, iD_rss));
            MyParsingActivity.linkDao.insert(new Link(null, "link 3", null, null, null, iD_rss));

            MyParsingActivity.db.setTransactionSuccessful();
        } finally {
            MyParsingActivity.db.endTransaction();
        }

        //find links
        String MY_QUERY;
        MY_QUERY = "SELECT LINK._id " + "FROM LINK "
                + "WHERE LINK._rssID = " + iD_rss ;

        cursor = MyParsingActivity.db.rawQuery(MY_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                System.out.println(""+MyParsingActivity.linkDao.load((long) cursor.getInt(0)).getId());
            } while (cursor.moveToNext());
        }
        cursor.close();








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

    private void importBdd(){
        dbImp = new MyDatabase(this);
        dbImp.getReadableDatabase();
        dbImp.close();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        db.close();
        super.onDestroy();
    }



}
