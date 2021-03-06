package me.ztowne13.customcrates.interfaces.igc.crates;

import me.ztowne13.customcrates.SpecializedCrates;
import me.ztowne13.customcrates.crates.Crate;
import me.ztowne13.customcrates.crates.options.CHolograms;
import me.ztowne13.customcrates.crates.options.holograms.animations.HoloAnimType;
import me.ztowne13.customcrates.interfaces.InventoryBuilder;
import me.ztowne13.customcrates.interfaces.igc.IGCDefaultItems;
import me.ztowne13.customcrates.interfaces.igc.IGCListEditor;
import me.ztowne13.customcrates.interfaces.igc.IGCListSelector;
import me.ztowne13.customcrates.interfaces.igc.IGCMenu;
import me.ztowne13.customcrates.interfaces.igc.inputmenus.InputMenu;
import me.ztowne13.customcrates.interfaces.items.DynamicMaterial;
import me.ztowne13.customcrates.interfaces.items.ItemBuilder;
import me.ztowne13.customcrates.utils.ChatUtils;
import me.ztowne13.customcrates.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by ztowne13 on 4/2/16.
 * <p>
 * 4:32
 */
public class IGCCrateHolograms extends IGCMenuCrate {
    public IGCCrateHolograms(SpecializedCrates cc, Player p, IGCMenu lastMenu, Crate crates) {
        super(cc, p, lastMenu, "&7&l> &6&lHolograms", crates);
    }

