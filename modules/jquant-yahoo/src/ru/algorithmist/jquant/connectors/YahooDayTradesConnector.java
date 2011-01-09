package ru.algorithmist.jquant.connectors;

import ru.algorithmist.jquant.infr.DateUtils;

import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.text.ParseException;

/**
 * Author: Sergey Edunov
 */
public class YahooDayTradesConnector  implements IConnector{

    private YahooDataExporter exporter = new YahooDataExporter();
    private static final Logger logger = Logger.getLogger(YahooDayTradesConnector.class.getName());


    @Override
    public void load(String name, String symbol, Date date) {
        System.out.println("Yahoo query for " + symbol + " for " + date);
        Date to = new Date();
        if (date.after(to)){
            throw new IllegalArgumentException("Date is not available " + date);
        }
        Date from = DateUtils.month2Before(date);
        try {
            exporter.parseDaily(symbol, from, to, new StoreQuoteCallback(symbol));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public boolean canLoad(String name, String symbol, Date date) {
        return true;//How to get list of Yahoo symbols?
    }
}
