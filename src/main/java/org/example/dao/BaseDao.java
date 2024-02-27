package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entity.Auditable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Component
@Transactional
public abstract class BaseDao<T extends Auditable, ID extends Serializable> {

    @PersistenceContext
    private EntityManager em;

    protected final Class<T> persistenceClass;

    @SuppressWarnings("unchecked")
    public BaseDao() {
        persistenceClass = (Class<T>) (((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return em.createQuery("from " + persistenceClass.getSimpleName())
                .getResultList();
    }

    @Transactional(readOnly = true)
    public T getOne(ID id) {
        return em.find(persistenceClass, id);
    }

    public void save(T entity) {
        em.persist(entity);
    }

    public void edit(T entity) {
        em.merge(entity);
    }

    public void delete(ID id) {
        em.createQuery("delete from " + persistenceClass.getSimpleName() + " where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
