package dev.aura.bungeechat.util;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.aura.bungeechat.BungeeChat;
import com.typesafe.config.Config;
import dev.aura.bungeechat.config.Configuration;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServerNameUtil {
  private static Map<String, String> aliasMapping = new HashMap<>();

  public static Optional<ServerInfo> getServerInfo(String serverName) {
    Optional<RegisteredServer> server = BungeeChat.getInstance().getProxy().getAllServers().stream()
        .filter(s -> serverName.equalsIgnoreCase(s.getServerInfo().getName()))
        .findAny();

    return server.map(RegisteredServer::getServerInfo);
  }

  public static Optional<String> verifyServerName(String serverName) {
    return getServerInfo(serverName).map(ServerInfo::getName);
  }

  public static Optional<String> verifyServerName(String serverName, CommandSource sender) {
    final Optional<String> verifiedServerName = verifyServerName(serverName);

    if (!verifiedServerName.isPresent()) {
      MessagesService.sendMessage(sender, Messages.UNKNOWN_SERVER.get(sender, serverName));
    }

    return verifiedServerName;
  }

  public static List<String> getServerNames() {
    return BungeeChat.getInstance().getProxy().getAllServers().stream().map(
            server -> server.getServerInfo().getName()).collect(Collectors.toList());
  }

  public static List<String> getMatchingServerNames(String partialName) {
    return getServerNames().stream()
        .filter(serverName -> serverName.startsWith(partialName))
        .collect(Collectors.toList());
  }

  public static String getServerAlias(ServerInfo server) {
    return getServerAlias(server.getName());
  }

  public static String getServerAlias(String name) {
    if (aliasMapping.containsKey(name)) return aliasMapping.get(name);
    else return name;
  }

  public static void loadAliases() {
    Config section = Configuration.get().getConfig("ServerAlias");

    aliasMapping =
        section.root().entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey, entry -> entry.getValue().unwrapped().toString()));
    ;
  }
}