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

import ru.algorithmist.jquant.storage.IDataStorage;
import ru.algorithmist.jquant.storage.Key;
import ru.algorithmist.jquant.storage.StorageFactory;

import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class DataService {

    private IDataStorage storage = StorageFactory.getInstance().getDataStorage();
    private static final DataService instance = new DataService();

    public static DataService instance(){
        return instance;
    }

    public double value(Date date, IParameter parameter){
        double value = storage.query(parameter.getQueryKey(), date);
        if (Double.isNaN(value) && parameter.isUpdatable()){
            value = parameter.getUpdater().update(date);
            if (Double.isNaN(value)) {
                value = storage.query(parameter.getQueryKey(), date);
            }
        }
        return value;
    }

    public void update(Date date, IParameter parameter, double value){
        storage.store(parameter.getQueryKey(), date, value);
    }
}
