package com.example.slowvf.Model;

import android.bluetooth.BluetoothDevice;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BluetoothItem that = (BluetoothItem) o;

        return macAddress != null ? macAddress.equals(that.macAddress) : that.macAddress == null;
    }

    @Override
    public int hashCode() {
        return macAddress != null ? macAddress.hashCode() : 0;
    }
}
