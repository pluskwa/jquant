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
package ru.algorithmist.jquant.engine;

import ru.algorithmist.jquant.connectors.ConnectorProcess;
import ru.algorithmist.jquant.storage.Key;

import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public abstract class StockQuoteParameter implements IParameter {

    protected String symbol;
    protected String name;
    private SimpleUpdater updater;
    protected TimeInterval interval;

    public StockQuoteParameter(String name, String symbol, TimeInterval interval) {
        this.symbol = symbol;
        this.name = name;
        this.interval = interval;
        updater = new SimpleUpdater(name, symbol);
    }

    @Override
    public boolean isUpdatable() {
        return true;
    }

    @Override
    public IUpdater getUpdater() {
        return updater;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockQuoteParameter that = (StockQuoteParameter) o;

        return name.equals(that.name) && symbol.equals(that.symbol);

    }

    @Override
    public int hashCode() {
        int result = symbol.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public boolean saveable() {
        return true;
    }

        @Override
    public Key getQueryKey() {
        return Key.from(name, symbol, interval.getKey());
    }

    @Override
    public TimeInterval getTimeInterval() {
        return interval;
    }

    private static class SimpleUpdater implements IUpdater{

        private String symbol;
        private String name;

        private SimpleUpdater(String name, String symbol) {
            this.symbol = symbol;
            this.name = name;
        }

        @Override
        public Value update(Date date) {
            ConnectorProcess.getInstance().update(name, symbol, date);
            return Value.TNA;
        }
    }
}
