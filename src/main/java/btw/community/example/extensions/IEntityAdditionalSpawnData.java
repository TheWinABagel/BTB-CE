package btw.community.example.extensions;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public interface IEntityAdditionalSpawnData { //todocore additional spawn data, low prio
    void writeSpawnData(ByteArrayDataOutput byteArrayDataOutput);

    void readSpawnData(ByteArrayDataInput byteArrayDataInput);
}
