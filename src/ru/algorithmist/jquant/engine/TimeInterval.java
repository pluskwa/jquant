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
package ru.algorithmist.jquant.engine;

/**
 * User: Sergey Edunov
 * Date: 29.12.10
 */
public enum TimeInterval {

//    TICK("tick"),
//    MIN("min"),
//    MIN10("10min"),
//    MIN5("5min"),
//    MIN15("15min"),
//    MIN30("30min"),
//    HOUR("hour"),
    DAY("day", 24*60*60*1000);
//    WEEK("week"),
//    MONTH("month");

    private String key;
    private long timeSpan;

    TimeInterval(String key, long timeSpan){
        this.key = key;
        this.timeSpan = timeSpan;
    }

    public String getKey() {
        return key;
    }

    public long getTimeSpan() {
        return timeSpan;
    }
}