    @Override
    public void openMenu() {

        InventoryBuilder ib = createDefault(27);
        CHolograms cholo = crates.getSettings().getHologram();

        ib.setItem(0, IGCDefaultItems.EXIT_BUTTON.getIb());

        // Reward Hologram
        String rewardHolo = cholo.getRewardHologram();
        ItemBuilder rewardHologram = new ItemBuilder(DynamicMaterial.LIGHT_BLUE_DYE, 1);
        rewardHologram.setName("&aEdit the reward-hologram");
        rewardHologram.addLore("&7Current value:")
                .addLore(ChatUtils.toChatColor(rewardHolo) == "" ? "&7&oNot in use" : rewardHolo).addLore("");
        rewardHologram.addAutomaticLore("&f", 30,
                "This is the hologram that appears after someone wins a reward. Type 'none' to remove it. " +
                        "Use %reward% as a placeholder for the reward name and %player% as a placeholder for the player name.");
        ib.setItem(2, rewardHologram);

        //Reward Hologram duration
        ItemBuilder rewardHoloDuration = new ItemBuilder(DynamicMaterial.PAPER, 1);
        rewardHoloDuration.setName("&aEdit the reward-hologram duration");
        rewardHoloDuration.addLore("&7Current value:").addLore("&7" + cholo.getRewardHoloDuration()).addLore("");
        rewardHoloDuration.addAutomaticLore("&f", 30, "This is the length, in ticks, the reward hologram will remain up");
        ib.setItem(11, rewardHoloDuration);

        //Reward Hologram yoffset
        ItemBuilder rewardHoloYOffset = new ItemBuilder(DynamicMaterial.BLAZE_ROD, 1);
        rewardHoloYOffset.setName("&aEdit the reward-hologram Y-Offset");
        rewardHoloYOffset.addLore("&7Current value:").addLore("&7" + cholo.getRewardHoloYOffset()).addLore("");
        rewardHoloYOffset
                .addAutomaticLore("&f", 30, "This is the amount up or down from the crate the REWARD-hologram will appear.");
        rewardHoloYOffset.addLore("")
                .addAutomaticLore("&e", 30,
                        "&6&lNOTE: &eTHIS IS FOR THE REWARD HOLOGRAM, NOT THE NORMAL/ANIMATED HOLOGRAM.");
        ib.setItem(20, rewardHoloYOffset);

        //Hologram Lines
        ItemBuilder hologramLines = new ItemBuilder(DynamicMaterial.BOOK, 1);
        hologramLines.setName("&aEdit the hologram");
        hologramLines.addLore("&7Current value:");
        for (String line : cholo.getLines()) {
            hologramLines.addLore(line);
        }
        hologramLines.addLore("").addAutomaticLore("&f", 30,
                "Edit the crate hologram. You DO NOT need to edit this value if using a hologram animation.");
        ib.setItem(4, hologramLines);

        // Green block
        boolean usingDefault = (cholo.getHat() == null || cholo.getHat().equals(HoloAnimType.NONE));
        ItemBuilder usingDefaultOrAnimate = new ItemBuilder(usingDefault ? DynamicMaterial.GREEN_WOOL :
                DynamicMaterial.RED_WOOL, 1);
        usingDefaultOrAnimate.setName("&aStatus: ");
        usingDefaultOrAnimate.addLore(usingDefault ? "&eUsing the normal hologram" : "&eUsing the animated hologram");
        usingDefaultOrAnimate.addLore("").addAutomaticLore("&f", 30, usingDefault ?
                "To use animated holograms, change the hologram animation type to something other than NONE." :
                "To stop using animated holograms, change the hologram animation type to NONE.");
        ib.setItem(9, usingDefaultOrAnimate);

        // Animated Hologram
        ItemBuilder animatedHolo = new ItemBuilder(DynamicMaterial.WRITABLE_BOOK, 1);
        animatedHolo.setName("&aEdit the animated hologram frames");
        animatedHolo.addLore("&7Frames: " + cholo.getPrefixes().size()).addLore("");
        animatedHolo.addAutomaticLore("&f", 30,
                "Each frame will play, one after the other every (whatever speed you set) ticks.")
                .addLore("").addAutomaticLore("&f", 30,
                "If there are going to be a lot of frames, editing this in the config file may be quicker.");
        ib.setItem(6, animatedHolo);

        // Animated hologram type
        ItemBuilder animatedHoloType = new ItemBuilder(DynamicMaterial.BEACON, 1);
        animatedHoloType.setName("&aEdit the hologram animation type");
        animatedHoloType.addLore("&7Current value:").addLore("&7" + cholo.getHat().name()).addLore("");
        animatedHoloType.addAutomaticLore("&f", 30,
                "Change the hologram animation type (there is currently only 1). Set to NONE to use the default holograms.");
        ib.setItem(15, animatedHoloType);

        // Animated hologram speed
        ItemBuilder animatedHoloSpeed = new ItemBuilder(DynamicMaterial.SUGAR, 1);
        animatedHoloSpeed.setName("&aEdit the animated hologram speed");
        animatedHoloSpeed.addLore("&7Current value:").addLore("&7" + cholo.getSpeed()).addLore("");
        animatedHoloSpeed.addAutomaticLore("&f", 30,
                "This is the delay between frame updates. The lower the value, the faster the frames cycle. A value of 10 is a frame update every second.");
        ib.setItem(24, animatedHoloSpeed);

        // Hologram offset
        ItemBuilder hologramOffset = new ItemBuilder(DynamicMaterial.BLAZE_ROD, 1);
        hologramOffset.setDisplayName("&aEdit the hologram Y-Offset");
        hologramOffset.addLore("&7Current value:").addLore("&7" + cs.getHologramOffset());
        hologramOffset.addLore("").addAutomaticLore("&f", 30,
                "This is the amount up or down from the crate that the NORMAL hologram will appear.");
        hologramOffset.addLore("").addAutomaticLore("&e", 30,
                "This affects the normal AND animated hologram (whichever you're using), but NOT the reward hologram");
        ib.setItem(13, hologramOffset);

        ib.open();
        putInMenu();
    }

