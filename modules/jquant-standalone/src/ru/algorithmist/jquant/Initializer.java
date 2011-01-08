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

import ru.algorithmist.jquant.connectors.ConnectorProcess;
import ru.algorithmist.jquant.connectors.FinamDayTradesConnector;
import ru.algorithmist.jquant.storage.ExportImport;

import java.io.*;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class Initializer {

    private static File f = new File("resources/init.txt");

    public static void initialize() {
        FinamDayTradesConnector connector = new FinamDayTradesConnector();
        ConnectorProcess cp = ConnectorProcess.getInstance();
        cp.register(connector);

        ExportImport ei = new ExportImport();

        InputStream is = null;
        try {
            is = new FileInputStream(f);
            ei.importData(is);
        } catch (FileNotFoundException e) {

        } finally {
            try {
                if (is!=null) is.close();
            } catch (IOException e) {}
        }

    }

    public static void dispose() throws IOException {
        if (!f.exists()) {
            f.createNewFile();
        }
        ExportImport ei = new ExportImport();
        OutputStream os = null;
        try{
            os = new FileOutputStream(f);
            ei.exportData(os);
        }finally {
            if (os!=null) {
                os.close();
            }
        }

    }

}
