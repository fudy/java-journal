package com.fudy.api;

import com.fudy.bean.Human;

public class Fudy extends Human {
    @Override
    public void say() {
        super.say();
        System.out.println("hello Fudy");
    }
}
