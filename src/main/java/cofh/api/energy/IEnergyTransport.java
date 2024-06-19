package cofh.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyTransport extends IEnergyProvider, IEnergyReceiver {
    int getEnergyStored(ForgeDirection var1);

    InterfaceType getTransportState(ForgeDirection var1);

    boolean setTransportState(InterfaceType var1, ForgeDirection var2);

    public static enum InterfaceType {
        SEND,
        RECEIVE,
        BALANCE;

        private InterfaceType() {
        }

        public InterfaceType getOpposite() {
            return this == BALANCE ? BALANCE : (this == SEND ? RECEIVE : SEND);
        }

        public InterfaceType rotate() {
            return this.rotate(true);
        }

        public InterfaceType rotate(boolean var1) {
            if (var1) {
                return this == BALANCE ? RECEIVE : (this == RECEIVE ? SEND : BALANCE);
            } else {
                return this == BALANCE ? SEND : (this == SEND ? RECEIVE : BALANCE);
            }
        }
    }
}
