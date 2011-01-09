/*
 * Copyright (c) 2011, Sergey Edunov. All Rights Reserved.
 *
 * This file is part of JQuant library.
 *
 * JQuant library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JQuant is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with JQuant. If not, see <http://www.gnu.org/licenses/>.
 */
package ru.algorithmist.jquant.connectors;



import ru.algorithmist.jquant.engine.TimeInterval;
import ru.algorithmist.jquant.infr.DateUtils;
import ru.algorithmist.jquant.infr.HTTPClientFactory;
import ru.algorithmist.jquant.infr.IHTTPClient;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version Jan 9, 2011
 */
public class YahooDataExporter {

    private static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    private IHTTPClient httpClient = HTTPClientFactory.getInstance().client();

    public void parseDaily(String ticker, Date from, Date to, QuoteCallback callback) throws IOException, ParseException {
        String content = httpClient.getContent("http://ichart.finance.yahoo.com/table.csv?s="
                + ticker +
                "&a=" + from.getMonth() +
                "&b=" + from.getDate() +
                "&c=" + (1900 + from.getYear()) +
                "&d=" + to.getMonth() +
                "&e=" + to.getDate() +
                "&f=" + (1900+to.getYear()) +
                "&g=d&ignore=.csv"
        );
        String[] lines = content.split("\n");
        parse(lines, callback);
    }


    private static void parse(String[] lines, QuoteCallback callback) throws ParseException {
        Date prev = null;
        if (lines[1]!=null && lines[1].contains("Yahoo! - 404 Not Found")) return;
        for(int i=1; i<lines.length; i++){
            String line = lines[i];
            prev = parse(prev, line, callback);
        }
    }



    private static Date parse(Date prev, String in, QuoteCallback callback) throws ParseException {
        try{
            String[] items = in.split(",");
            Date date = DF.parse(items[0]);
            if (prev!=null) {
                fillTheGap(prev, date, callback);
            }
            callback.setDate(date);
            callback.setOpen(Double.parseDouble(items[1]));
            callback.setHigh(Double.parseDouble(items[2]));
            callback.setLow(Double.parseDouble(items[3]));
            callback.setClose(Double.parseDouble(items[4]));
            callback.setVolume(Long.parseLong(items[5]));
            callback.setTimeInterval(TimeInterval.DAY);
            callback.commit();
            return date;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void fillTheGap(Date prev, Date date, QuoteCallback callback) {
        Date tomorrow = DateUtils.tomorrow(date);
        while (!DateUtils.isTheSameDay(prev, tomorrow)){
            callback.setDate(tomorrow);
            callback.setClose(Double.NaN);
            callback.setOpen(Double.NaN);
            callback.setHigh(Double.NaN);
            callback.setLow(Double.NaN);
            callback.setVolume(Double.NaN);
            callback.commit();
            tomorrow = DateUtils.tomorrow(tomorrow);
        }
    }
}