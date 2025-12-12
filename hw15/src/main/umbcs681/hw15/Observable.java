package umbcs681.hw15;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe Observable class.
 * My name is Xiangtao Fu, last name starts with 'F' (A-M range),
 * so I use lockObs and open call in notifyObservers().
 */
public class Observable<T> {
    private LinkedList<Observer<T>> observers = new LinkedList<>();
    private ReentrantLock lockObs = new ReentrantLock();

    public void addObserver(Observer<T> observer) {
        lockObs.lock();
        try {
            observers.add(observer);
        } finally {
            lockObs.unlock();
        }
    }

    public void removeObserver(Observer<T> observer) {
        lockObs.lock();
        try {
            observers.remove(observer);
        } finally {
            lockObs.unlock();
        }
    }

    public int countObservers() {
        lockObs.lock();
        try {
            return observers.size();
        } finally {
            lockObs.unlock();
        }
    }

    public void clearObservers() {
        lockObs.lock();
        try {
            observers.clear();
        } finally {
            lockObs.unlock();
        }
    }

    public void notifyObservers(T event) {
        List<Observer<T>> observersCopy;
        lockObs.lock();
        try {
            observersCopy = new LinkedList<>(observers);
        } finally {
            lockObs.unlock();
        }
        for (Observer<T> observer : observersCopy) {
            observer.update(this, event);
        }
    }
}
