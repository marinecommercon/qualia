package com.example.qualia;

import adapter.AdapterLink;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import dao.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class MyParsingActivity extends ListActivity {

    //nodes
    static final String KEY_ITEM = "item";
    static final String KEY_LINK = "link";
    static final String KEY_TITLE = "title";
    static final String KEY_DESC = "description";
    static final String KEY_DATE = "pubDate";
    public static SQLiteDatabase db;
    public static DaoSession daoSession;
    public static RssDao rssDao;
    public static LinkDao linkDao;
    private DaoMaster daoMaster;
    private Cursor cursor;
    private ArrayList<Link> listLink;
    private AdapterLink adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_parsing_activity);

        int rssId = this.getIntent().getIntExtra("ID", 0);

        initDB();

        //First time the user clicks on this rss : requests cursor is null. Add all items to the database
        if (findLink(rssId) == 0) {

            if (isOnline() == false) {
                Toast.makeText(this, "Désolé, le contenu ne peut être chargé", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                addAllLink(rssId);
                findLink(rssId);
            }

        } else {
        }

        //Create the list with the cursor
        initList();

        //Show the list
        adapter = new AdapterLink(this, listLink);
        setListAdapter(adapter);

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent = new Intent(MyParsingActivity.this, ItemDetail.class);
        intent.putExtra("ID", (int) id);
        startActivity(intent);
    }


    private void initDB() {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "qualiadb", null);
        db = helper.getWritableDatabase();

        //init Daos
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        rssDao = MyParsingActivity.daoSession.getRssDao();
        linkDao = MyParsingActivity.daoSession.getLinkDao();
    }

    private int findLink(int rssId) {

        String MY_QUERY;
        MY_QUERY = "SELECT LINK._id " + "FROM LINK"
                + " WHERE LINK._rssId = " + rssId;
        cursor = MyParsingActivity.db.rawQuery(MY_QUERY, null);

        return cursor.getCount();
    }

    private void addAllLink(int rssID) {

        Rss rssItem = MyParsingActivity.rssDao.queryBuilder()
                .where(RssDao.Properties.Id.eq(rssID))
                .unique();

        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl("" + rssItem.getName());
        Document doc = parser.getDocFromString(xml);
        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

        for (int i = 0; i < nl.getLength(); i++) {

            Element e = (Element) nl.item(i);
            MyParsingActivity.db.beginTransaction();

            try {
                MyParsingActivity.linkDao.insert(new Link(null, "" + parser.getValue(e, KEY_TITLE),
                        "" + parser.getValue(e, KEY_DESC),
                        "" + parser.getValue(e, KEY_LINK),
                        "" + parser.getValue(e, KEY_DATE),
                        rssID));
                MyParsingActivity.db.setTransactionSuccessful();
            } finally {
                MyParsingActivity.db.endTransaction();
            }
        }
    }

    private void initList() {

        listLink = new ArrayList<Link>();
        if (cursor.moveToFirst()) {
            do {
                listLink.add(MyParsingActivity.linkDao.load((long) cursor.getInt(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        db.close();
        super.onDestroy();
    }


}
