package com.suchaos.a1b2c3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 解法1
 *
 * @author suchao
 * @date 2020/5/7
 */
@Slf4j
public class Solution01_wait_notify_2 {

    public static void main(String[] args) throws InterruptedException {

        char[] a1 = "1234567".toCharArray();
        char[] a2 = "abcdefg".toCharArray();

        // 用来确保首先输出数字 也可以用 CountDownLatch
        AtomicBoolean flag = new AtomicBoolean(false);

        final Object lock = new Object();

        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                flag.set(true);
                for (char a : a1) {
                    log.info(String.valueOf(a));
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notify();
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                while (!flag.get()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (char a : a2) {
                    log.info(String.valueOf(a));
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notify();
            }
        });

        thread2.start();
        thread1.start();

        thread1.join();
        thread2.join();
    }
}
