package ru.algorithmist.jquant;

import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.engine.TimeInterval;
import ru.algorithmist.jquant.engine.Value;
import ru.algorithmist.jquant.indicators.EMAParameter;
import ru.algorithmist.jquant.indicators.RSIParameter;
import ru.algorithmist.jquant.quotes.CloseParameter;
import ru.algorithmist.jquant.storage.ExportImport;
import ru.algorithmist.jquant.infr.DateUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class RSISample {

    public static void main(String[] args) throws IOException {
        Initializer.initialize();
        Date date = new Date(110, 11, 28);
        IParameter rsi = new RSIParameter("MICEX", 7, TimeInterval.DAY);
//        IParameter rsi = new EMAParameter(new CloseParameter("SBER", TimeInterval.DAY), 7);
        for(int i=100; i>=0; i--){
            Date d = DateUtils.shift(date, TimeInterval.DAY, -i);
            Value v = DataService.instance().value(d, rsi);
            if (v.isOK()){
                System.out.println(d + " " + v.getValue());
            }
        }

        Initializer.dispose();
    }


}