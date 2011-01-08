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
package ru.algorithmist.jquant.signals;

import ru.algorithmist.jquant.indicators.RSIParameter;
import ru.algorithmist.jquant.engine.TimeInterval;
import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.engine.Value;

import java.util.Date;

/**
 * Author: Sergey Edunov
 */
public class RSISignal implements ISignal {

    private IParameter rsi;

    public RSISignal(String symbol, int span, TimeInterval interval){
        rsi = new RSIParameter(symbol, span, interval);
    }

    @Override
    public SignalData test(Date date) {
        Value res = DataService.instance().value(date, rsi);
        if (res.isOK()){
            if (res.getValue() < 30){
                return new BuySellSignalData(date, BuySellStatus.BUY_LONG);
            }else if (res.getValue() > 70){
                return new BuySellSignalData(date, BuySellStatus.SELL_SHORT);
            }
        }
        return SignalData.none(date);
    }

    @Override
    public TimeInterval timeInterval() {
        return rsi.getTimeInterval();
    }
}
