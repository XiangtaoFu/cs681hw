package umbcs681.hw15;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for StockQuoteObservable with 12 data handler threads.
 * My name is Xiangtao Fu, last name starts with 'F' (A-M range),
 * so I use lockObs, open call, and 2-step termination.
 */
public class StockQuoteObservableTest {

    public static void main(String[] args) {

        StockQuoteObservable stockObservable = new StockQuoteObservable();
        List<StockHandler> handlers = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        // Create 10+ data handler threads
        int numHandlers = 12;
        for (int i = 1; i <= numHandlers; i++) {
            StockHandler handler = new StockHandler("Handler-" + i);
            handlers.add(handler);
            stockObservable.addObserver(handler);
            
            Thread thread = new Thread(handler);
            threads.add(thread);
            thread.start();
        }

        System.out.println("Started " + numHandlers + " handler threads.");
        System.out.println("Observer count: " + stockObservable.countObservers() + "\n");

        String[] tickers = {"AAPL", "GOOGL", "MSFT", "AMZN", "META"};
        double[] quotes = {150.25, 2800.50, 300.75, 3400.00, 330.25};
        
        for (int i = 0; i < tickers.length; i++) {
            stockObservable.changeQuote(tickers[i], quotes[i]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nCurrent Stock Quotes:");
        for (String ticker : tickers) {
            System.out.println(ticker + ": $" + stockObservable.getQuote(ticker));
        }

        System.out.println("\n2-Step Termination:");
        
        for (StockHandler handler : handlers) {
            handler.setDone();
        }
        
        for (Thread thread : threads) {
            thread.interrupt();
        }

        for (Thread thread : threads) {
            try {
                thread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("All handler threads terminated.");
        System.out.println("Final observer count: " + stockObservable.countObservers());
    }
}
