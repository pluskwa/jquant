package ru.algorithmist.jquant.storage;

import ru.algorithmist.jquant.storage.IDataStorage;
import ru.algorithmist.jquant.storage.Key;

import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class GAEDataStorage implements IDataStorage{


    @Override
    public void store(Key key, Date date, double value) {

    }

    @Override
    public double query(Key key, Date date) {
        return 0;
    }
}
