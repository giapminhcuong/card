package com.cuong.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.cuong.models.TimeManageable;
import com.cuong.utils.HibernateUtils;

public abstract class GenericDAO<PK extends Serializable, T extends TimeManageable> {

	private static final Logger LOGGER = Logger.getLogger(GenericDAO.class.getName());

	private SessionFactory sessionFactory;
	private Class<T> persistenceClass;

	@SuppressWarnings("unchecked")
	public GenericDAO() {
		this.persistenceClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[1];
		sessionFactory = HibernateUtils.getSessionFactory();
	}

	public List<T> loadAll() {
		LOGGER.info("loadAll()");
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		Query<T> query = session.createQuery("from " + getPersistenceClass().getName() + " order by createdAt",
				getPersistenceClass());
		List<T> resultList = query.getResultList();
		session.flush();
		transaction.commit();
		return resultList;
	}

	public T findById(Serializable id) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		T entity = session.find(getPersistenceClass(), id);
		session.flush();
		transaction.commit();
		return entity;
	}

	public T save(T entity) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		Date currentDate = new Date();
		entity.setCreatedAt(currentDate);
		entity.setUpdatedAt(currentDate);
		session.save(entity);
		session.flush();
		transaction.commit();
		return entity;
	}

	public T update(T entity) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		entity.setUpdatedAt(new Date());
		session.update(entity);
		session.flush();
		transaction.commit();
		return entity;
	}

	public T saveOrUpdate(T entity) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(entity);
		session.flush();
		transaction.commit();
		return entity;
	}

	public boolean delete(T entity) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		session.delete(entity);
		session.flush();
		transaction.commit();
		return true;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Class<T> getPersistenceClass() {
		return persistenceClass;
	}

}
