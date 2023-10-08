package com.koleff.stockserver.stocks.dto.wrapper;

import com.koleff.stockserver.stocks.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StocksWrapper{
    private List<Stock> stockList;
}
