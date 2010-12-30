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

import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.infr.DateUtils;
import ru.algorithmist.jquant.storage.Key;

import java.util.Date;

/**
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
    public double calculate(Date date) {
        if (deep > span * 2){
            return 0;
        }
        double value = DataService.instance().value(date, base);
        if (Double.isNaN(value)) {
            return Double.NaN;
        }
        Date yesterday = date;
        double prevValue;
        int stopCounter = 30;
        deep++;
        do {
            yesterday = DateUtils.yesterday(yesterday);
            prevValue = DataService.instance().value(yesterday, this);
            stopCounter--;
        } while (Double.isNaN(prevValue) && stopCounter > 0);
        deep--;
        if (Double.isNaN(prevValue)) {
            return Double.NaN;
        }
        double alpha = 2. / (1 + span);
        return alpha * value + (1 - alpha) * prevValue;
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
}
