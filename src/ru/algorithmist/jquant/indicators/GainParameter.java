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
package ru.algorithmist.jquant.indicators;

import ru.algorithmist.jquant.storage.Key;
import ru.algorithmist.jquant.engine.TimeInterval;
import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.engine.Value;
import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.quotes.CloseParameter;

import java.util.Date;

/**
 * Author: Sergey Edunov
 */
public class GainParameter extends CalculatedParameter {

    private String symbol;
    private TimeInterval interval;
    private IParameter close;

    public GainParameter(String symbol, TimeInterval interval){
        this.symbol = symbol;
        close = new CloseParameter(symbol, interval);
        this.interval = interval;
    }

    @Override
    public Key getQueryKey() {
        return Key.from("Gain", symbol, interval.getKey());
    }

    @Override
    public TimeInterval getTimeInterval() {
        return interval;
    }

    @Override
    public IParameter[] declareDependencies() {
        return new IParameter[]{close};
    }

    @Override
    public Value calculate(Date date) {
        int shift = -1;
        Value v1 = DataService.instance().value(date, close, shift);
        Value v2 = DataService.instance().value(date, close);
        while(v2.isOK() && v1.isNA() && shift > -30){
            v1 = DataService.instance().value(date, close, --shift);
        }
        if (v1.isOK() && v2.isOK()){
            double delta = v2.getValue() - v1.getValue();
            if (delta > 0){
                return new Value(delta);
            }
            return new Value(0);
        }
        if (v1.isNA() || v2.isNA()){
            return Value.NA;
        }
        return Value.TNA;
    }
}
