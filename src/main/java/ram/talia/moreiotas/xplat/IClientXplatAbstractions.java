package ram.talia.moreiotas.xplat;

import java.util.List;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import ram.talia.moreiotas.MoreIotas;

public interface IClientXplatAbstractions {
   IClientXplatAbstractions INSTANCE = new ForgeClientXplatImpl();

   void sendPacketToServer(CustomPacketPayload var1);

   void initPlatformSpecific();

   <T extends Entity> void registerEntityRenderer(EntityType<? extends T> var1, EntityRendererProvider<T> var2);

   <T extends ParticleOptions> void registerParticleType(ParticleType<T> var1, Function<SpriteSet, ParticleProvider<T>> var2);

   void registerItemProperty(Item var1, ResourceLocation var2, ClampedItemPropertyFunction var3);

   void setFilterSave(AbstractTexture var1, boolean var2, boolean var3);

   void restoreLastFilter(AbstractTexture var1);

   private static IClientXplatAbstractions find() {
      List<Provider<IClientXplatAbstractions>> providers = ServiceLoader.load(IClientXplatAbstractions.class).stream().toList();
      if (providers.size() != 1) {
         String names = providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
         throw new IllegalStateException("There should be exactly one IClientXplatAbstractions implementation on the classpath. Found: " + names);
      } else {
         Provider<IClientXplatAbstractions> provider = providers.get(0);
         MoreIotas.LOGGER.debug("Instantiating client xplat impl: " + provider.type().getName());
         return provider.get();
      }
   }
}
