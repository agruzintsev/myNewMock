package com.example.newMock.controller;

import com.example.newMock.model.RequestDTO;
import com.example.newMock.model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(
            value = "info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    public Object postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            Random random = new Random();
            String clientId = requestDTO.getClientId();
            char firstChar = clientId.charAt(0);
            BigDecimal maxLimit;
            String currency;
            BigDecimal balance;

            switch (firstChar) {
                case '8':
                    maxLimit = new BigDecimal(2000);
                    currency = "USD";
                    balance = BigDecimal.valueOf(random.nextDouble()).multiply(maxLimit).setScale(2, RoundingMode.HALF_UP);
                    break;
                case '9':
                    maxLimit = new BigDecimal(1000);
                    currency = "EUR";
                    balance = BigDecimal.valueOf(random.nextDouble()).multiply(maxLimit).setScale(2, RoundingMode.HALF_UP);
                    break;
                default:
                    maxLimit = new BigDecimal(10000);
                    currency = "RUB";
                    balance = BigDecimal.valueOf(random.nextDouble()).multiply(maxLimit).setScale(2, RoundingMode.HALF_UP);
                    break;
            }

            ResponseDTO responseDTO = new ResponseDTO(
                    requestDTO.getRqUID(),
                    clientId,
                    requestDTO.getAccount(),
                    currency,
                    balance,
                    maxLimit
            );

            logger.info("\n********** RequestDTO ********** \n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            logger.info("\n********** ResponseDTO ********** \n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
