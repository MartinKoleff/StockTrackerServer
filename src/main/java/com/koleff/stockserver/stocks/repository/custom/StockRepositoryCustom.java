package com.koleff.stockserver.stocks.repository.custom;

import com.koleff.stockserver.stocks.repository.custom.query.DeleteByTagQueryCustom;
import com.koleff.stockserver.stocks.repository.custom.query.TruncateQueryCustom;

public interface StockRepositoryCustom extends TruncateQueryCustom, DeleteByTagQueryCustom {
    void updateIntraDayStatus(String stockTag);
    void updateEndOfDayStatus(String stockTag);
}
