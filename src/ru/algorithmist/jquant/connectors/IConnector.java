package ru.algorithmist.jquant.connectors;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public interface IConnector {

    public void load();

    public boolean canRun();

}
