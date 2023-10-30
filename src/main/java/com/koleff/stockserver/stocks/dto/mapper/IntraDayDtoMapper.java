package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class IntraDayDtoMapper implements Function<IntraDay, IntraDayDto> {

    @Override
    public IntraDayDto apply(IntraDay intraDay) {
        return new IntraDayDto(
                intraDay.getOpen(),
                intraDay.getClose(),
                intraDay.getHigh(),
                intraDay.getLow(),
                intraDay.getLast(),
                intraDay.getVolume(),
                intraDay.getDate()
        );
    }
}
