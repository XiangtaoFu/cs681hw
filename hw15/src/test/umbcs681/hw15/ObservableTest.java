package umbcs681.hw15;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ObservableTest {

    private StockQuoteObservable stockObservable;

    @BeforeEach
    void setUp() {
        stockObservable = new StockQuoteObservable();
    }

    @Test
    void testAddObserver() {
        StockHandler handler = new StockHandler("TestHandler");
        stockObservable.addObserver(handler);
        assertEquals(1, stockObservable.countObservers());
    }

    @Test
    void testRemoveObserver() {
        StockHandler handler = new StockHandler("TestHandler");
        stockObservable.addObserver(handler);
        stockObservable.removeObserver(handler);
        assertEquals(0, stockObservable.countObservers());
    }

    @Test
    void testClearObservers() {
        for (int i = 0; i < 5; i++) {
            stockObservable.addObserver(new StockHandler("Handler-" + i));
        }
        assertEquals(5, stockObservable.countObservers());
        stockObservable.clearObservers();
        assertEquals(0, stockObservable.countObservers());
    }

    @Test
    void testChangeQuote() {
        stockObservable.changeQuote("AAPL", 150.00);
        assertEquals(150.00, stockObservable.getQuote("AAPL"));
    }

    @Test
    void testMultipleQuotes() {
        stockObservable.changeQuote("AAPL", 150.00);
        stockObservable.changeQuote("GOOGL", 2800.00);
        stockObservable.changeQuote("MSFT", 300.00);

        assertEquals(150.00, stockObservable.getQuote("AAPL"));
        assertEquals(2800.00, stockObservable.getQuote("GOOGL"));
        assertEquals(300.00, stockObservable.getQuote("MSFT"));
    }

    @Test
    void testStockEventRecord() {
        StockEvent event = new StockEvent("AAPL", 150.00);
        assertEquals("AAPL", event.ticker());
        assertEquals(150.00, event.quote());
    }

    @Test
    void testStockEventImmutability() {
        StockEvent event1 = new StockEvent("AAPL", 150.00);
        StockEvent event2 = new StockEvent("AAPL", 150.00);
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testConcurrentAddObservers() throws InterruptedException {
        int numThreads = 10;
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            new Thread(() -> {
                stockObservable.addObserver(new StockHandler("Handler-" + index));
                latch.countDown();
            }).start();
        }
        
        latch.await(5, TimeUnit.SECONDS);
        assertEquals(numThreads, stockObservable.countObservers());
    }

    @Test
    void testConcurrentQuoteChanges() throws InterruptedException {
        int numThreads = 10;
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            new Thread(() -> {
                stockObservable.changeQuote("STOCK-" + index, index * 100.0);
                latch.countDown();
            }).start();
        }
        
        latch.await(5, TimeUnit.SECONDS);
        assertEquals(numThreads, stockObservable.getTickerQuoteMap().size());
    }

    @Test
    void testTwoStepTermination() throws InterruptedException {
        StockHandler handler = new StockHandler("TestHandler");
        Thread thread = new Thread(handler);
        thread.start();
        
        assertFalse(handler.isDone());
        
        // Step 1: Set done flag
        handler.setDone();
        // Step 2: Interrupt thread
        thread.interrupt();
        
        thread.join(1000);
        
        assertTrue(handler.isDone());
        assertFalse(thread.isAlive());
    }

    @Test
    void testMultipleHandlerThreads() throws InterruptedException {
        List<StockHandler> handlers = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        
        // Create 10+ handlers
        for (int i = 0; i < 12; i++) {
            StockHandler handler = new StockHandler("Handler-" + i);
            handlers.add(handler);
            stockObservable.addObserver(handler);
            Thread thread = new Thread(handler);
            threads.add(thread);
            thread.start();
        }
        
        // Send some events
        stockObservable.changeQuote("TEST", 100.0);
        Thread.sleep(200);
        
        // Terminate all
        for (StockHandler handler : handlers) {
            handler.setDone();
        }
        for (Thread thread : threads) {
            thread.interrupt();
        }
        for (Thread thread : threads) {
            thread.join(1000);
        }
        
        // All threads should be terminated
        for (Thread thread : threads) {
            assertFalse(thread.isAlive());
        }
    }
}
