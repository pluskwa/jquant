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
package ru.algorithmist.jquant.infr;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Logger;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class GAEHTTPClient implements IHTTPClient {

    private DefaultHttpClient client;
    private static final Logger logger = Logger.getLogger(GAEHTTPClient.class.getName());


    public GAEHTTPClient(){
        SchemeRegistry schemeRegistry  = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        BasicHttpParams params = new BasicHttpParams();
        GAEConnectionManager connmgr =  new GAEConnectionManager();
        client = new DefaultHttpClient(connmgr, params);
        client.getParams()
                   .setParameter("User-Agent",
                            "Mozilla/5.0 (X11; Linux x86_64; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
    }


    public String getContent(URI url) throws IOException {
        HttpResponse resp = client.execute(new HttpGet(url));
        HttpEntity entity = resp.getEntity();
        String encoding = EntityUtils.getContentCharSet(entity);

        logger.info("Character encoding  " + encoding);
        if (encoding == null){
            encoding = "UTF-8";
        }
        return new String(EntityUtils.toByteArray(entity), encoding);
    }

    @Override
    public String getContent(String url) throws IOException {
        logger.info("Query " + url);
        HttpResponse resp = client.execute(new HttpGet(url));
        HttpEntity entity = resp.getEntity();
        String encoding = EntityUtils.getContentCharSet(entity);

        logger.info("Character encoding  " + encoding);
        if (encoding == null){
            encoding = "UTF-8";
        }
        return new String(EntityUtils.toByteArray(entity), encoding);
    }

    @Override
    public InputStream getContentStream(String url) throws IOException {
        HttpResponse resp = client.execute(new HttpGet(url));
        HttpEntity entity = resp.getEntity();
        return entity.getContent();
    }
}