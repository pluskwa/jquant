package ru.algorithmist.jquant;

import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.engine.TimeInterval;
import ru.algorithmist.jquant.engine.Value;
import ru.algorithmist.jquant.indicators.EMAParameter;
import ru.algorithmist.jquant.infr.DateUtils;
import ru.algorithmist.jquant.math.GrangerTest;
import ru.algorithmist.jquant.quotes.CloseParameter;
import ru.algorithmist.jquant.quotes.OpenParameter;
import ru.algorithmist.jquant.storage.ExportImport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Sergey Edunov
 * Date: 06.01.11
 */
public class GrangerCausalitySample {


    public static void main(String[] args) throws IOException {
        Initializer.initialize();

        IParameter close = new CloseParameter("MICEX", TimeInterval.DAY);
        IParameter open = new OpenParameter("MICEX", TimeInterval.DAY);

        GrangerTest.granger(tod(values(close)), tod(values(open)), 1);

        GrangerTest.granger(tod(values(open)), tod(values(close)), 1);

        Initializer.dispose();
    }

    private static List<Double> values(IParameter param){
        List<Double> res = new LinkedList<Double>();
        Date to = new Date(110, 11, 30);
        Date from = new Date(108, 11, 30);
        while(from.before(to)){
            Value v = DataService.instance().value(from, param);
            if (v.isOK()){
                res.add(v.getValue());
            }
            from = DateUtils.tomorrow(from);
        }
        return res;
    }



    public static double[] tod(List<Double> data){
        double[] res = new double[data.size()];
        for(int i=0; i<res.length; i++){
            res[i] = data.get(i);
        }
        return res;
    }

    public static List<Double> delta(List<Double> in){
        List<Double> res = new ArrayList<Double>();
        for(int i=1; i<in.size(); i++){
            res.add(in.get(i)-in.get(i-1));
        }
        return res;
    }

}
