package org.example.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Locker {
    private final static Map<String, ReentrantLock> locks = new HashMap<>();

    private Locker() {
    }

    public static ReentrantLock getLock(String id) {
        if (locks.containsKey(id))
            return locks.get(id);
        else {
            locks.put(id, new ReentrantLock());
            return locks.get(id);
        }
    }
}

