package org.example.dao;

import org.example.entity.AuthUser;
import org.example.entity.Todo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TodoDao extends BaseDao<Todo, Integer> {

    @Transactional(readOnly = true)
    public List<Todo> findAllByCreatedUser(AuthUser user) {
        try {
            return em.createQuery("from Todo where createdUser = :created_user", Todo.class)
                    .setParameter("created_user", user)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
