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

public class UnmuteCommandTest extends AccountManagerTest {
  private static final UnmuteCommand handler =
      Mockito.mock(UnmuteCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
      SimpleCommand.Invocation invocation = Mockito.mock(SimpleCommand.Invocation.class);
      Mockito.when(invocation.arguments()).thenReturn(args);
      Mockito.when(invocation.source()).thenReturn(console);

      return handler.suggest(invocation);
  }

  @Test
  public void tabCompleteFirstArgumentTest() {
    assertEquals(Arrays.asList("test", "player1", "player2", "hello"), tabComplete(""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("p"));
    assertEquals(Collections.singletonList("player1"), tabComplete("player1"));
    assertEquals(Collections.singletonList("hello"), tabComplete("HeLl"));
    assertEquals(Collections.singletonList("test"), tabComplete("tEsT"));
    assertEquals(Collections.emptyList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Collections.emptyList(), tabComplete("player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("player1", "xxx"));

    assertEquals(Collections.emptyList(), tabComplete("player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("player1", "xxx", "xxx"));
  }
}
