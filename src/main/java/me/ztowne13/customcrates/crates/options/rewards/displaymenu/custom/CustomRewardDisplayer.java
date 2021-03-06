package me.ztowne13.customcrates.crates.options.rewards.displaymenu.custom;

import me.ztowne13.customcrates.crates.Crate;
import me.ztowne13.customcrates.crates.options.rewards.displaymenu.RewardDisplayer;
import me.ztowne13.customcrates.interfaces.InventoryBuilder;
import me.ztowne13.customcrates.interfaces.files.FileHandler;
import me.ztowne13.customcrates.interfaces.items.DynamicMaterial;
import me.ztowne13.customcrates.interfaces.items.SaveableItemBuilder;
import me.ztowne13.customcrates.interfaces.logging.StatusLoggerEvent;
import me.ztowne13.customcrates.players.PlayerManager;
import me.ztowne13.customcrates.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomRewardDisplayer extends RewardDisplayer {
    public static final String PREFIX = "reward-display.custom-display.items";

    HashMap<Integer, DisplayPage> pages = new HashMap<>();
    HashMap<String, SaveableItemBuilder> items = new HashMap<>();
    String nextPageItem;
    String prevPageItem;

    public CustomRewardDisplayer(Crate crate) {
        super(crate);
    }

    @Override
    public void open(Player p) {
        createInventory(p);
        PlayerManager.get(getCrate().getCc(), p).setInRewardMenu(true);
    }

    @Override
    public InventoryBuilder createInventory(Player p) {
        if (pages.containsKey(1))
            return pages.get(1).buildInventoryBuilder(p);
        return null;
    }

    @Override
    public void load() {
        loadDefaults();

        FileHandler fileHandler = getCrate().getSettings().getFileHandler();
        FileConfiguration fc = fileHandler.get();

        ConfigurationSection configSection;

        if (fc.isConfigurationSection(PREFIX)) {
            configSection = fc.getConfigurationSection(PREFIX);

            for (String key : configSection.getKeys(false)) {
                SaveableItemBuilder itemBuilder = new SaveableItemBuilder(DynamicMaterial.STONE, 1);
                boolean successLoadItem =
                        itemBuilder.loadItem(
                                getCrate().getSettings().getFileHandler(), PREFIX + "." + key, getCrate().getSettings().getStatusLogger(),
                                StatusLoggerEvent.SETTINGS_REWARD_DISPLAYER_ITEM_FAILURE,
                                StatusLoggerEvent.SETTINGS_REWARD_DISPLAYER_ENCHANTMENT_ADD_FAILURE,
                                StatusLoggerEvent.SETTINGS_REWARD_DISPLAYER_POTION_ADD_FAILURE,
                                StatusLoggerEvent.SETTINGS_REWARD_DISPLAYER_GLOW_FAILURE,
                                StatusLoggerEvent.SETTINGS_REWARD_DISPLAYER_AMOUNT_FAILURE,
                                StatusLoggerEvent.SETTINGS_REWARD_DISPLAYER_FLAG_FAILURE);
                if (successLoadItem)
                    items.put(key, itemBuilder);
            }
        }

        if (fc.isConfigurationSection("reward-display.custom-display")) {
            configSection = fc.getConfigurationSection("reward-display.custom-display");

            if (configSection.contains("nextpageitem"))
                nextPageItem = configSection.getString("nextpageitem");
            if (configSection.contains("lastpageitem"))
                prevPageItem = configSection.getString("lastpageitem");
        }

        if (fc.isConfigurationSection(DisplayPage.PREFIX)) {

            configSection = fc.getConfigurationSection(DisplayPage.PREFIX);

            for (String key : configSection.getKeys(false)) {
                if (Utils.isInt(key)) {
                    int pageNum = Integer.parseInt(key);
                    DisplayPage displayPage = new DisplayPage(this, pageNum);
                    boolean successLoadPage = displayPage.load();

                    if (successLoadPage)
                        pages.put(pageNum, displayPage);
                }
            }
        }
    }

    public void saveAllPages() {
        for (DisplayPage page : getPages().values()) {
            page.save();
        }

        FileConfiguration fc = getFileHandler().get();
        fc.set(PREFIX, null);

        for (String symbol : getItems().keySet()) {
            SaveableItemBuilder builder = getItems().get(symbol);
            String path = PREFIX + "." + symbol;

            builder.saveItem(getFileHandler(), path, true);
        }
    }

    public String getNextSymbol() {
        for (int i = 0; i < 1000; i++) {
            if (!getItems().containsKey(i + ""))
                return i + "";
        }

        return "-1";
    }

    public List<String> getDescriptors() {
        ArrayList<String> descriptors = new ArrayList<>();

        for (SaveableItemBuilder item : getItems().values()) {
            descriptors.add("&fItem Name: " + item.getDisplayName(true));
        }

        return descriptors;
    }

    public Map<String, SaveableItemBuilder> getItems() {
        return items;
    }

    public String getNextPageItem() {
        return nextPageItem;
    }

    public void setNextPageItem(String nextPageItem) {
        this.nextPageItem = nextPageItem;
    }

    public String getPrevPageItem() {
        return prevPageItem;
    }

    public void setPrevPageItem(String prevPageItem) {
        this.prevPageItem = prevPageItem;
    }

    public Map<Integer, DisplayPage> getPages() {
        return pages;
    }
}
