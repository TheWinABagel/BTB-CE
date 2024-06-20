package net.minecraftforge.fluids;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoaderException;
import net.minecraft.src.Block;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.Icon;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDummyContainer;

import java.util.Locale;
import java.util.Map;

public class Fluid {
   protected final String fluidName;
   /**
    * The unlocalized name of this item.
    */
   protected String unlocalizedName;
   protected Icon stillIcon;
   protected Icon flowingIcon;
   protected int luminosity = 0;
   protected int density = 1000;
   /**
    * The temperature of this biome.
    */
   protected int temperature = 295;
   protected int viscosity = 1000;
   protected boolean isGaseous;
   protected EnumRarity rarity;
   /**
    * The block ID of the Block associated with this ItemBlock
    */
   protected int blockID;
   private static Map<String, String> legacyNames = Maps.newHashMap();

   public Fluid(String fluidName) {
      this.rarity = EnumRarity.common;
      this.blockID = -1;
      this.fluidName = fluidName.toLowerCase(Locale.ENGLISH);
      this.unlocalizedName = fluidName;
   }

   /**
    * "Sets the unlocalized name of this item to the string passed as the parameter, prefixed by ""item."""
    */
   public Fluid setUnlocalizedName(String unlocalizedName) {
      this.unlocalizedName = unlocalizedName;
      return this;
   }

   public Fluid setBlockID(int blockID) {
      if (this.blockID != -1 && this.blockID != blockID) {
         if (ForgeDummyContainer.forceDuplicateFluidBlockCrash) {
            FMLLog.severe("A mod has attempted to assign BlockID " + blockID + " to the Fluid '" + this.fluidName + "' but this Fluid has already been linked to BlockID " + this.blockID + ". Configure your mods to prevent this from happening.");
            throw new LoaderException(new RuntimeException("A mod has attempted to assign BlockID " + blockID + " to the Fluid '" + this.fluidName + "' but this Fluid has already been linked to BlockID " + this.blockID + ". Configure your mods to prevent this from happening."));
         }

         FMLLog.warning("A mod has attempted to assign BlockID " + blockID + " to the Fluid '" + this.fluidName + "' but this Fluid has already been linked to BlockID " + this.blockID + ". Configure your mods to prevent this from happening.");
      } else {
         this.blockID = blockID;
      }

      return this;
   }

   public Fluid setBlockID(Block block) {
      return this.setBlockID(block.blockID);
   }

   public Fluid setLuminosity(int luminosity) {
      this.luminosity = luminosity;
      return this;
   }

   public Fluid setDensity(int density) {
      this.density = density;
      return this;
   }

   public Fluid setTemperature(int temperature) {
      this.temperature = temperature;
      return this;
   }

   public Fluid setViscosity(int viscosity) {
      this.viscosity = viscosity;
      return this;
   }

   public Fluid setGaseous(boolean isGaseous) {
      this.isGaseous = isGaseous;
      return this;
   }

   public Fluid setRarity(EnumRarity rarity) {
      this.rarity = rarity;
      return this;
   }

   /**
    * Returns the name of this game type
    */
   public final String getName() {
      return this.fluidName;
   }

   /**
    * Returns the ID of this game type
    */
   public final int getID() {
      return FluidRegistry.getFluidID(this.fluidName);
   }

   /**
    * Gets the BlockID for this BlockEventData
    */
   public final int getBlockID() {
      return this.blockID;
   }

   public final boolean canBePlacedInWorld() {
      return this.blockID != -1;
   }

   /**
    * Gets the localized name of this block. Used for the statistics page.
    */
   public String getLocalizedName() {
      String s = this.getUnlocalizedName();
      return s == null ? "" : StatCollector.translateToLocal(s);
   }

   /**
    * Returns the unlocalized name of this block.
    */
   public String getUnlocalizedName() {
      return "fluid." + this.unlocalizedName;
   }

   /**
    * "Returns 0 for /terrain.png, 1 for /gui/items.png"
    */
   public final int getSpriteNumber() {
      return 0;
   }

   public final int getLuminosity() {
      return this.luminosity;
   }

   public final int getDensity() {
      return this.density;
   }

   public final int getTemperature() {
      return this.temperature;
   }

   public final int getViscosity() {
      return this.viscosity;
   }

   public final boolean isGaseous() {
      return this.isGaseous;
   }

   /**
    * Return an item rarity from EnumRarity
    */
   public EnumRarity getRarity() {
      return this.rarity;
   }

   /**
    * Return the color for the specified armor ItemStack.
    */
   public int getColor() {
      return 16777215;
   }

   public final Fluid setStillIcon(Icon stillIcon) {
      this.stillIcon = stillIcon;
      return this;
   }

   public final Fluid setFlowingIcon(Icon flowingIcon) {
      this.flowingIcon = flowingIcon;
      return this;
   }

   public final Fluid setIcons(Icon stillIcon, Icon flowingIcon) {
      return this.setStillIcon(stillIcon).setFlowingIcon(flowingIcon);
   }

   public final Fluid setIcons(Icon commonIcon) {
      return this.setStillIcon(commonIcon).setFlowingIcon(commonIcon);
   }

   /**
    * "From the specified side and block metadata retrieves the blocks texture. Args: side, metadata"
    */
   public Icon getIcon() {
      return this.getStillIcon();
   }

   public Icon getStillIcon() {
      return this.stillIcon;
   }

   public Icon getFlowingIcon() {
      return this.flowingIcon;
   }

   public int getLuminosity(FluidStack stack) {
      return this.getLuminosity();
   }

   public int getDensity(FluidStack stack) {
      return this.getDensity();
   }

   public int getTemperature(FluidStack stack) {
      return this.getTemperature();
   }

   public int getViscosity(FluidStack stack) {
      return this.getViscosity();
   }

   public boolean isGaseous(FluidStack stack) {
      return this.isGaseous();
   }

   /**
    * Return an item rarity from EnumRarity
    */
   public EnumRarity getRarity(FluidStack stack) {
      return this.getRarity();
   }

   /**
    * Return the color for the specified armor ItemStack.
    */
   public int getColor(FluidStack stack) {
      return this.getColor();
   }

   /**
    * "From the specified side and block metadata retrieves the blocks texture. Args: side, metadata"
    */
   public Icon getIcon(FluidStack stack) {
      return this.getIcon();
   }

   public int getLuminosity(World world, int x, int y, int z) {
      return this.getLuminosity();
   }

   public int getDensity(World world, int x, int y, int z) {
      return this.getDensity();
   }

   public int getTemperature(World world, int x, int y, int z) {
      return this.getTemperature();
   }

   public int getViscosity(World world, int x, int y, int z) {
      return this.getViscosity();
   }

   public boolean isGaseous(World world, int x, int y, int z) {
      return this.isGaseous();
   }

   /**
    * Return an item rarity from EnumRarity
    */
   public EnumRarity getRarity(World world, int x, int y, int z) {
      return this.getRarity();
   }

   /**
    * Return the color for the specified armor ItemStack.
    */
   public int getColor(World world, int x, int y, int z) {
      return this.getColor();
   }

   /**
    * "From the specified side and block metadata retrieves the blocks texture. Args: side, metadata"
    */
   public Icon getIcon(World world, int x, int y, int z) {
      return this.getIcon();
   }

   static String convertLegacyName(String fluidName) {
      return fluidName != null && legacyNames.containsKey(fluidName) ? (String)legacyNames.get(fluidName) : fluidName;
   }

   public static void registerLegacyName(String legacyName, String canonicalName) {
      legacyNames.put(legacyName.toLowerCase(Locale.ENGLISH), canonicalName);
   }
}
