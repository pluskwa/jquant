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

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class GAEDataStorage implements IDataStorage {

    private static final Logger logger = Logger.getLogger(GAEDataStorage.class.getName());

    private PersistenceManagerFactory pmf;

    public GAEDataStorage() {
        pmf = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    }

    @Override
    public void store(Key key, Date date, double value) {
        PersistenceManager pm = null;
        try {
            pm = pmf.getPersistenceManager();
            List<GData> res = (List<GData>) lookup(pm).execute(key.toString(), date);
            if (res == null || res.isEmpty()) {
                pm.makePersistent(new GData(key, date, value));
            } else {
                if (res.size() > 1) {
                    logger.log(Level.WARNING, "Query returned more than one instance " + key + ' ' + date);
                }
                res.get(0).setValue(value);
            }
        } finally {
            if (pm != null) {
                pm.close();
            }
        }
    }

    @Override
    public double query(Key key, Date date) {
        PersistenceManager pm = null;
        try {
            pm = pmf.getPersistenceManager();
            List<GData> res = (List<GData>) lookup(pm).execute(key.toString(), date);
            if (res == null || res.isEmpty()) {
                return Double.NaN;
            }
            if (res.size() > 1) {
                logger.log(Level.WARNING, "Query returned more than one instance " + key + ' ' + date);
            }
            return res.get(0).getValue();
        } finally {
            if (pm != null) {
                pm.close();
            }
        }

    }

    private Query lookup(PersistenceManager manager) {
        Query lookup = manager.newQuery(GData.class);
        lookup.setFilter("key == keyParam");
        lookup.setFilter("date == dateParam");
        lookup.declareImports("import java.util.Date");
        lookup.declareParameters("String key");
        lookup.declareParameters("Date dateParam");
        return lookup;
    }

}
