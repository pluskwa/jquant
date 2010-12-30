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
import ru.algorithmist.jquant.engine.IUpdater;
import ru.algorithmist.jquant.engine.Value;

import java.util.Arrays;
import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public abstract class CalculatedParameter implements IParameter, ICalculator {


    public CalculatedParameter(){
    }

    @Override
    public IUpdater getUpdater() {
        return new CalculatorUpdater(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalculatedParameter that = (CalculatedParameter) o;

        return Arrays.equals(declareDependencies(), that.declareDependencies());

    }

    @Override
    public boolean saveable() {
        return false;
    }



    @Override
    public int hashCode() {
        return declareDependencies() != null ? Arrays.hashCode(declareDependencies()) : 0;
    }

    @Override
    public boolean isUpdatable() {
        for(IParameter param : declareDependencies()){
            if (!param.isUpdatable()) return false;
        }
        return true;
    }

    private  static class CalculatorUpdater implements IUpdater{

        private CalculatedParameter parameter;

        public CalculatorUpdater(CalculatedParameter parameter){
            this.parameter = parameter;

        }

        @Override
        public Value update(Date date) {
            Value value = parameter.calculate(date);
            if (parameter.saveable()) {
                DataService.instance().update(date, parameter, value);
            }
            return value;
        }
    }
}
