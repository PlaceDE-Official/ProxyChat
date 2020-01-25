package dev.aura.bungeechat.command;

import com.velocitypowered.api.command.CommandSource;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.StaffChatModule;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StaffChatCommand extends BaseCommand {
  public StaffChatCommand(StaffChatModule staffChatModule) {
    super("staffchat", staffChatModule.getModuleSection().getStringList("aliases"));
  }

  @Override
  public void execute(CommandSource sender, String[] args) {
    if (PermissionManager.hasPermission(sender, Permission.COMMAND_STAFFCHAT)) {
      if (args.length == 0) {
        BungeeChatAccount player = BungeecordAccountManager.getAccount(sender).get();

        if (player.getChannelType() == ChannelType.STAFF) {
          player.setChannelType(ChannelType.LOCAL);
          MessagesService.sendMessage(sender, Messages.ENABLE_LOCAL.get());
        } else {
          player.setChannelType(ChannelType.STAFF);
          MessagesService.sendMessage(sender, Messages.ENABLE_STAFFCHAT.get());
        }
      } else {
        String finalMessage = Arrays.stream(args).collect(Collectors.joining(" "));

        MessagesService.sendStaffMessage(sender, finalMessage);
      }
    }
  }
}
