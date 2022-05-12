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

package uk.co.notnull.ProxyChat.command;

import static org.junit.Assert.assertEquals;

import com.velocitypowered.api.command.SimpleCommand;
import uk.co.notnull.ProxyChat.testhelpers.AccountManagerTest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.mockito.Mockito;

public class IgnoreCommandTest extends AccountManagerTest {
  private static final IgnoreCommand handler =
      Mockito.mock(IgnoreCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
      SimpleCommand.Invocation invocation = Mockito.mock(SimpleCommand.Invocation.class);
      Mockito.when(invocation.arguments()).thenReturn(args);
      Mockito.when(invocation.source()).thenReturn(console);

      return handler.suggest(invocation);
  }

  @Test
  public void tabCompletefirstArgumentTest() {
    assertEquals(Arrays.asList("list", "add", "remove"), tabComplete(""));
    assertEquals(Collections.singletonList("add"), tabComplete("a"));
    assertEquals(Collections.singletonList("remove"), tabComplete("rem"));
    assertEquals(Collections.singletonList("remove"), tabComplete("remove"));
    assertEquals(Collections.singletonList("list"), tabComplete("lis"));
    assertEquals(Collections.singletonList("list"), tabComplete("list"));
    assertEquals(Collections.emptyList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteSecondArgumentTest() {
    assertEquals(Arrays.asList("test", "player1", "player2", "hello"), tabComplete("add", ""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("add", "p"));
    assertEquals(Collections.singletonList("player1"), tabComplete("add", "player1"));
    assertEquals(Collections.singletonList("hello"), tabComplete("add", "HeLl"));
    assertEquals(Collections.singletonList("test"), tabComplete("add", "tEsT"));
    assertEquals(Arrays.asList("test", "player1", "player2", "hello"), tabComplete("remove", ""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("remove", "p"));
    assertEquals(Collections.singletonList("player1"), tabComplete("remove", "player1"));
    assertEquals(Collections.singletonList("hello"), tabComplete("remove", "HeLl"));
    assertEquals(Collections.singletonList("test"), tabComplete("remove", "tEsT"));

    assertEquals(Collections.emptyList(), tabComplete("list", ""));
    assertEquals(Collections.emptyList(), tabComplete("list", "p"));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Collections.emptyList(), tabComplete("add", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("add", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("add", "player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "player1"));

    assertEquals(Collections.emptyList(), tabComplete("add", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("add", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("add", "player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "xxx", "player1"));
  }
}
