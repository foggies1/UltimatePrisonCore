package dev.foggies.prisoncore.pickaxe.enchants.storage;

import dev.foggies.prisoncore.api.Storage;
import dev.foggies.prisoncore.pickaxe.enchants.api.AbstractEnchant;
import lombok.Getter;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

@Getter
public class EnchantStorage extends Storage<String, AbstractEnchant> {

    public EnchantStorage() {
        loadAll();
    }

    public void loadAll() {
        Reflections reflections = new Reflections("dev.foggies.prisoncore.pickaxe.enchants.impl");
        reflections.getSubTypesOf(AbstractEnchant.class).forEach(enchant -> {
            try {
                AbstractEnchant abstractEnchant = enchant.getDeclaredConstructor().newInstance();
                this.add(abstractEnchant.getIdentifier(), abstractEnchant);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
