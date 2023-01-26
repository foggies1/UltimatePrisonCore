package dev.foggies.prisoncore.player.items;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.factory.SphereRegionFactory;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.foggies.prisoncore.PrisonCore;
import dev.foggies.prisoncore.mine.data.Mine;
import dev.foggies.prisoncore.utils.SmallCaps;
import lombok.Getter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.scheduler.Task;
import me.lucko.helper.text3.Text;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.TimeUnit;

@Getter
public class Bomb {

    private final int radius;
    private int i = 5;
    private Item item;
    private Task task;

    public Bomb(int radius) {
        this.radius = radius;
    }

    public void throwBomb(ItemStack bombItem, Player player, Mine mine){
        ItemStack thrownItem = new ItemStack(bombItem);
        thrownItem.setAmount(1);

        this.item = player.getWorld().dropItem(player.getLocation(), thrownItem);
        this.item.setCanMobPickup(false);
        this.item.setCanPlayerPickup(false);
        this.item.setCustomNameVisible(true);
        this.item.setVelocity(player.getLocation().getDirection().multiply(2.5));

        bombItem.setAmount(bombItem.getAmount() - 1);
        begin(mine);
    }

    public void begin(Mine mine) {
        this.task = Schedulers.builder()
                .sync()
                .every(1, TimeUnit.SECONDS)
                .run(() -> {
                            if (i == 0) {
                                mine.getMineRegion().setRegion(
                                        new SphereRegionFactory().createCenteredAt(
                                                BlockVector3.at(
                                                        item.getLocation().getBlockX(),
                                                        item.getLocation().getBlockY(),
                                                        item.getLocation().getBlockZ()
                                                ),
                                                radius
                                        ),
                                        BlockTypes.AIR
                                );
                                this.item.remove();
                                this.task.close();
                            }
                            item.setCustomName(Text.colorize("&c&l" + SmallCaps.convert("Exploding in " + i + "s")));
                            i--;
                        }
                );
    }

    public ItemStack toItem(PrisonCore plugin){
        return ItemStackBuilder.of(Material.MAGMA_CREAM)
                .name("&c&l" + SmallCaps.convert("Mine Bomb") + " &7(&e&l" + SmallCaps.convert(String.valueOf(this.radius)) + "&7)")
                .lore("&7" + SmallCaps.convert("right click while holding to throw."))
                .enchant(Enchantment.DIG_SPEED)
                .hideAttributes()
                .transformMeta(meta -> {
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    container.set(
                            new NamespacedKey(plugin, "clickable_item"),
                            PersistentDataType.STRING,
                            "bomb"
                    );
                    container.set(
                            new NamespacedKey(plugin, "bomb_radius"),
                            PersistentDataType.INTEGER,
                            this.radius
                    );
                })
                .build();
    }

}
