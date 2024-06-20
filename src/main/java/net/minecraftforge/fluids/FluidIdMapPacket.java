package net.minecraftforge.fluids;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraftforge.common.network.ForgePacket;

import java.util.Iterator;
import java.util.Map.Entry;

public class FluidIdMapPacket extends ForgePacket {
   private BiMap<String, Integer> fluidIds = HashBiMap.create();

   public byte[] generatePacket() {
      ByteArrayDataOutput dat = ByteStreams.newDataOutput();
      dat.writeInt(FluidRegistry.maxID);
      Iterator i$ = FluidRegistry.fluidIDs.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, Integer> entry = (Entry)i$.next();
         dat.writeUTF((String)entry.getKey());
         dat.writeInt((Integer)entry.getValue());
      }

      return dat.toByteArray();
   }

   public ForgePacket consumePacket(byte[] data) {
      ByteArrayDataInput dat = ByteStreams.newDataInput(data);
      int listSize = dat.readInt();

      for(int i = 0; i < listSize; ++i) {
         String fluidName = dat.readUTF();
         int fluidId = dat.readInt();
         this.fluidIds.put(fluidName, fluidId);
      }

      return this;
   }

   public void execute(INetworkManager network, EntityPlayer player) {
      FluidRegistry.initFluidIDs(this.fluidIds);
   }
}
