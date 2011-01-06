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
package ru.algorithmist.jquant.infr;

import ru.algorithmist.jquant.engine.TimeInterval;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: Sergey Edunov
 * Date: 09.12.10
 */
public class DateUtils {

    public static boolean isTheSameDay(Date a, Date b) {
        return a.getDate() == b.getDate() && a.getYear() == b.getYear() && a.getMonth() == b.getMonth();
    }

    public static boolean isDateAfterDay(Date a, Date b){
        return b.getYear() >= a.getYear() && b.getMonth() >= a.getMonth() && b.getDate() >= a.getDate();
    }

    public static Date shift(Date from, TimeInterval interval, int shift){
        if (interval == TimeInterval.DAY){
            return shiftDays(from, shift);
        }
        throw new RuntimeException("Not implemented");
    }

    public static Date monthBefore(Date today){
        Date from = (Date) today.clone();
        if (from.getMonth() > 0){
            from.setMonth(today.getMonth() - 1);
        }else {
            from.setYear(today.getYear() - 1);
            from.setMonth(11);
        }
        return from;
    }

    public static Date month2Before(Date today){
        Date from = (Date) today.clone();
        if (from.getMonth() > 1){
            from.setMonth(today.getMonth() - 2);
        }else if (from.getMonth()==1) {
            from.setYear(today.getYear() - 1);
            from.setMonth(11);
        }else{
            from.setYear(today.getYear() - 1);
            from.setMonth(10);
        }
        return from;
    }

    public static Date shiftDays(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static Date yesterday(Date today){
        return shiftDays(today, -1);
    }

    public static Date tomorrow(Date today){
        return shiftDays(today, 1);
    }


    public static Date years10Before(Date today){
        Date from = (Date) today.clone();
        from.setYear(from.getYear()-10);
        return from;
    }

}
