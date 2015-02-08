package com.example.qualia;

import adapter.AdapterRss;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import dao.DaoMaster;
import dao.DaoSession;
import dao.Rss;
import dao.RssDao;
import database.MyDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Marine on 08/02/2015.
 */
public class MainActivity extends ListActivity {


    private MyDatabase dbImp;
    public static SQLiteDatabase db;
    private DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static RssDao rssDao;

    private ArrayList<Rss> listRss;
    private Cursor cursor;
    private AdapterRss adapter;

    private Timer myTimer;



    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_activity);


        //Add rss as Strings
        final ArrayList<String> rssItems = new ArrayList<String>();

        rssItems.add("http://www.lemonde.fr/videos/rss_full.xml");
        rssItems.add("http://www.lemonde.fr/ameriques/rss_full.xml");
        rssItems.add("http://www.lemonde.fr/services-aux-internautes/rss_full.xml");
        rssItems.add("http://www.lemonde.fr/afrique/rss_full.xml");
        rssItems.add("http://www.lemonde.fr/m-actu/rss_full.xml");

        //Prepare the database
        initDB();


        //First run : requests cursor is null. Add all items to the database
        if(findRss() == 0){
            addAllRss(rssItems);
            findRss();
        }else{}

        //Create the list with the cursor
        initList();

        //Show the list
        adapter = new AdapterRss(this, listRss);
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Toast.makeText(this, "Chargement", Toast.LENGTH_SHORT).show();
        myTimer = new Timer();
        id = listRss.get(position).getId();
        final Intent intent = new Intent(MainActivity.this,MyParsingActivity.class);
        intent.putExtra("ID", (int) id);

        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 1000);
    }


    private void initDB() {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "qualiadb", null);
        db = helper.getWritableDatabase();

        //init Daos
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        rssDao = MainActivity.daoSession.getRssDao();
    }


    private void addAllRss(List<String> rssItems){

        MainActivity.rssDao.deleteAll();

        for (int i = 0 ; i < rssItems.size() ; i++) {

            MainActivity.db.beginTransaction();
            try {
                MainActivity.rssDao.insert(new Rss(null, "" + rssItems.get(i)));
                MainActivity.db.setTransactionSuccessful();
            } finally {
                MainActivity.db.endTransaction();
            }

        }


    }

    private int findRss() {

        String MY_QUERY;
        MY_QUERY = "SELECT RSS._id " + "FROM RSS";
        cursor = MainActivity.db.rawQuery(MY_QUERY, null);

        return cursor.getCount();
    }

    private void initList(){

        listRss = new ArrayList<Rss>();
        if (cursor.moveToFirst()) {
            do {
                listRss.add(MainActivity.rssDao.load((long) cursor.getInt(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        db.close();
        super.onDestroy();
    }


}