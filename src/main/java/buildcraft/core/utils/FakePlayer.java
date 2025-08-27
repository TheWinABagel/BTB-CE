package buildcraft.core.utils;

import btw.world.util.data.DataEntry;
import btw.world.util.data.DataStorage;
import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

public class FakePlayer extends EntityPlayer {
    private final DataStorage dataStorage = new DataStorage();

    public FakePlayer(World par1World, int posX, int posY, int posZ) {
        this(par1World);
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public FakePlayer(World par1World) {
        super(par1World, "[Buildcraft Fake Player]");
    }

    @Override
    public <T> T getData(DataEntry.PlayerDataEntry<T> var1) {
        return dataStorage.getData(var1);
    }

    @Override
    public <T> void setData(DataEntry.PlayerDataEntry<T> var1, T var2) {
        dataStorage.setData(var1, var2);
    }

    @Override
    public void sendChatToPlayer(ChatMessageComponent var1) {
    }

    @Override
    public boolean canCommandSenderUseCommand(int var1, String var2) {
        return false;
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return null;
    }
}
