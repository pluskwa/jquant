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
package ru.algorithmist.jquant.indicators;

import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.engine.TimeInterval;
import ru.algorithmist.jquant.engine.Value;
import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.storage.Key;

import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class RSIParameter extends CalculatedParameter  {

    private String symbol;
    private int span;
    private TimeInterval interval;
    private IParameter averageGain;
    private IParameter averageLoss;

    public RSIParameter(String symbol, int span, TimeInterval interval) {
        this.symbol = symbol;
        this.span = span;
        this.interval = interval;
        averageGain = new EMAParameter(new GainParameter(symbol, interval), span);
        averageLoss = new EMAParameter(new LossParameter(symbol, interval), span);
    }

    @Override
    public IParameter[] declareDependencies() {
        return new IParameter[]{averageGain, averageLoss};
    }

    @Override
    public Value calculate(Date date) {
        Value gain = DataService.instance().value(date, averageGain);
        Value loss = DataService.instance().value(date, averageLoss);
        if (gain.isOK() && loss.isOK()){
            if (Math.abs(loss.getValue()) < 1e-6){
                return new Value(100);
            }
            double rs = gain.getValue()/loss.getValue();
            double rsi = 100 - 100 / (1. + rs);
            return new Value(rsi);
        }
        if (gain.isNA() || loss.isNA()){
            return Value.NA;
        }
        return Value.TNA;

    }

    @Override
    public Key getQueryKey() {
        return Key.from("RSI", String.valueOf(span), symbol, interval.getKey());
    }

    @Override
    public boolean saveable() {
        return true;
    }

    @Override
    public TimeInterval getTimeInterval() {
        return interval;
    }
}
