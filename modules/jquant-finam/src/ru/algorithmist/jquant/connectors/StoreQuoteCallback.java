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

import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.quotes.CloseParameter;
import ru.algorithmist.jquant.quotes.HighParameter;
import ru.algorithmist.jquant.quotes.LowParameter;
import ru.algorithmist.jquant.quotes.OpenParameter;
import ru.algorithmist.jquant.quotes.VolumeParameter;

import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class StoreQuoteCallback implements QuoteCallback {

    private DataService dataService = DataService.instance();
    private String symbol;
    private double open;
    private double close;
    private double high;
    private double low;
    private double volume;
    private Date date;


    public StoreQuoteCallback(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public void setOpen(double value) {
        open = value;
    }

    @Override
    public void setClose(double value) {
        close = value;
    }

    @Override
    public void setHigh(double value) {
        high = value;
    }

    @Override
    public void setLow(double value) {
        low = value;
    }

    @Override
    public void setVolume(long value) {
        volume = value;
    }

    @Override
    public void setDate(Date date) {
         this.date = date;
    }

    @Override
    public void commit() {
        store(new CloseParameter(symbol), close);
        store(new OpenParameter(symbol), open);
        store(new HighParameter(symbol), high);
        store(new LowParameter(symbol), low);
        store(new VolumeParameter(symbol), volume);
    }

    public void store(IParameter parameter, double  value){
        dataService.update(date, parameter, value);
    }
}
