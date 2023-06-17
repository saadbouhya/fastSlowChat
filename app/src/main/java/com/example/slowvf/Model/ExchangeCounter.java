package com.example.slowvf.Model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ExchangeCounter implements Serializable {
    private int transferRate = 0;
    private int receptionRate = 0;
    private int receivedMessages = 0;
    private int sentMessages = 0;
    private int tolalMessages = 0;
    private int tolalReceipts = 0;

}
