/*
 * Copyright (c) 2010, Sergey Edunov. All Rights Reserved.
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

import ru.algorithmist.jquant.engine.Value;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
@PersistenceCapable
public class GData {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private com.google.appengine.api.datastore.Key id;

    @Persistent
    private String key;

    @Persistent
    private Date date;

    @Persistent
    private double value;

    @Persistent
    private String status;

    public GData(Key key, Date date, Value value) {
        this.date = date;
        this.value = value.getValue();
        status = value.getStatus().toString();
        this.key = key.toString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Value getValue() {
        if ("OK".equals(status)){
            return new Value(value);
        }else if ("TNA".equals(status)){
            return Value.TNA;
        }
        return Value.NA;
    }

    public void setValue(Value value) {
        this.value = value.getValue();
        status = value.getStatus().toString();
    }
}
