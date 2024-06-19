/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.api.core;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Implemented by classes representing serializable packet state
 */
public interface ISerializable {

    /**
     * Deserializes the state from the stream
     *
     * @param stream
     */
    void readData(DataInput stream) throws IOException ;

    /**
     * Serializes the state to the stream
     *
     * @param stream
     */
    void writeData(DataOutput stream) throws IOException;
}
