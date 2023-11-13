package com.koleff.stockserver.stocks.repository.custom;

import com.koleff.stockserver.stocks.repository.custom.query.DeleteByTagQueryCustom;
import com.koleff.stockserver.stocks.repository.custom.query.TruncateQueryCustom;

public interface IntraDayRepositoryCustom extends DeleteByTagQueryCustom, TruncateQueryCustom {
}
