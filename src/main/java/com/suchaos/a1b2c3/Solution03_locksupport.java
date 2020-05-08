package com.suchaos.a1b2c3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 解法3
 *
 * @author suchao
 * @date 2020/5/7
 */
@Slf4j
public class Solution03_locksupport {

    static Thread thread1 = null, thread2 = null;

    public static void main(String[] args) throws InterruptedException {

        char[] a1 = "1234567".toCharArray();
        char[] a2 = "abcdefg".toCharArray();

        thread1 = new Thread(() -> {
            for (char a : a1) {
                log.info(String.valueOf(a));
                LockSupport.unpark(thread2);
                LockSupport.park();
            }
        });
        thread2 = new Thread(() -> {
            for (char a : a2) {
                LockSupport.park();
                log.info(String.valueOf(a));
                LockSupport.unpark(thread1);
            }
        });

        thread2.start();
        thread1.start();

        thread1.join();
        thread2.join();
    }
}
