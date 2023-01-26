package dev.foggies.prisoncore.pickaxe.provider;

import dev.foggies.prisoncore.pickaxe.skin.PickaxeSkins;
import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.Arrays;
import java.util.List;

public class PickaxeSkinProvider extends UltraProvider<PickaxeSkins> {

    @Getter
    private static final PickaxeSkinProvider instance = new PickaxeSkinProvider();

    @Override
    public PickaxeSkins convert(String toConvert, UltraSender sender) throws CommandException {
        return PickaxeSkins.valueOf(toConvert);
    }

    @Override
    public List<String> tabComplete(@NonNull String toComplete, UltraSender sender) {
        return Arrays.stream(PickaxeSkins.values()).map(Enum::name).toList();
    }

    @Override
    public String getArgumentDescription() {
        return "pickaxe_skins";
    }
}
