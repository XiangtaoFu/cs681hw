package umbcs681.hw15;

/**
 * StockHandler - handles stock events.
 * My name is Xiangtao Fu, last name starts with 'F' (A-M range),
 * so I use volatile flag for 2-step thread termination.
 */
public class StockHandler implements Observer<StockEvent>, Runnable {
    private volatile boolean done = false;
    private String handlerName;
    private StockEvent latestEvent = null;
    private final Object eventLock = new Object();

    public StockHandler(String handlerName) {
        this.handlerName = handlerName;
    }

    public void setDone() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public void update(Observable<StockEvent> sender, StockEvent event) {
        if (done) {
            return;
        }
        synchronized (eventLock) {
            latestEvent = event;
            eventLock.notifyAll();
        }
    }

    @Override
    public void run() {
        while (!done) {
            StockEvent event = null;
            synchronized (eventLock) {
                while (latestEvent == null && !done) {
                    try {
                        eventLock.wait(100);
                    } catch (InterruptedException e) {
                        if (done) {
                            return;
                        }
                    }
                }
                if (done) {
                    return;
                }
                event = latestEvent;
                latestEvent = null;
            }
            
            if (event != null && !done) {
                processEvent(event);
            }
        }
    }

    private void processEvent(StockEvent event) {
        if (done) {
            return;
        }
        System.out.println(handlerName + " received: " + event.ticker() + " = $" + event.quote());
    }

    public String getHandlerName() {
        return handlerName;
    }
}
