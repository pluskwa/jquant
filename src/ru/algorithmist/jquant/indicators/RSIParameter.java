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

import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.storage.Key;

import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class RSIParameter extends CalculatedParameter  {

    public RSIParameter(int days) {
    }

    @Override
    public IParameter[] declareDependencies() {
        return new IParameter[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double calculate(Date date) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Key getQueryKey() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean saveable() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
