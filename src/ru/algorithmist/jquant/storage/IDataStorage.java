package ru.algorithmist.jquant.storage;

import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public interface IDataStorage {

    public void store(Key key, Date date, double value);

    public double query(Key key, Date date);

}
