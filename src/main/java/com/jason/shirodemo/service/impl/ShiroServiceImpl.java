package com.jason.shirodemo.service.impl;

import com.jason.shirodemo.bean.*;
import com.jason.shirodemo.service.ShiroService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <一句话简单描述>
 * <详细介绍>
 *
 * @author lihaitao on 2019/7/23
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    @Override
    public SysRole[] findSysRoleListByUsername(String userName) {
        return new SysRole[]{new MockSysRole()};
    }

    @Override
    public SysPermission[] findSysPermissionListByRoleId(int id) {
        return new SysPermission[]{new MockSysPermission()};
    }

    @Override
    public UserInfo selectUserInfoByUsername(String username) {
        return new MockUserInfo();
    }


    public static void main(String[] args) {
        computeMap();
    }

    private static void computeMap() {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        final Map<Integer, Integer> data = new ConcurrentHashMap<>(100);
        final AtomicInteger result = new AtomicInteger(0);
        final AtomicInteger key = new AtomicInteger(0);

        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                while (cyclicBarrier.getNumberWaiting() != 0) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (cyclicBarrier.getNumberWaiting() == 0) {
                    data.put(i, i);
                    key.set(i);
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
        }

        new Thread(() -> {
            while (true) {
                if (cyclicBarrier.getNumberWaiting() == 1) {
                    result.addAndGet(data.get(key.get()));
                    System.out.println(result.get());
                    if (key.get() == 100) {
                        break;
                    }
                    try {
                        cyclicBarrier.await();
                        cyclicBarrier.reset();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
