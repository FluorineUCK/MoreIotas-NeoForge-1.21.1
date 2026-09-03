package ram.talia.moreiotas;

import at.petrak.hexcasting.common.lib.HexRegistries;
import com.mojang.logging.LogUtils;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.ejml.simple.SimpleMatrix;
import org.slf4j.Logger;
import ram.talia.moreiotas.api.ChatEventHandler;
import ram.talia.moreiotas.common.lib.hex.MoreIotasActions;
import ram.talia.moreiotas.common.lib.hex.MoreIotasArithmetics;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;

@Mod("moreiotas")
public class MoreIotas {
   public static final String MODID = "moreiotas";
   public static final Logger LOGGER = LogUtils.getLogger();

   public static ResourceLocation id(String str) {
      return ResourceLocation.fromNamespaceAndPath("moreiotas", str);
   }

   public static boolean matrixIsEmpty(SimpleMatrix matrix) {
      return matrix.getNumCols() * matrix.getNumRows() < 1.0E-4;
   }

   public MoreIotas(IEventBus bus, ModContainer modContainer) {
      bus.addListener(this::commonSetup);
      NeoForge.EVENT_BUS.register(this);
      bus.addListener(this::addCreative);
      modContainer.registerConfig(Type.COMMON, MoreIotasConfig.SPEC);
      this.bind(HexRegistries.ACTION, MoreIotasActions::register, bus);
      NeoForge.EVENT_BUS.addListener(ChatEventHandler::chatMessageSent);
      this.bind(HexRegistries.ARITHMETIC, MoreIotasArithmetics::register, bus);
      this.bind(HexRegistries.IOTA_TYPE, MoreIotasIotaTypes::registerTypes, bus);
   }

   private void commonSetup(FMLCommonSetupEvent event) {
   }

   private void addCreative(BuildCreativeModeTabContentsEvent event) {
   }

   @SubscribeEvent
   public void onServerStarting(ServerStartingEvent event) {
   }

   private <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source, IEventBus bus) {
      bus.addListener((RegisterEvent event) -> {
         if (registry.equals(event.getRegistryKey())) {
            source.accept((value, id) -> event.register(registry, id, () -> value));
         }
      });
   }
}
