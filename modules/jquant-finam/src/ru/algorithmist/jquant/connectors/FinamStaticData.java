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
package ru.algorithmist.jquant.connectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author "Sergey Edunov"
 * @version Nov 18, 2010
 */
public class FinamStaticData {

    private Set<Security> IDS = new HashSet<Security>();

    public FinamStaticData() {
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int get(String code){
        return getSecurity(code).id;
    }

    public Security getSecurity(String code){
        for(Security sec : IDS){
            if (code.equals(sec.code)){
                return sec;
            }
        }
        return null;
    }


    public List<Security> getSecurityByMarket(int market){
        List<Security> res = new ArrayList<Security>();
        for(Security sec : IDS){
            if (sec.market == market){
                res.add(sec);
            }
        }
        return res;
    }

    public void initialize() throws IOException {
        InputStream is = FinamStaticData.class.getResourceAsStream("fstd.txt");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String[] ids = br.readLine().split(",");
        String[] names = br.readLine().split(",");
        String[] codes = br.readLine().split(",");
        String[] markets = br.readLine().split(",");
        for(int i=0; i<ids.length; i++){
            String s = codes[i].substring(1);
            s = s.substring(0, s.length()-1);
            String name = names[i].substring(1);
            name = name.substring(0, name.length()-1);
            IDS.add(new Security(Integer.parseInt(ids[i]), Integer.parseInt(markets[i]), name, s));
        }

    }


    public static void main(String[] args) throws IOException {
        FinamStaticData fsd = new FinamStaticData();
        fsd.initialize();
        for(Security sec : fsd.getSecurityByMarket(200)){
            System.out.println(sec);
        }
    }
}

class Security{
    int id;
    int market;
    String name;
    String code;

    Security(int id, int market, String name, String code) {
        this.id = id;
        this.market = market;
        this.name = name;
        this.code = code;
    }

    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Security security = (Security) o;

        if (id != security.id) return false;
        if (market != security.market) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + market;
        return result;
    }
}