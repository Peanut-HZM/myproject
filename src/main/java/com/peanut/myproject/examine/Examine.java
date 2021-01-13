package com.peanut.myproject.examine;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author peanut
 */
public class Examine {

    public static void main(String[] args) {

        testCatch();

    }

    public static void testList(){
        String url = "";
        String username = "";
        String password = "";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (connection != null) {
               try {
                   connection.close();
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        }
    }

    public static void testCatch(){
        int a = 10;
        int b = 20;
        boolean flag = false;

        for (int i = 0; i < 100; i++) {
            try {
                System.out.println(i);
                if (b < i){
                    throw new RuntimeException("=======try-catch里面语句抛出运行时异常=====");
                }
                System.out.println("throw后面的代码块");
            }catch (Exception e){
                flag = true;
                System.out.println("执行catch代码块");
                e.printStackTrace();
                throw new RuntimeException("=======catch里面抛异常========");
//                break;
            }finally {
                System.out.println("执行finally代码块");
            }
        }
        if (flag){
            throw new RuntimeException("try-catch语句中有异常，后续代码不再执行");
        }
        System.out.println("try-catch之后的代码块");
    }

}
