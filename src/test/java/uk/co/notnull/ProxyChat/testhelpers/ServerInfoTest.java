/*
 * ProxyChat, a Velocity chat solution
 * Copyright (C) 2020 James Lyne
 *
 * Based on BungeeChat2 (https://github.com/AuraDevelopmentTeam/BungeeChat2)
 * Copyright (C) 2020 Aura Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.co.notnull.ProxyChat.testhelpers;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import uk.co.notnull.ProxyChat.ProxyChat;
import uk.co.notnull.ProxyChat.TestHelper;
import uk.co.notnull.ProxyChat.velocity.DummyProxyServer;

public abstract class ServerInfoTest {
  @SuppressFBWarnings(
      value = {"MS_PKGPROTECT", "MS_CANNOT_BE_FINAL"},
      justification = "Child classes need access to it.")
  protected static Map<String, RegisteredServer> servers;

  @BeforeClass
  public static void setupProxyServer() {
    servers = new LinkedMap<>(); // LinkedHashMaps keep insertion order
    TestHelper.initProxyChat();

    addMockServer("main");
    addMockServer("hub1");
    addMockServer("hub2");
    addMockServer("test");
  }

  private static void addMockServer(String serverName) {
    final ServerInfo serverInfo = new ServerInfo(serverName, new InetSocketAddress(80));
    final RegisteredServer registeredServer = Mockito.mock(RegisteredServer.class);

    Mockito.when(registeredServer.getServerInfo()).thenReturn(serverInfo);

    ((DummyProxyServer) ProxyChat.getInstance().getProxy()).addServer(serverName, registeredServer);
    servers.put(serverName, registeredServer);
  }
}
