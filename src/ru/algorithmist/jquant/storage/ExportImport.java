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
package ru.algorithmist.jquant.storage;

import ru.algorithmist.jquant.engine.Value;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * @author "Sergey Edunov"
 * @version 12/30/10
 */
public class ExportImport {

    private static final Pattern TAB = Pattern.compile("\t");
    private DateFormat DF = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);


    public void exportData(OutputStream os) {
        IDataStorage storage = StorageFactory.getInstance().getDataStorage();
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(os));
            final BufferedWriter bwt = bw;
            storage.iterate(new DataStorageWalker() {
                @Override
                public void accept(Key key, Date date, Value value) {
                    try {
                        bwt.write(key.toString());
                        bwt.write('\t');
                        bwt.write(value.toString());
                        bwt.write('\t');
                        bwt.write(DF.format(date));
                        bwt.write('\n');
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            try {
                bw.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            if (bw!=null) {
                try {
                    bw.close();
                } catch (IOException e) {}
            }
        }
    }

    public void importData(InputStream is) {
        IDataStorage storage = StorageFactory.getInstance().getDataStorage();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] chunks = TAB.split(line);
                Key key = Key.parseString(chunks[0]);
                Value value = Value.from(chunks[1]);
                Date date = DF.parse(chunks[2]);
                storage.store(key, date, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
