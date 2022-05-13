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

package uk.co.notnull.ProxyChat.account;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.player.PlayerSettings;
import com.velocitypowered.api.proxy.player.ResourcePackInfo;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.ModInfo;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import uk.co.notnull.ProxyChat.ProxyChat;
import uk.co.notnull.ProxyChat.api.account.AccountInfo;
import uk.co.notnull.ProxyChat.api.account.AccountManager;
import uk.co.notnull.ProxyChat.api.account.ProxyChatAccount;
import uk.co.notnull.ProxyChat.event.ProxyChatJoinEvent;
import uk.co.notnull.ProxyChat.event.ProxyChatLeaveEvent;

import java.net.InetSocketAddress;
import uk.co.notnull.ProxyChat.api.permission.Permission;
import uk.co.notnull.ProxyChat.permission.PermissionManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProxyChatAccountManager extends AccountManager {
  private static final List<UUID> newPlayers = new LinkedList<>();

  public static Optional<ProxyChatAccount> getAccount(CommandSource player) {
    if (player instanceof Player) return getAccount(((Player) player).getUniqueId());
    else if (player == null) return Optional.empty();
    else return Optional.of(consoleAccount);
  }

  public static Optional<CommandSource> getCommandSource(UUID uuid) {
    ProxyServer instance = ProxyChat.getInstance().getProxy();

    if(uuid == null) {
      return Optional.empty();
    }

    if(uuid.equals(consoleAccount.getUniqueId())) {
      return Optional.of(instance.getConsoleCommandSource());
    } else {
      return Optional.ofNullable(instance.getPlayer(uuid).orElse(null));
    }
  }

  public static Optional<? extends CommandSource> getCommandSource(@NotNull ProxyChatAccount account) {
    ProxyServer instance = ProxyChat.getInstance().getProxy();

    switch (account.getAccountType()) {
      case PLAYER:
        return instance.getPlayer(account.getUniqueId());
      case CONSOLE:
      default:
        return Optional.of(instance.getConsoleCommandSource());
    }
  }

  public static List<ProxyChatAccount> getAccountsForPartialName(
      String partialName, CommandSource player) {
    List<ProxyChatAccount> accounts = getAccountsForPartialName(partialName);

    if (!PermissionManager.hasPermission(player, Permission.COMMAND_VANISH_VIEW)) {
      accounts =
          accounts.stream().filter(account -> !account.isVanished()).collect(Collectors.toList());
    }

    return accounts;
  }

  public static List<ProxyChatAccount> getAccountsForPartialName(
      String partialName, ProxyChatAccount account) {
    return getAccountsForPartialName(partialName, getCommandSource(account).orElse(null));
  }

  public static void loadAccount(UUID uuid) {
    AccountInfo loadedAccount = getAccountStorage().load(uuid);

    accounts.put(uuid, loadedAccount.getAccount());

    if (loadedAccount.isForceSave()) {
      saveAccount(loadedAccount.getAccount());
    }

    if (loadedAccount.isNewAccount()) {
      newPlayers.add(loadedAccount.getAccount().getUniqueId());
    }
  }

  public static void unloadAccount(UUID uuid) {
    Optional<ProxyChatAccount> account = getAccount(uuid);

    account.ifPresent(
        acc -> {
          unloadAccount(acc);
          newPlayers.remove(acc.getUniqueId());
        });
  }

  public static void unloadAccount(ProxyChatAccount account) {
    AccountManager.unloadAccount(account);
  }

  public static boolean isNew(UUID uuid) {
    return newPlayers.contains(uuid);
  }

  @Subscribe(order = PostOrder.FIRST)
  public void onPlayerConnect(ProxyChatJoinEvent event) {
    loadAccount(event.getPlayer().getUniqueId());
  }

  @Subscribe(order = PostOrder.LAST)
  public void onPlayerDisconnect(ProxyChatLeaveEvent event) {
    unloadAccount(event.getPlayer().getUniqueId());
  }
}
