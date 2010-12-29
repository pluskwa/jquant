package ru.algorithmist.jquant;

import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.indicators.EMAParameter;
import ru.algorithmist.jquant.quotes.CloseParameter;

import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class PrintStocksSample {

    public static void main(String[] args) {
        Initializer.initialize();
        Date date = new Date(110, 11, 28);
        IParameter close = new CloseParameter("SBER");
        System.out.println(DataService.instance().value(date, close));
        IParameter ema = new EMAParameter(close, 10);
        System.out.println(DataService.instance().value(date, ema));
        System.out.println(DataService.instance().value(date, ema));
    }
}
