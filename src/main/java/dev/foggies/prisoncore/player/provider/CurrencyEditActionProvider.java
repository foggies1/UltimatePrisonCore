package dev.foggies.prisoncore.player.provider;

import dev.foggies.prisoncore.player.currency.CurrencyEditAction;
import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.Arrays;
import java.util.List;

public class CurrencyEditActionProvider extends UltraProvider<CurrencyEditAction> {

    @Getter
    private static final CurrencyEditActionProvider instance = new CurrencyEditActionProvider();

    @Override
    public List<String> tabComplete(@NonNull String toComplete, UltraSender sender) {
        return Arrays.stream(CurrencyEditAction.values()).map(CurrencyEditAction::name).toList();
    }

    @Override
    public CurrencyEditAction convert(String toConvert, UltraSender sender) throws CommandException {
        return CurrencyEditAction.valueOf(toConvert.toUpperCase());
    }

    @Override
    public String getArgumentDescription() {
        return "currency_edit_action";
    }
}
