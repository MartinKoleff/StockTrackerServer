package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.Currency;
import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.dto.CurrencyDto;
import com.koleff.stockserver.stocks.dto.TimezoneDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CurrencyDtoMapper implements Function<Currency, CurrencyDto> {

    @Override
    public CurrencyDto apply(Currency currency) {
        return new CurrencyDto(
                currency.getCode(),
                currency.getSymbol(),
                currency.getName()
        );
    }
}
