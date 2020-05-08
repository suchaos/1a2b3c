package com.suchaos.a1b2c3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 解法2
 *
 * @author suchao
 * @date 2020/5/7
 */
@Slf4j
public class Solution02_lock_condition {

    public static void main(String[] args) throws InterruptedException {

        char[] a1 = "1234567".toCharArray();
        char[] a2 = "abcdefg".toCharArray();

        Lock lock = new ReentrantLock();
        Condition number = lock.newCondition();
        Condition letter = lock.newCondition();

        CountDownLatch latch = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            latch.countDown();
            lock.lock();
            try {
                for (char a : a1) {
                    log.info(String.valueOf(a));
                    letter.signal();
                    number.await();
                }
                letter.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            try {
                for (char a : a2) {
                    log.info(String.valueOf(a));
                    number.signal();
                    letter.await();
                }
                number.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        thread2.start();
        thread1.start();

        thread1.join();
        thread2.join();
    }
}
