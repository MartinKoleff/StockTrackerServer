package com.koleff.stockserver.stocks.dto.validation;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

public record DatabaseTableDto (
    @Pattern(regexp = "intraday|eod|exchange|tickers")
    String databaseTable
){}
