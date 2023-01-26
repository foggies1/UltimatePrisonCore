package dev.foggies.prisoncore.pickaxe.data;

import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.pickaxe.booster.PickaxeBooster;
import dev.foggies.prisoncore.pickaxe.booster.PickaxeBoosterType;
import dev.foggies.prisoncore.pickaxe.enchants.data.PickaxeEnchants;
import dev.foggies.prisoncore.pickaxe.enchants.storage.EnchantStorage;
import dev.foggies.prisoncore.pickaxe.skin.PickaxeSkin;
import dev.foggies.prisoncore.utils.Hex;
import dev.foggies.prisoncore.utils.SmallCaps;
import lombok.Getter;
import lombok.Setter;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.time.DurationFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Pickaxe {

    private final UUID uuid;
    private PickaxeBooster pickaxeBooster;
    private PickaxeInfo pickaxeInfo;
    private PickaxeEnchants pickaxeEnchants;
    private PickaxeSkin pickaxeSkin;

    public Pickaxe(UUID uuid, EnchantStorage enchantStorage) {
        this.uuid = uuid;
        this.pickaxeBooster = new PickaxeBooster(this, PickaxeBoosterType.XP, 100000, 403.05f);
        this.pickaxeInfo = new PickaxeInfo(this);
        this.pickaxeEnchants = new PickaxeEnchants(this, enchantStorage);
        this.pickaxeSkin = null;
    }

    public Pickaxe(UUID uuid, EnchantStorage enchantStorage, String serialisedData) {
        this.uuid = uuid;
        String[] data = serialisedData.split(",");
        this.pickaxeInfo = new PickaxeInfo(this, data[0]);
        this.pickaxeBooster = data[1].equals("none") ? null : new PickaxeBooster(this, data[1]);
        this.pickaxeEnchants = new PickaxeEnchants(this, enchantStorage, data[2]);
        this.pickaxeSkin = data[3].equals("none") ? null : new PickaxeSkin(this, data[3]);
    }

    public ItemStack toItem() {

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("#227F51&l▎ #6316A0ᴘɪᴄᴋᴀxᴇ ɪɴғᴏʀᴍᴀᴛɪᴏɴ");
        lore.add("");
        lore.add("#6793D6• ʙʟᴏᴄᴋs ᴍɪɴᴇᴅ " + SmallCaps.convert(String.valueOf(pickaxeInfo.getBlocksBroken())));
        lore.add("#6793D6• ᴄᴏɪɴs ᴇᴀʀɴᴇᴅ " + SmallCaps.convert(String.valueOf(pickaxeInfo.getCoinsEarned())));
        lore.add("#6793D6• sʜᴀʀᴅs ᴇᴀʀɴᴇᴅ " + SmallCaps.convert(String.valueOf(pickaxeInfo.getShardsEarned())));
        lore.add("#6793D6• ᴏʀʙs ᴇᴀʀɴᴇᴅ " + SmallCaps.convert(String.valueOf(pickaxeInfo.getOrbsEarned())));
        lore.add("");
        lore.add("#227F51&l▎ #6316A0ᴘɪᴄᴋᴀxᴇ ᴇɴᴄʜᴀɴᴛᴍᴇɴᴛs");
        lore.add("");
        lore.addAll(pickaxeEnchants.toLore());
        lore.add("");

        if(pickaxeBooster != null || pickaxeSkin != null) {
            lore.add("#227F51&l▎ #6316A0ᴘɪᴄᴋᴀxᴇ ᴀᴛᴛʀɪʙᴜᴛᴇs");
            lore.add("");
        }

        if(pickaxeSkin != null) {
            lore.add("#57986B• ᴘɪᴄᴋᴀxᴇ sᴋɪɴ &7(&c&l" + SmallCaps.convert(pickaxeSkin.getName()) + "&7)");
            lore.add("&f ⤻ ᴛʏᴘᴇ: &cᴘᴇʀᴍᴀɴᴇɴᴛ");
            lore.add("&f ⤻ ʙᴏᴏsᴛᴇʀ: &c" + SmallCaps.convert(pickaxeSkin.getPickaxeSkinBooster().getType().getDisplayName()));
            lore.add("&f ⤻ ᴍᴜʟᴛɪᴘʟɪᴇʀ: &c⁺" + SmallCaps.convert(String.valueOf(pickaxeSkin.getPickaxeSkinBooster().getPercentage())) + "%");
            lore.add("");
        }

        if(pickaxeBooster != null) {
            lore.add("#57986B• ᴘɪᴄᴋᴀxᴇ ʙᴏᴏsᴛᴇʀ &7(&c&l" + SmallCaps.convert(DurationFormatter.format(Duration.ofSeconds(pickaxeBooster.getDuration()), true)) + "&7)");
            lore.add("&f ⤻ ᴛʏᴘᴇ: &c" + SmallCaps.convert(pickaxeBooster.getDurationType().name()));
            lore.add("&f ⤻ ʙᴏᴏsᴛᴇʀ: &c" + SmallCaps.convert(pickaxeBooster.getType().getDisplayName()));
            lore.add("&f ⤻ ᴍᴜʟᴛɪᴘʟɪᴇʀ: &c⁺" + SmallCaps.convert(String.valueOf(pickaxeBooster.getPercentage())) + "%");
        }

        return ItemStackBuilder.of(Material.DIAMOND_PICKAXE)
                .name(Hex.translate("#4A0D79" + SmallCaps.convert( toBukkit().getName() + "'s Pickaxe")))
                .lore(
                        Hex.translate(lore)
                )
                .breakable(false)
                .transformMeta(meta -> {
                    if (this.pickaxeSkin != null) {
                        meta.setCustomModelData(this.pickaxeSkin.getData());
                    }
                    meta.getPersistentDataContainer().set(
                            new NamespacedKey(PrisonCore.getProvidingPlugin(PrisonCore.class), "pickaxe"),
                            PersistentDataType.STRING,
                            "yes"
                    );
                })

                .enchant(Enchantment.DIG_SPEED, 100)
                .hideAttributes()
                .build();
    }

    public Player toBukkit() {
        return Bukkit.getPlayer(this.uuid);
    }

    public String serialise() {
        return String.format("%s,%s,%s,%s",
                this.pickaxeInfo.serialise(),
                this.pickaxeBooster == null ? "none" : this.pickaxeBooster.serialise(),
                this.pickaxeEnchants.serialise(),
                this.pickaxeSkin == null ? "none" : this.pickaxeSkin.serialise());
    }

}
