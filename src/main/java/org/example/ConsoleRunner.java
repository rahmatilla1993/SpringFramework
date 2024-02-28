package org.example;

import org.example.dao.AuthUserDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsoleRunner {
    public static void main(String[] args) {
        try (var context = new ClassPathXmlApplicationContext("classpath:/data-config.xml")) {
            AuthUserDao dao = context.getBean(AuthUserDao.class);
            dao.findByUsername("admin")
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("User not found")
                    );
        }
    }
}
