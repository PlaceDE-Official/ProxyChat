package dev.aura.bungeechat.module.perms;

import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.hook.LuckPerms5Hook;
import dev.aura.bungeechat.util.ClassUtil;

public class LuckPerms5Module extends PermissionPluginModule {
  @Override
  public String getName() {
    return "LuckPerms";
  }

  @Override
  public boolean isEnabled() {
    return super.isEnabled() && ClassUtil.doesClassExist("me.lucko.luckperms.LuckPerms");
  }

  @Override
  public void onEnable() {
    HookManager.addHook(getName(), new LuckPerms5Hook());
  }

  @Override
  public void onDisable() {
    HookManager.removeHook(getName());
  }
}
