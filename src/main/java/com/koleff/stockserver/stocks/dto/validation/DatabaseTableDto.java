package com.koleff.stockserver.stocks.dto.validation;

import jakarta.validation.constraints.Pattern;

public record DatabaseTableDto (
    @Pattern(regexp = "intraday|eod|exchange|tickers")
    String databaseTable
){}
