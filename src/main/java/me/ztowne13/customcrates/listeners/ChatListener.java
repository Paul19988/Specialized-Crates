package me.ztowne13.customcrates.listeners;

import me.ztowne13.customcrates.SpecializedCrates;
import me.ztowne13.customcrates.interfaces.igc.IGCMenu;
import me.ztowne13.customcrates.interfaces.igc.inputmenus.InputMenu;
import me.ztowne13.customcrates.players.PlayerManager;
import me.ztowne13.customcrates.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by ztowne13 on 3/15/16.
 */
public class ChatListener implements Listener {
    SpecializedCrates cc;

    public ChatListener(SpecializedCrates cc) {
        this.cc = cc;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (p.hasPermission("customcrates.admin") || p.hasPermission("specializedcrates.admin")) {
            PlayerManager pm = PlayerManager.get(cc, p);
            if (pm.isInOpenMenu()) {
                final IGCMenu menu = pm.getOpenMenu();
                if (menu.isInInputMenu()) {
                    final String msg = e.getMessage();
                    final InputMenu im = menu.getInputMenu();

                    Bukkit.getScheduler().runTaskLater(cc, () -> {
                        im.runFor(menu, msg);
                        p.sendMessage(ChatUtils.toChatColor(" &7&l> &f" + msg));
                    }, 1);

                    e.getRecipients().clear();
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCmiReloadListener(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPermission("customcrates.admin") || player.hasPermission("specializedcrates.admin")) {
            if (cc.getSettings().getInfoToLog().containsKey("Hologram Plugin") &&
                    cc.getSettings().getInfoToLog().get("Hologram Plugin").equalsIgnoreCase("CMI")) {
                String[] split = event.getMessage().split(" ");
                if (split.length >= 2) {
                    if (split[0].equalsIgnoreCase("/cmi") || split[0].equalsIgnoreCase("/cmi:cmi")) {
                        if (split[1].equalsIgnoreCase("reload")) {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(cc, () -> {
                                long start = System.currentTimeMillis();
                                ChatUtils.msgInfo(player,
                                        "You have executed &c/cmi reload&e. In order to keep holograms on the &6Specialized&7Crates &ecrates, &6Specialized&7Crates &eis also reloading.");
                                cc.reload();
                                ChatUtils.msgInfo(player,
                                        "Reloaded the Specialized Crate plugin &7(" + (System.currentTimeMillis() - start) +
                                                "ms)&a.");
                            }, 10);
                        }
                    }
                }
            }
        }
    }
}
