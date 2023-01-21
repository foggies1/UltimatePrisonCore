package dev.foggies.prisoncore.player.provider;

import dev.foggies.prisoncore.player.currency.CurrencyType;
import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.Arrays;
import java.util.List;

public class CurrencyProvider extends UltraProvider<CurrencyType> {

    @Getter private static final CurrencyProvider instance = new CurrencyProvider();

    @Override
    public List<String> tabComplete(@NonNull String toComplete, UltraSender sender) {
        return Arrays.stream(CurrencyType.values()).map(CurrencyType::name).toList();
    }

    @Override
    public CurrencyType convert(String toConvert, UltraSender sender) {
        return CurrencyType.valueOf(toConvert.toUpperCase());
    }

    @Override
    public String getArgumentDescription() {
        return "currency";
    }

}
