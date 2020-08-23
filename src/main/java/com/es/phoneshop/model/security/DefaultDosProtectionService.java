package com.es.phoneshop.model.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 10;

    private Map<String, Long> countMap = new ConcurrentHashMap();


    private static class Singleton {
        private static final DefaultDosProtectionService instance = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return Singleton.instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}
