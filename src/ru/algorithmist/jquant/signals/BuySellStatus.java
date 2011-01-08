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

/**
 * Author: Sergey Edunov
 */
public enum BuySellStatus {

    BUY_LONG(3),
    SELL_SHORT(-3),
    CLOSE_LONG(-2),
    CLOSE_SHORT(2),
    KEEP_LONG(1),
    KEEP_SHORT(-1);

    private int value;

    BuySellStatus(int value){
        this.value = value;
    }

}
