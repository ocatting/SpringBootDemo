package com.design.factory.spring.demo;

public class RedisCounter {

    private final String host;
    private final int prot;

    public RedisCounter(String host, int prot) {
        this.host = host;
        this.prot = prot;
    }
}
