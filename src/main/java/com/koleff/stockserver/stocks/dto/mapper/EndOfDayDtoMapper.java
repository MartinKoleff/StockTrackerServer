package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.EndOfDay;
import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.dto.EndOfDayDto;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Component
public class EndOfDayDtoMapper implements Function<EndOfDay, EndOfDayDto> {

    @Override
    public EndOfDayDto apply(EndOfDay endOfDay) {
        return new EndOfDayDto(
                endOfDay.getId(),
                endOfDay.getStockId(),
                endOfDay.getOpen(),
                endOfDay.getClose(),
                endOfDay.getHigh(),
                endOfDay.getLow(),
                endOfDay.getVolume(),
                endOfDay.getAdjOpen(),
                endOfDay.getAdjClose(),
                endOfDay.getAdjHigh(),
                endOfDay.getAdjLow(),
                endOfDay.getAdjVolume(),
                endOfDay.getSplitFactor(),
                endOfDay.getDate()
        );
    }
}
