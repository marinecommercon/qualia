package dao;

import de.greenrobot.dao.DaoException;


public class Rss {

	private Long id;
	private String name;

	private transient DaoSession daoSession;

	private transient RssDao myDao;


	public Rss() {
	}

	public Rss(Long id) {
		this.id = id;
	}

	public Rss(Long id, String name) {
		this.id = id;
		this.name = name;
	}


	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getRssDao() : null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public void delete() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.delete(this);
	}


	public void update() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.update(this);
	}


	public void refresh() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.refresh(this);
	}

}
