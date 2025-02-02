package buildcraft.core.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

/**
 * 
 * @author CovertJaguar <railcraft.wikispaces.com>
 */
public abstract interface IGuiReturnHandler {
	public World getWorld();

	public void writeGuiData(DataOutputStream data) throws IOException;

	public void readGuiData(DataInputStream data, EntityPlayer player) throws IOException;
}
