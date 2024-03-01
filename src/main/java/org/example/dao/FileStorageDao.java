package org.example.dao;

import org.example.entity.FileStorage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FileStorageDao extends BaseDao<FileStorage, Integer> {

    @Transactional(readOnly = true)
    public FileStorage findByGeneratedName(String genName) {
        return em.createQuery("from FileStorage where generatedName = :generatedName", FileStorage.class)
                .setParameter("generatedName", genName)
                .getSingleResult();
    }
}
