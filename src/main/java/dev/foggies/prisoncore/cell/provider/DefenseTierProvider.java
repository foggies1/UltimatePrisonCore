package dev.foggies.prisoncore.cell.provider;

import dev.foggies.prisoncore.cell.defense.DefenseBlockTier;
import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefenseTierProvider extends UltraProvider<DefenseBlockTier> {

    @Getter
    private static final DefenseTierProvider instance = new DefenseTierProvider();

    @Override
    public DefenseBlockTier convert(String toConvert, UltraSender sender) throws CommandException {
        return DefenseBlockTier.valueOf(toConvert.toUpperCase());
    }

    @Override
    public List<String> tabComplete(@NonNull String toComplete, UltraSender sender) {
        return Arrays.stream(DefenseBlockTier.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public String getArgumentDescription() {
        return "Defense Tier";
    }
}
