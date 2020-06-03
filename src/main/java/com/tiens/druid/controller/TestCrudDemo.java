package com.tiens.druid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RequestMapping("/test")
@RestController
public class TestCrudDemo implements Runnable{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TestCrudDemo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(20, 100, 30,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(80));

    @Override
    public void run() {
        for(int i=0;i<=1000;i++) {
            String sqlStr = " INSERT INTO user(user_id, user_name, user_pwd) VALUES (" + i + " ,'q', 'u')";
            jdbcTemplate.update(sqlStr);
        }
    }

    @RequestMapping("/insert")
    public void save() {
        for(int i=0;i<80;i++){
            threadPool.execute(new TestCrudDemo(jdbcTemplate));
        }
    }
}
