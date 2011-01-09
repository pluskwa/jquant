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
package ru.algorithmist.jquant.infr.impl;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import ru.algorithmist.jquant.infr.IHTTPClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author "Sergey Edunov"
 * @version 12/29/10
 */
public class StandaloneHTTPClient implements IHTTPClient{

    private HttpClient client;
    private long lastQueryTime = 0;
    public static final long TIME_DELAY = 300;

    public StandaloneHTTPClient() {
        client = new DefaultHttpClient();
        client.getParams()
                .setParameter("User-Agent",
                        "Mozilla/5.0 (X11; Linux x86_64; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
    }

    public void setProxy(String host, int port) {
        HttpHost proxy = new HttpHost(host, port);
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    public void dispose() {
        client.getConnectionManager().shutdown();
    }

    public String getContent(String url) throws IOException {
        System.out.println(url);
        long timeSinceLastQuery = System.currentTimeMillis() - lastQueryTime;
        if (timeSinceLastQuery < 1000) {
            try {
                Thread.sleep(TIME_DELAY);
            } catch (InterruptedException e) {
            }
        }
        HttpResponse resp = client.execute(new HttpGet(url));
        HttpEntity entity = resp.getEntity();
        String encoding = EntityUtils.getContentCharSet(entity);

        if (encoding == null) {
            encoding = "UTF-8";
        }
        String result = new String(EntityUtils.toByteArray(entity), encoding);
        lastQueryTime = System.currentTimeMillis();
        return result;
    }

    public InputStream getContentStream(String url) throws IOException {
        long timeSinceLastQuery = System.currentTimeMillis() - lastQueryTime;
        if (timeSinceLastQuery < 1000) {
            try {
                Thread.sleep(TIME_DELAY);
            } catch (InterruptedException e) {
            }
        }
        HttpResponse resp = client.execute(new HttpGet(url));
        HttpEntity entity = resp.getEntity();
        lastQueryTime = System.currentTimeMillis();
        return entity.getContent();
    }


    public String getContent(String host, String url) throws IOException {
        long timeSinceLastQuery = System.currentTimeMillis() - lastQueryTime;
        if (timeSinceLastQuery < 1000) {
            try {
                Thread.sleep(TIME_DELAY);
            } catch (InterruptedException e) {
            }
        }
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        HttpResponse resp = client.execute(new HttpHost(host), new HttpGet(url));
        HttpEntity entity = resp.getEntity();
        String charSet = EntityUtils.getContentCharSet(entity);
        if (charSet == null) {
            charSet = "UTF-8";
        }
        String result = new String(EntityUtils.toByteArray(entity), charSet);
        lastQueryTime = System.currentTimeMillis();
        return result;
    }

}
