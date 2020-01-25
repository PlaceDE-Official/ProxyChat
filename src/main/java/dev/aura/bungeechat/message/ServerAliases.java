package dev.aura.bungeechat.message;

import com.typesafe.config.Config;
import dev.aura.bungeechat.config.Configuration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServerAliases {
  private static Map<String, String> aliasMapping = new HashMap<>();

  public static String getServerAlias(String name) {
    return aliasMapping.getOrDefault(name, name);
  }

  public static void loadAliases() {
    Config section = Configuration.get().getConfig("ServerAlias");

    aliasMapping =
        section.root().keySet().stream().collect(Collectors.toMap(key -> key, section::getString));
  }
}
