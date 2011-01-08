/*
 * Copyright (c) 2011, Sergey Edunov. All Rights Reserved.
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
package ru.algorithmist.jquant;

import ru.algorithmist.jquant.signals.*;
import ru.algorithmist.jquant.engine.TimeInterval;
import ru.algorithmist.jquant.engine.IParameter;
import ru.algorithmist.jquant.engine.DataService;
import ru.algorithmist.jquant.engine.Value;
import ru.algorithmist.jquant.quotes.CloseParameter;
import ru.algorithmist.jquant.math.GrangerTest;
import ru.algorithmist.jquant.infr.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Author: Sergey Edunov
 */
public class RSICausalityAnalysis {

    static Date to = new Date(110, 11, 28);
    static Date from = new Date(102, 11, 28);

    public static void main(String[] args) throws IOException {
        Initializer.initialize();

        String symbol = "MICEX";
        TimeInterval interval = TimeInterval.DAY;
        RSISignal signal = new RSISignal(symbol, 14, interval);


        SignalProcessor processor = SignalProcessor.instance();
        RSICausalityCallback callback = new RSICausalityCallback(new CloseParameter(symbol, interval));
        processor.process(signal, from, to, callback);

        for(int d : new int[] {3,5,7}) {
            double[] y = y(callback.futureMoves(d));
            double[] x = x(callback.getSignalData());
            int cnt = 0;
            for(int i=0; i<x.length; i++){
                if (x[i]*y[i] > 0){
                    cnt++;
                }
            }
            System.out.println(cnt + "/" + x.length + " " + cnt/(double)x.length);
        }
        Initializer.dispose();
        
    }

    static double[] y(List<Boolean> futureMoves){
        double[] res = new double[futureMoves.size()];
        for(int i=0; i<futureMoves.size(); i++){
            boolean move = futureMoves.get(i);
            if (move){
                res[i] = 1;
            }else{
                res[i] = -1;
            }
        }
        return res;
    }

    static double[] x(List<BuySellSignalData> signalData){
        double[] res = new double[signalData.size()];
        for(int i=0; i<signalData.size(); i++){
            BuySellSignalData bssd = signalData.get(i);
            if (bssd.getStatus() == BuySellStatus.BUY_LONG){
                res[i] = 1;
            }else{
                res[i] = -1;
            }
        }
        return res;
    }

    static class RSICausalityCallback implements SignalCallback{

        private List<BuySellSignalData> signalData = new ArrayList<BuySellSignalData>();
        private List<Boolean>[] moveData = new List[] {new ArrayList<Boolean>(), new ArrayList<Boolean>(), new ArrayList<Boolean>(), new ArrayList<Boolean>()};
        private IParameter closeValue;

        RSICausalityCallback(IParameter closeValue) {
            this.closeValue = closeValue;
        }

        @Override
        public void signal(ISignal signal, SignalData data) {
            if (data instanceof BuySellSignalData){
                signalData.add((BuySellSignalData) data);
                Boolean r3 = realMarketMove(closeValue, data.getSignalDate(), 3);
                if (r3==null) return;
                moveData[0].add(r3);
                Boolean r5 = realMarketMove(closeValue, data.getSignalDate(), 5);
                if (r5==null) return;
                moveData[1].add(r5);
                Boolean r7 = realMarketMove(closeValue, data.getSignalDate(), 7);
                if (r7==null) return;
                moveData[2].add(r7);
                System.out.println(data.getSignalDate() + "  "  + ((BuySellSignalData) data).getStatus() + " " + r3 + " " + r5 + " " + r7);


            }
        }

        private Boolean realMarketMove(IParameter closeValue, Date signalDate, int delay) {
            double currentClose = DataService.instance().value(signalDate, closeValue).getValue();
            double futureClose = 0;
            while(delay > 0) {
                signalDate = DateUtils.shift(signalDate, closeValue.getTimeInterval(), 1);
                if (signalDate.after(to)){
                    break;
                }
                Value value = DataService.instance().value(signalDate, closeValue);
                if (value.isOK()){
                    delay--;
                    futureClose = value.getValue();
                }
            }
            return futureClose > currentClose;

        }

        public List<BuySellSignalData> getSignalData() {
            return signalData;
        }

        public List<Boolean> futureMoves(int days){
            if (days==3){
                return moveData[0];
            }
            if (days==5){
                return moveData[1];
            }
            if (days==7){
                return moveData[2];
            }
            return moveData[3];
        }
    }
}
