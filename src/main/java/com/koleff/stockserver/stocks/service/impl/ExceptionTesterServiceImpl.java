package com.koleff.stockserver.stocks.service.impl;

import com.koleff.stockserver.stocks.exceptions.ApiRequestException;
import com.koleff.stockserver.stocks.exceptions.DBEmptyException;
import com.koleff.stockserver.stocks.exceptions.JsonNotFoundException;
import com.koleff.stockserver.stocks.exceptions.StockNotFoundException;
import com.koleff.stockserver.stocks.service.ExceptionTesterService;
import org.springframework.stereotype.Service;

@Service
public class ExceptionTesterServiceImpl implements ExceptionTesterService {


    @Override
    public void getError(String exceptionName) {
        switch (exceptionName){
            case "NotFound":
                throw new StockNotFoundException("StockNotFoundException");
            case "ApiRequestException":
                throw new ApiRequestException("ApiRequestException");
            case "JsonNotFoundException":
                throw new JsonNotFoundException("JsonNotFoundException");
            case "DBEmptyException":
                throw new DBEmptyException("DBEmptyException");
            default:
                throw new RuntimeException("RuntimeException");
        }
    }
}
