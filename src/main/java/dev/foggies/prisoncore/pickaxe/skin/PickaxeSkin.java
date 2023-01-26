package dev.foggies.prisoncore.pickaxe.skin;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.pickaxe.data.Pickaxe;
import dev.foggies.prisoncore.utils.Hex;
import dev.foggies.prisoncore.utils.SmallCaps;
import lombok.Getter;
import me.lucko.helper.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PickaxeSkin {

    private final Pickaxe pickaxe;
    private final String name;
    private final int data;
    private final PickaxeSkinBooster pickaxeSkinBooster;

    public PickaxeSkin(Pickaxe pickaxe) {
        this.pickaxe = pickaxe;
        this.data = 10;
        this.name = "Default";
        this.pickaxeSkinBooster = new PickaxeSkinBooster(this, PickaxeSkinBoosterType.ALL, 10);
    }

    public PickaxeSkin(String name, PickaxeSkinBoosterType type, int data, float percentage) {
        this.name = name;
        this.pickaxe = null;
        this.data = data;
        this.pickaxeSkinBooster = new PickaxeSkinBooster(this, type, percentage);
    }

    public PickaxeSkin(Pickaxe pickaxe, ItemStack pickaxeSkinItem, PrisonCore plugin) {
        this.pickaxe = pickaxe;
        String serialisedData = pickaxeSkinItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "pickaxe_skin"), PersistentDataType.STRING);
        String[] data = serialisedData.split(":");
        this.name = data[0];
        this.data = Integer.parseInt(data[1]);
        this.pickaxeSkinBooster = new PickaxeSkinBooster(this, data[2]);
    }

    public PickaxeSkin(Pickaxe pickaxe, String name, int data, PickaxeSkinBooster pickaxeSkinBooster) {
        this.pickaxe = pickaxe;
        this.name = name;
        this.data = data;
        this.pickaxeSkinBooster = pickaxeSkinBooster;
    }

    public PickaxeSkin(Pickaxe pickaxe, String serialisedData) {
        this.pickaxe = pickaxe;
        String[] data = serialisedData.split(":");
        this.name = data[0];
        this.data = Integer.parseInt(data[1]);
        this.pickaxeSkinBooster = new PickaxeSkinBooster(this, data[2]);
    }

    public ItemStack toItem(PrisonCore plugin) {

        List<String> lore = new ArrayList<>();
        lore.add("#227F51&l▎ &2ᴜsᴀɢᴇ");
        lore.add("&fᴛᴏ ᴀᴘᴘʟʏ ᴛʜɪs, ʏᴏᴜ ᴍᴜsᴛ ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴡʜɪʟᴇ ʜᴏʟᴅɪɴɢ");
        lore.add("&fᴀɴᴅ ɪᴛ'ʟʟ &a&nᴀᴜᴛᴏᴍᴀᴛɪᴄᴀʟʟʏ&f ɢᴏ ᴏɴᴛᴏ ʏᴏᴜʀ ᴘɪᴄᴋᴀxᴇ.");
        lore.add("");
        lore.add("#227F51&l▎ &2sᴋɪɴ ɪɴғᴏʀᴍᴀᴛɪᴏɴ");
        lore.add("  &f• ⁺" + SmallCaps.convert(pickaxeSkinBooster.getPercentage() + "% " + pickaxeSkinBooster.getType().getDisplayName()));
        lore.add("  &f• Permanent");

        return ItemStackBuilder.of(Material.SHULKER_SHELL)
                .name("&2&l" + SmallCaps.convert("Pickaxe Skin") + " &7(&e" + SmallCaps.convert(this.name) + "&7)")
                .lore(
                        Hex.translate(lore)
                )
                .transformMeta(meta -> {
                    PersistentDataContainer container = meta.getPersistentDataContainer();

                    container.set(
                            new NamespacedKey(
                                    plugin,
                                    "clickable_item"
                            ),
                            PersistentDataType.STRING,
                            "pickaxe_skin"
                    );

                    container.set(
                            new NamespacedKey(
                                    plugin,
                                    "pickaxe_skin"
                            ),
                            PersistentDataType.STRING,
                            serialise()
                    );

                    meta.setCustomModelData(3452);
                })
                .build();
    }

    public String serialise() {
        return String.format("%s:%s:%s", name, data, pickaxeSkinBooster.serialise());
    }

}
