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

import java.util.Date;

/**
 * Author: Sergey Edunov
 */
public class SignalData {

    private Date signalDate;

    private SignalStatus status;

    public static SignalData none(Date date){
        return new SignalData(date, SignalStatus.NONE);
    }

    public SignalData(Date signalDate, SignalStatus status) {
        this.signalDate = signalDate;
        this.status = status;
    }

    public boolean isSignal(){
        return status == SignalStatus.OK;
    }

    public Date getSignalDate() {
        return signalDate;
    }
}
