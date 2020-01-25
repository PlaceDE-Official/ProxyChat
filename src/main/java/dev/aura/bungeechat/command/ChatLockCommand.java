package dev.aura.bungeechat.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.module.ChatLockModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;

public class ChatLockCommand extends BaseCommand {
  private static final String USAGE = "/chatlock <local|global> [clear]";

  public ChatLockCommand(ChatLockModule chatLockModule) {
    super("chatlock", chatLockModule.getModuleSection().getStringList("aliases"));
  }

  @Override
  public void execute(CommandSource sender, String[] args) {
    if (PermissionManager.hasPermission(sender, Permission.COMMAND_CHAT_LOCK)) {
      if (!(sender instanceof Player)) {
        MessagesService.sendMessage(sender, Messages.NOT_A_PLAYER.get());
      } else {
        BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

        if (args.length < 1) {
          MessagesService.sendMessage(sender, Messages.INCORRECT_USAGE.get(player, USAGE));
        } else {
          final ChatLockModule chatLock = BungeecordModuleManager.CHAT_LOCK_MODULE;
          final boolean clear = (args.length >= 2) && args[1].equalsIgnoreCase("clear");
          final int emptyLines =
              clear ? chatLock.getModuleSection().getInt("emptyLinesOnClear") : 0;

          if (args[0].equalsIgnoreCase("global")) {
            if (chatLock.isGlobalChatLockEnabled()) {
              chatLock.disableGlobalChatLock();
              MessagesService.sendMessage(sender, Messages.DISABLE_CHATLOCK.get(player));
            } else {
              chatLock.enableGlobalChatLock();

              if (clear) {
                ClearChatCommand.clearGlobalChat(emptyLines);
              }

              MessagesService.sendToMatchingPlayers(
                  Messages.ENABLE_CHATLOCK.get(player), MessagesService.getGlobalPredicate());
            }
          } else if (args[0].equalsIgnoreCase("local")) {
            String serverName = player.getServerName();

            if (chatLock.isLocalChatLockEnabled(serverName)) {
              chatLock.disableLocalChatLock(serverName);
              MessagesService.sendMessage(sender, Messages.DISABLE_CHATLOCK.get(player));
            } else {
              chatLock.enableLocalChatLock(serverName);

              if (clear) {
                ClearChatCommand.clearLocalChat(serverName, emptyLines);
              }

              MessagesService.sendToMatchingPlayers(
                  Messages.ENABLE_CHATLOCK.get(player),
                  MessagesService.getLocalPredicate(serverName));
            }
          } else {
            MessagesService.sendMessage(sender, Messages.INCORRECT_USAGE.get(player, USAGE));
          }
        }
      }
    }
  }
}