    @Override
    public void handleClick(int slot) {
        if (slot == 0) {
            up();
        } else if (slot == 2) {
            new InputMenu(getCc(), getP(), "reward-hologram", crates.getSettings().getHologram().getRewardHologram(),
                    "Type 'none' to remove the reward hologram. Use %reward% as a placeholder for the reward and " +
                            "%player% as a placeholder for the player name or %nickname% for the player nickname.",
                    String.class,
                    this);
        } else if (slot == 11) {
            new InputMenu(getCc(), getP(), "reward-hologram-duration",
                    crates.getSettings().getHologram().getRewardHoloDuration() + "", Integer.class, this);
        } else if (slot == 20) {
            new InputMenu(getCc(), getP(), "reward-hologram-yoffset",
                    crates.getSettings().getHologram().getRewardHoloYOffset() + "", Double.class, this);
        } else if (slot == 4) {
            new IGCListEditor(getCc(), getP(), this, "Hologram Editor", "Line", cs.getHologram().getLines(),
                    DynamicMaterial.BOOK,
                    1).open();
        } else if (slot == 6) {
            new IGCListEditor(getCc(), getP(), this, "Animation Editor", "Frame", cs.getHologram().getPrefixes(),
                    DynamicMaterial.BOOK,
                    1).open();
        } else if (slot == 13) {
            new InputMenu(getCc(), getP(), "hologram-offset",
                    crates.getSettings().getHologramOffset() + "", Double.class, this);
        } else if (slot == 15) {
//            new InputMenu(getCc(), getP(), "hologram.animation.type", crates.getCs().getCholoCopy().getHat().toString(),
//                    "Animation types: " + Arrays.toString(HoloAnimType.values()), String.class, this, true);
            new IGCListSelector(getCc(), getP(), this, "Holo Animation Type", Arrays.asList(HoloAnimType.values()),
                    DynamicMaterial.PAPER, 1, HoloAnimType.descriptors()).open();
        } else if (slot == 24) {
            new InputMenu(getCc(), getP(), "hologram.animation.speed",
                    crates.getSettings().getHologram().getSpeed() + "", Integer.class, this);
        }
    }

    @Override
    public boolean handleInput(String value, String input) {
        if (value.equalsIgnoreCase("reward-hologram")) {
            if (input.equalsIgnoreCase("none") || input.equalsIgnoreCase("off") || input.equalsIgnoreCase("null")) {
                cs.getHologram().setRewardHologram("");
                return true;
            }
            cs.getHologram().setRewardHologram(ChatUtils.fromChatColor(input));
            ChatUtils.msgSuccess(getP(), "Set " + value + " to " + input);
            return true;
        } else if (value.equalsIgnoreCase("reward-hologram-duration")) {
            if (Utils.isInt(input)) {
                int newDur = Integer.parseInt(input);
                cs.getHologram().setRewardHoloDuration(newDur);
                ChatUtils.msgSuccess(getP(), "Set " + value + " to " + input);
                return true;
            } else {
                ChatUtils.msgError(getP(), input + " is not a valid whole-number.");
            }
        } else if (value.equalsIgnoreCase("reward-hologram-yoffset")) {
            if (Utils.isDouble(input)) {
                double newYOff = Double.parseDouble(input);
                cs.getHologram().setRewardHoloYOffset(newYOff);
                ChatUtils.msgSuccess(getP(), "Set " + value + " to " + input);
                return true;
            } else {
                ChatUtils.msgError(getP(), input + " is not a valid decimal-number.");
            }
        } else if (value.equalsIgnoreCase("hologram-offset")) {
            if (Utils.isDouble(input)) {
                double newYOff = Double.parseDouble(input);
                cs.setHologramOffset(newYOff);
                ChatUtils.msgSuccess(getP(), "Set " + value + " to " + input);
                return true;
            } else {
                ChatUtils.msgError(getP(), input + " is not a valid decimal-number.");
            }
        } else if (value.equalsIgnoreCase("Holo Animation Type")) {
            try {
                HoloAnimType newHoloAnim = HoloAnimType.valueOf(input.toUpperCase());
                cs.getHologram().setHat(newHoloAnim);
                ChatUtils.msgSuccess(getP(), "Set hologram.animation.type to " + input);
                return true;
            } catch (Exception exc) {
                ChatUtils.msgError(getP(), input + " is not a valid hologram animation type.");
            }
        } else if (value.equalsIgnoreCase("hologram.animation.speed")) {
            if (Utils.isInt(input)) {
                int newSpeed = Integer.parseInt(input);
                cs.getHologram().setSpeed(newSpeed);
                ChatUtils.msgSuccess(getP(), "Set " + value + " to " + input);
                return true;
            } else {
                ChatUtils.msgError(getP(), input + " is not a valid whole-number.");
            }
        }
        return false;
    }
}
