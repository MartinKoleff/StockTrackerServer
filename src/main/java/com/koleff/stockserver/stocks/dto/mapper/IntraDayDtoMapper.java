package com.koleff.stockserver.stocks.dto.mapper;

import com.koleff.stockserver.stocks.domain.IntraDay;
import com.koleff.stockserver.stocks.domain.Stock;
import com.koleff.stockserver.stocks.dto.IntraDayDto;
import com.koleff.stockserver.stocks.dto.StockDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class IntraDayDtoMapper implements Function<IntraDay, IntraDayDto> {

    @Override
    public IntraDayDto apply(IntraDay intraDay) {
        return new IntraDayDto(
                intraDay.getId(),
                intraDay.getStockId(),
                intraDay.getOpen(),
                intraDay.getClose(),
                intraDay.getHigh(),
                intraDay.getLow(),
                intraDay.getVolume(),
                intraDay.getSplitFactor(),
                intraDay.getDate()
        );
    }
}
