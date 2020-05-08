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
public class Solution01_wait_notify {

    // 用来确保首先输出数字 也可以用 CountDownLatch
    static volatile Boolean numberStartFlag = false;

    public static void main(String[] args) throws InterruptedException {

        char[] a1 = "1234567".toCharArray();
        char[] a2 = "abcdefg".toCharArray();

        final Object lock = new Object();

        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                numberStartFlag = true;
                for (char a : a1) {
                    log.info(String.valueOf(a));
                    lock.notify(); // 注意：notify 不会让出锁，只是将条件队列中的线程唤醒了，使得线程去等待队列中争取锁
                    try {
                        lock.wait(); // 让出锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notify(); // 重点
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                while (!numberStartFlag) {
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
                lock.notify(); // 重点
            }
        });

        thread2.start();
        thread1.start();

        thread1.join();
        thread2.join();
    }
}
