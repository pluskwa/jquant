/*
 * Copyright (c) 20010, Sergey Edunov. All Rights Reserved.
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
package ru.algorithmist.jquant.storage;

import ru.algorithmist.jquant.storage.impl.InMemoryDataStorage;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public final class StorageFactory {

    private static final StorageFactory instance = new StorageFactory();

    public static StorageFactory getInstance(){
        return instance;
    }

    public IDataStorage dataStorage = new InMemoryDataStorage();

    private StorageFactory(){

    }

    public IDataStorage getDataStorage() {
        return dataStorage;
    }

    public void register(IDataStorage storage){
        dataStorage = storage;
    }
}
