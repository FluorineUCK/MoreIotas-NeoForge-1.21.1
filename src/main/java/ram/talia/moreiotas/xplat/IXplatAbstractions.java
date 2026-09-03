package ram.talia.moreiotas.xplat;

import at.petrak.hexcasting.api.HexAPI;
import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.ServiceLoader.Provider;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface IXplatAbstractions {
   GameProfile MOREIOTAS = new GameProfile(UUID.fromString("8BE7E9DA-1667-11EE-BE56-0242AC120002"), "[MoreIotas]");
   IXplatAbstractions INSTANCE = new ForgeXplatImpl();

   boolean isPhysicalClient();

   void sendPacketToPlayer(ServerPlayer var1, CustomPacketPayload var2);

   void sendPacketNear(Vec3 var1, double var2, ServerLevel var4, CustomPacketPayload var5);

   Packet<?> toVanillaClientboundPacket(CustomPacketPayload var1);

   boolean isBreakingAllowed(Level var1, BlockPos var2, BlockState var3, Player var4);

   @Nullable
   String lastMessage(@Nullable Player var1);

   void setChatPrefix(Player var1, @Nullable String var2);

   @Nullable
   String getChatPrefix(Player var1);

   private static IXplatAbstractions find() {
      List<Provider<IXplatAbstractions>> providers = ServiceLoader.load(IXplatAbstractions.class).stream().toList();
      if (providers.size() != 1) {
         String names = providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
         throw new IllegalStateException("There should be exactly one IXplatAbstractions implementation on the classpath. Found: " + names);
      } else {
         Provider<IXplatAbstractions> provider = providers.get(0);
         HexAPI.LOGGER.debug("Instantiating xplat impl: " + provider.type().getName());
         return provider.get();
      }
   }
}
