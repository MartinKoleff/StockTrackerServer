package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.Timezone;
import com.koleff.stockserver.stocks.dto.TimezoneDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TimezoneDtoMapper implements Function<Timezone, TimezoneDto> {

    @Override
    public TimezoneDto apply(Timezone timezone) {
        return new TimezoneDto(
                timezone.getTimezone(),
                timezone.getAbbreviation(),
                timezone.getAbbreviationDst()
        );
    }
}
