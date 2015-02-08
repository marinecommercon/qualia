package dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;


public class LinkDao extends AbstractDao<Link, Long> {

    public static final String TABLENAME = "LINK";
    private DaoSession daoSession;

    ;

    public LinkDao(DaoConfig config) {
        super(config);
    }

    public LinkDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'LINK' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TITLE' TEXT," +
                "'DESC' TEXT," +
                "'LINK' TEXT," +
                "'DATE' TEXT," +
                "'_rssId' INTEGER NOT NULL );");
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
                + "'LINK'";
        db.execSQL(sql);
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, Link entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }

        String desc = entity.getDesc();
        if (desc != null) {
            stmt.bindString(3, desc);
        }

        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(4, link);
        }

        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(5, date);
        }

        stmt.bindLong(6, entity.getRssId());
    }

    @Override
    protected void attachEntity(Link entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Link readEntity(Cursor cursor, int offset) {
        Link entity = new Link(
                //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0),
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1),
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2),
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3),
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4),
                cursor.getLong(offset + 5)
        );
        return entity;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, Link entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor
                .getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor
                .getString(offset + 1));
        entity.setDesc(cursor.isNull(offset + 2) ? null : cursor
                .getString(offset + 2));
        entity.setLink(cursor.isNull(offset + 3) ? null : cursor
                .getString(offset + 3));
        entity.setDate(cursor.isNull(offset + 4) ? null : cursor
                .getString(offset + 4));
        entity.setRssId(cursor.getLong(offset + 5));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected Long updateKeyAfterInsert(Link entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long getKey(Link entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id",
                true, "_id");
        public final static Property Title = new Property(1, String.class, "title",
                false, "TITLE");
        public final static Property Desc = new Property(2, String.class, "desc",
                false, "DESC");
        public final static Property Link = new Property(3, String.class, "link",
                false, "LINK");
        public final static Property Date = new Property(4, String.class, "date",
                false, "DATE");
        public final static Property Rssid = new Property(5, Long.class, "rssId",
                false, "_rssId");

    }


}
