package ru.algorithmist.jquant.quotes;

import ru.algorithmist.jquant.engine.StockQuoteParameter;
import ru.algorithmist.jquant.engine.TimeInterval;
import ru.algorithmist.jquant.storage.Key;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class VolumeParameter extends StockQuoteParameter {

    public VolumeParameter(String symbol, TimeInterval interval) {
        super("Volume", symbol, interval);
    }

    @Override
    public Key getQueryKey() {
        return Key.from(name, symbol, "day");
    }

}
