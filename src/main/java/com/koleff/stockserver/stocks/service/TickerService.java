package com.koleff.stockserver.stocks.service;

import com.koleff.stockserver.stocks.domain.Ticker;
import com.koleff.stockserver.stocks.exceptions.TickerNotFoundException;
import com.koleff.stockserver.stocks.repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TickerService {

    private final TickerRepository tickerRepository;

    @Autowired
    public TickerService(TickerRepository tickerRepository){ //@Qualifier(value = "fake")
        this.tickerRepository = tickerRepository;
    }

    public List<Ticker> getTickers(){
        return tickerRepository.findAll();
    }

    public Ticker getTicker(Long id) {
        return tickerRepository
                .findById(id)
                .orElseThrow(() -> new TickerNotFoundException(
                        String.format("Ticker with id %d not found.", id)));
    }
}
