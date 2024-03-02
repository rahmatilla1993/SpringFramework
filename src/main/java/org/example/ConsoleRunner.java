package org.example;

import org.example.dao.AuthUserDao;
import org.example.dao.RoleDao;
import org.example.entity.Permission;
import org.example.entity.Role;
import org.example.enums.RoleName;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

public class ConsoleRunner {
    public static void main(String[] args) {
        try (var context = new ClassPathXmlApplicationContext("classpath:/data-config.xml")) {
            AuthUserDao dao = context.getBean(AuthUserDao.class);
            dao.findAllUsersByRole2(RoleName.ROLE_USER).forEach(System.out::println);
//            dao.findAll().forEach(System.out::println);
        }
    }
}
