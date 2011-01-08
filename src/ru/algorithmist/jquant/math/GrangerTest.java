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
package ru.algorithmist.jquant.math;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.FDistribution;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.stat.regression.OLSMultipleLinearRegression;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Implementation of Granger causality test in pure Java.
 * It is base on linear regression implementation in Apache Commons Math.
 *
 * User: Sergey Edunov
 * Date: 06.01.11
 */
public class GrangerTest {

    /**
     * Returns p-value for Granger causality test.
     *
     * @param y - predictable variable
     * @param x - predictor
     * @param L - lag, should be 1 or greater.
     * @return p-value of Granger causality
     */
    public static double granger(double[] y, double[] x, int L){
        OLSMultipleLinearRegression h0 = new OLSMultipleLinearRegression();
        OLSMultipleLinearRegression h1 = new OLSMultipleLinearRegression();

        double[][] laggedY = createLaggedSide(L, y);

        double[][] laggedXY = createLaggedSide(L, x, y);

        int n = laggedY.length;

        h0.newSampleData(strip(L, y), laggedY);
        h1.newSampleData(strip(L, y), laggedXY);

        double rs0[] = h0.estimateResiduals();
        double rs1[] = h1.estimateResiduals();


        double RSS0 = sqrSum(rs0);
        double RSS1 = sqrSum(rs1);

        double ftest = ((RSS0 - RSS1)/L) / (RSS1 / ( n - 2*L - 1));

        System.out.println(RSS0 + " " + RSS1);
        System.out.println("F-test " + ftest);

        FDistribution fDist = new FDistributionImpl(L, n-2*L-1);
        try {
            double pValue = 1.0 - fDist.cumulativeProbability(ftest);
            System.out.println("P-value " + pValue);
            return  pValue;
        } catch (MathException e) {
            throw new RuntimeException(e);
        }

    }


    private static double[][] createLaggedSide(int L, double[]... a) {
        int n = a[0].length - L;
        double[][] res = new double[n][L*a.length+1];
        for(int i=0; i<a.length; i++){
            double[] ai = a[i];
            for(int l=0; l<L; l++){
                for(int j=0; j<n; j++){
                    res[j][i*L+l] = ai[l+j];
                }
            }
        }
        for(int i=0; i<n; i++){
            res[i][L*a.length] = 1;
        }
        return res;
    }

    public static double sqrSum(double[] a){
        double res = 0;
        for(double v : a){
            res+=v*v;
        }
        return res;
    }


     public static double[] strip(int l, double[] a){

        double[] res = new double[a.length-l];
        System.arraycopy(a, l, res, 0, res.length);
        return res;
    }




}
