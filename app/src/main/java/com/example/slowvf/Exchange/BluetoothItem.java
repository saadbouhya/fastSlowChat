package com.example.slowvf.Exchange;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BluetoothItem implements Serializable {
    String name;
    String macAddress;
}
