package naughty.tuzamate.auth.hantu;

import naughty.tuzamate.auth.hantu.repository.HantuApiTokenStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
@Primary
public class HantuApiTokenInMemoryStore implements HantuApiTokenStore {

    private String accessToken;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void saveAccessToken(String accessToken) {

        lock.writeLock().lock();
        try {
            this.accessToken = accessToken;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String getAccessToken() {

        lock.readLock().lock();
        try {
            return this.accessToken;
        } finally {
            lock.readLock().unlock();
        }
    }
}
