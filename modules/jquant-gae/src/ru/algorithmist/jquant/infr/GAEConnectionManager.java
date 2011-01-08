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
package ru.algorithmist.jquant.infr;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpParams;

import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class GAEConnectionManager
  implements ClientConnectionManager {

  public GAEConnectionManager() {
    SocketFactory no_socket_factory = new SocketFactory() {
	public Socket connectSocket(Socket sock, String host, int port,
				    InetAddress localAddress, int localPort,
				    HttpParams params) {
	  return null;
	}

	public Socket createSocket() {
	  return null;
	}

	public boolean isSecure(Socket s) {
	  return false;
	}
      };

    schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("http",  no_socket_factory, 80));
    schemeRegistry.register(new Scheme("https", no_socket_factory, 443));
  }


  @Override public SchemeRegistry getSchemeRegistry() {
    return schemeRegistry;
  }

  @Override public ClientConnectionRequest requestConnection(final HttpRoute route,
							     final Object state) {
    return new ClientConnectionRequest() {
      public void abortRequest() {
	// Nothing to do
      }

      public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
	return GAEConnectionManager.this.getConnection(route, state);
      }
    };
  }

  @Override public void releaseConnection(ManagedClientConnection conn,
					  long validDuration, TimeUnit timeUnit) {
  }

  @Override public void closeIdleConnections(long idletime, TimeUnit tunit) {
  }

  @Override public void closeExpiredConnections() {
  }

  @Override public void shutdown() {
  }

  private ManagedClientConnection getConnection(HttpRoute route, Object state) {
    return new GAEClientConnection(this, route, state);
  }

  private SchemeRegistry schemeRegistry;
}
