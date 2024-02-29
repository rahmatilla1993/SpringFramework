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
//            AuthUserDao dao = context.getBean(AuthUserDao.class);
//            RoleDao roleDao = context.getBean(RoleDao.class);
//            Permission permission1 = Permission
//                    .builder()
//                    .name("block user")
//                    .code("BLOCK_USER")
//                    .createdAt(LocalDateTime.now())
//                    .build();
//            Role role = Role
//                    .builder()
//                    .name("admin")
//                    .createdAt(LocalDateTime.now())
//                    .code(RoleName.ROLE_ADMIN)
//                    .permissions(List.of(permission1))
//                    .build();
//
//            permission1.addRole(role);
//            roleDao.save(role);
        }
    }
}
