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

import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.engine.TimeInterval;
import ru.algorithmist.jquant.engine.Value;
import ru.algorithmist.jquant.quotes.CloseParameter;
import ru.algorithmist.jquant.quotes.OpenParameter;
import ru.algorithmist.jquant.storage.Key;

import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class BottomParameter extends CalculatedParameter {

    private String symbol;
    private IParameter open;
    private IParameter close;
    private TimeInterval interval;


    public BottomParameter(String symbol, TimeInterval interval) {
        this.symbol = symbol;
        this.interval = interval;
        open = new OpenParameter(symbol, interval);
        close = new CloseParameter(symbol, interval);
    }

    @Override
    public Key getQueryKey() {
        return Key.from("Bottom", symbol, interval.getKey());
    }

    @Override
    public IParameter[] declareDependencies() {
        return new IParameter[]{open, close};
    }

    @Override
    public TimeInterval getTimeInterval() {
        return interval;
    }

    @Override
    public Value calculate(Date date) {
        Value vo = DataService.instance().value(date, open);
        Value vc = DataService.instance().value(date, close);
        if (vo.isOK() && vc.isOK()) {
            return new Value(Math.min(vo.getValue(), vc.getValue()));
        }
        if (vo.isNA() || vc.isNA()) {
            return Value.NA;
        }
        return Value.TNA;
    }
}
