package umbcs681.hw15;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe StockQuoteObservable.
 * My name is Xiangtao Fu, last name starts with 'F' (A-M range),
 * so I use ConcurrentHashMap and inherit lockObs/open call from Observable.
 */
public class StockQuoteObservable extends Observable<StockEvent> {
    private ConcurrentHashMap<String, Double> tickerQuoteMap = new ConcurrentHashMap<>();

    public void changeQuote(String ticker, double quote) {
        tickerQuoteMap.put(ticker, quote);
        notifyObservers(new StockEvent(ticker, quote));
    }

    public Double getQuote(String ticker) {
        return tickerQuoteMap.get(ticker);
    }

    public ConcurrentHashMap<String, Double> getTickerQuoteMap() {
        return tickerQuoteMap;
    }
}
