/*
 * Copyright (c) 20010, Sergey Edunov. All Rights Reserved.
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

import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.infr.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class FinamDayTradesConnector implements IConnector{

    private FinamStaticData fstd = new FinamStaticData();
    private FinamDataExporter exporter = new FinamDataExporter();

    private static final Logger logger = Logger.getLogger(FinamDayTradesConnector.class.getName());


    private static final String[] NAMES = new String[] {"Close", "Open", "High", "Low"};


    @Override
    public void load(String name, String symbol, Date date) {
        Date from = DateUtils.month2Before(date);
        Date to = new Date();
        if (to.getHours() < 20){
            to = DateUtils.yesterday(to);
        }
        try {
            exporter.parseDaily(symbol, from, to, new StoreQuoteCallback(symbol));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public boolean canLoad(String name, String symbol, Date date) {
        Security sec = fstd.getSecurity(symbol);
        if (sec == null || !supportsName(name)){
            return false;
        }
        Date today = new Date();
        if (DateUtils.isTheSameSay(date, today)){
            if (today.getHours() < 20) return false;
        }
        if (DateUtils.isDateAfterDay(today, date)){
            return false;
        }
        return true;
    }

    private boolean supportsName(String name){
        for(String n : NAMES){
            if (n.equals(name)) return true;
        }
        return false;
    }
}
