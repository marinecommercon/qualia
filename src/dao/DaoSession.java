package dao;

import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.IdentityScopeType;

import java.util.Map;



public class DaoSession extends AbstractDaoSession {

	private final DaoConfig linkDaoConfig;
	private final DaoConfig rssDaoConfig;

	private final LinkDao linkDao;
	private final RssDao rssDao;

	public DaoSession(SQLiteDatabase db, IdentityScopeType type,
			Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);

		linkDaoConfig = daoConfigMap.get(LinkDao.class).clone();
		linkDaoConfig.initIdentityScope(type);

		rssDaoConfig = daoConfigMap.get(RssDao.class).clone();
		rssDaoConfig.initIdentityScope(type);

		linkDao = new LinkDao(linkDaoConfig, this);
		rssDao = new RssDao(rssDaoConfig, this);

		registerDao(Link.class, linkDao);
		registerDao(Rss.class, rssDao);
	}

	public void clear() {
		linkDaoConfig.getIdentityScope().clear();
		rssDaoConfig.getIdentityScope().clear();
	}

	public LinkDao getLinkDao() {
		return linkDao;
	}

	public RssDao getRssDao() {
		return rssDao;
	}

}
