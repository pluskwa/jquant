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
import ru.algorithmist.jquant.engine.ValueStatus;
import ru.algorithmist.jquant.infr.DateUtils;
import ru.algorithmist.jquant.storage.Key;

import java.util.Date;

/**
 * <b>Exponential moving average</b> calculated parameter.
 * Calculates EMA for specified parameter with specified time span.
 * Usually in finance people are interested in EMA(Close, 11)
 *
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class EMAParameter extends CalculatedParameter {

    private IParameter base;
    private int span;
    private int deep = 0;

    public EMAParameter(IParameter base, int span) {
        if (span < 1) {
            throw new IllegalArgumentException("EMA span is too small " + span);
        }
        this.base = base;
        this.span = span;
    }

    @Override
    public IParameter[] declareDependencies() {
        return new IParameter[]{base};
    }

    @Override
    public Value calculate(Date date) {
        if (deep > span * 2){
            return new Value(0);
        }
        Value value = DataService.instance().value(date, base, 0);
        if (value.isOK()) {

            Value prevValue;
            deep++;
            int prevShift = -1;
            do {
                prevValue = DataService.instance().value(date, this, prevShift);
                prevShift--;
            } while (prevValue.isNA() && prevShift > -30);
            deep--;
            if (prevValue.isOK()) {
                double alpha = 2. / (1 + span);
                return new Value(alpha * value.getValue() + (1 - alpha) * prevValue.getValue());
            }
            return  prevValue;
        }
        return value;
    }

    @Override
    public Key getQueryKey() {
        return Key.from("EMA", String.valueOf(span))
                .addKey(base.getQueryKey());
    }

    @Override
    public boolean saveable() {
        return deep==0;
    }

    @Override
    public TimeInterval getTimeInterval() {
        return base.getTimeInterval();
    }
}
