package me.ztowne13.customcrates.interfaces.externalhooks;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.ztowne13.customcrates.Messages;
import me.ztowne13.customcrates.SpecializedCrates;
import me.ztowne13.customcrates.crates.Crate;
import me.ztowne13.customcrates.crates.PlacedCrate;
import me.ztowne13.customcrates.players.PlayerDataManager;
import me.ztowne13.customcrates.players.PlayerManager;
import me.ztowne13.customcrates.players.data.VirtualCrateData;
import me.ztowne13.customcrates.utils.Utils;
import org.bukkit.entity.Player;

public class PlaceHolderAPIHandler extends PlaceholderExpansion {
    SpecializedCrates cc;

    public PlaceHolderAPIHandler(SpecializedCrates cc) {
        this.cc = cc;
    }

    public static String setPlaceHolders(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "specializedcrates";
    }

    @Override
    public String getAuthor() {
        return cc.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return cc.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // %specializedcrates_virtual_keys_[cratename]%
        // %specializedcrates_total_virtual_keys%
        // %specializedcrates_virtual_crates_[cratename]%
        // %specializedcrates_cooldown_[cratename]%
        // %specializedcrates_placedcrates%
        // %specializedcrates_last_crate_opened%
        // %specializedcrates_last_crate_opened_rewards%
        // %specializedcrates_last_player_[cratename]%
        // %specializedcrates_last_reward_[cratename]%

        if (!cc.isAllowTick())
            return "";

        if (player == null)
            return "";

        String[] args = identifier.split("_");

        PlayerManager playerManager = PlayerManager.get(cc, player);
        PlayerDataManager playerDataManager = playerManager.getPdm();

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("placedcrates"))
                return PlacedCrate.getPlacedCrates().keySet().size() + "";
        } else if (args.length == 2) {
            // specializedcrates_cooldown
            if (args[0].equalsIgnoreCase("cooldown")) {
                try {
                    // specializedcrates_cooldown_[cratename]
                    if (!Crate.exists(args[1]))
                        return "[" + args[1] + " is an invalid crate]";

                    Crate crate = Crate.getCrate(cc, args[1]);

                    if (playerDataManager.getCrateCooldownEventByCrates(crate) == null)
                        return "-";

                    int seconds = Math.round(playerDataManager.getCrateCooldownEventByCrates(crate).isCooldownOver());

                    String[] values = Utils.convertSecondToHHMMString(seconds);
                    String formatted = "";

                    if (!values[0].equalsIgnoreCase("0"))
                        formatted =
                                formatted + values[0] + " " + Messages.PLACEHOLDER_DAYS.getPropperMsg(crate.getCc()) + ", ";
                    if (!values[1].equalsIgnoreCase("00"))
                        formatted =
                                formatted + values[1] + " " + Messages.PLACEHOLDER_HOURS.getPropperMsg(crate.getCc()) + ", ";
                    if (!values[2].equalsIgnoreCase("00"))
                        formatted = formatted + values[2] + " " + Messages.PLACEHOLDER_MINUTES.getPropperMsg(crate.getCc()) +
                                ", ";
                    if (!values[3].equalsIgnoreCase("00"))
                        formatted = formatted + values[3] + " " + Messages.PLACEHOLDER_SECONDS.getPropperMsg(crate.getCc());
//                    else
//                        formatted = formatted.substring(0, formatted.length() - 2);

                    return formatted.equals("") ? "0" : formatted;
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        } else if (args.length == 3) {
            // specializedcrates_virtual
            if (args[0].equalsIgnoreCase("virtual")) {
                // specialized_virtual_..._[cratename]
                if (!Crate.exists(args[2]))
                    return "[" + args[2] + " is an invalid crate]";

                Crate crate = Crate.getCrate(cc, args[2]);

                // specializedcrates_virtual_keys_[cratename]
                if (args[1].equalsIgnoreCase("keys")) {
                    String value = "0";
                    try {
                        value = "" + playerDataManager.getVirtualCrateData().get(crate).getKeys();
                    } catch (Exception exc) {
                    }

                    return value;
                }
                // specializedcrates_virtual_crates_[cratename]
                else if (args[1].equalsIgnoreCase("crates")) {
                    String value = "0";

                    try {
                        value = playerDataManager.getVirtualCrateData().get(crate).getCrates() + "";
                    } catch (Exception exc) {
                    }

                    return value;
                }
            } else if (args[0].equalsIgnoreCase("last")) {
                if (args[1].equalsIgnoreCase("crate") && args[2].equalsIgnoreCase("opened")) {
                    if (!playerDataManager.getHistoryEvents().isEmpty())
                        return playerDataManager.getHistoryEvents().get(playerDataManager.getHistoryEvents().size() - 1)
                                .getCrates().getName();
                    else
                        return "None";
                }

                if (!Crate.exists(args[2]))
                    return "[" + args[2] + " is an invalid crate]";

                Crate crate = Crate.getCrate(cc, args[2]);

                if (args[1].equalsIgnoreCase("player"))
                    return crate.getLastOpenedName();
                else if (args[1].equalsIgnoreCase("reward"))
                    return crate.getLastOpenedReward();
            } else if (args[0].equalsIgnoreCase("total") && args[1].equalsIgnoreCase("virtual") &&
                    args[2].equalsIgnoreCase("keys")) {
                int total = 0;
                for (VirtualCrateData virtualCrateData : playerDataManager.getVirtualCrateData().values()) {
                    total += virtualCrateData.getKeys();
                }

                return total + "";
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("last") && args[1].equalsIgnoreCase("crate") &&
                    args[2].equalsIgnoreCase("opened") && args[3].equalsIgnoreCase("rewards")) {
                if (!playerDataManager.getHistoryEvents().isEmpty()) {
                    String rewards =
                            playerDataManager.getHistoryEvents().get(playerDataManager.getHistoryEvents().size() - 1)
                                    .getRewards().toString();
                    return rewards.substring(1, rewards.length() - 1);
                } else {
                    return "None";
                }
            }
        }
        return null;
    }
}
