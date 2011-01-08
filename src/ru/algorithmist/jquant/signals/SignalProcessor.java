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

import ru.algorithmist.jquant.infr.DateUtils;

import java.util.Date;

/**
 * Author: Sergey Edunov
 */
public class SignalProcessor {

    private static SignalProcessor processor = new SignalProcessor();


    public static SignalProcessor instance(){
        return processor;
    }

    private SignalProcessor() {
    }

    public void process(ISignal signal, Date from, Date to, SignalCallback callback){
        if (to.before(from)){
            throw new IllegalArgumentException("From date should be before to date");
        }
        while(from.before(to)){
            SignalData data = signal.test(from);
            if (data.isSignal()){
                callback.signal(signal, data);
            }
            from = DateUtils.shift(from, signal.timeInterval(), 1);
        }
    }
}
