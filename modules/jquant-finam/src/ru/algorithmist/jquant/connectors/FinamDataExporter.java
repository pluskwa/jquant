/*
 * Copyright (c) 2010, Sergey Edunov. All Rights Reserved.
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
 * @version Nov 18, 2010
 */
public class FinamDataExporter {

    private static DateFormat DF = new SimpleDateFormat("yyyyMMddHHmmss");
    private IHTTPClient httpClient = HTTPClientFactory.getInstance().client();
    private FinamStaticData fstd = new FinamStaticData();

    public void parseDaily(String ticker, Date from, Date to, QuoteCallback callback) throws IOException, ParseException {
        int id = getId(ticker);
        String content = httpClient.getContent("http://195.128.78.52/file.txt?d=d&market=1&em="
                +id+
                "&df=" + from.getDate() +
                "&mf="+ (from.getMonth()) +
                "&yf="+ (1900 + from.getYear()) +
                "&dt=" + to.getDate() +
                "&mt=" + (to.getMonth()) +
                "&yt="+ (1900+to.getYear()) +
                "&p=8&f=file&e=.txt&cn="+ticker+
                "&dtf=1&tmf=1&MSOR=0&sep=1&sep2=1&datf=1&at=1"
        );
        String[] lines = content.split("\n");
        parse(lines, callback);
    }


    private static void parse(String[] lines, QuoteCallback callback) throws ParseException {
        Date prev = null;
        for(int i=1; i<lines.length; i++){
            String line = lines[i];
            prev = parse(prev, line, callback);
        }
    }


    private int getId(String ticker) {
        return fstd.get(ticker);
    }



    private static Date parse(Date prev, String in, QuoteCallback callback) throws ParseException {
        try{
            String[] items = in.split(",");
            Date date = DF.parse(items[2] + items[3]);
            if (prev!=null) {
                fillTheGap(prev, date, callback);
            }
            callback.setDate(date);
            callback.setOpen(Double.parseDouble(items[4]));
            callback.setHigh(Double.parseDouble(items[5]));
            callback.setLow(Double.parseDouble(items[6]));
            callback.setClose(Double.parseDouble(items[7]));
            if (items.length>8) {
                callback.setVolume(Long.parseLong(items[8].trim()));
            } else{
                callback.setVolume(Double.NaN);
            }
            callback.setTimeInterval(TimeInterval.DAY);
            callback.commit();
            return date;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void fillTheGap(Date prev, Date date, QuoteCallback callback) {
        Date yesterday = DateUtils.yesterday(date);
        while (!DateUtils.isTheSameDay(prev, yesterday)){
            callback.setDate(yesterday);
            callback.setClose(Double.NaN);
            callback.setOpen(Double.NaN);
            callback.setHigh(Double.NaN);
            callback.setLow(Double.NaN);
            callback.setVolume(Double.NaN);
            callback.commit();
            yesterday = DateUtils.yesterday(yesterday);
        }
    }
}
