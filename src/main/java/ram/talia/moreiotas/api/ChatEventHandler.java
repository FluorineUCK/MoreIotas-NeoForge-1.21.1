package ram.talia.moreiotas.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.ServerChatEvent;
import org.jetbrains.annotations.Nullable;

public class ChatEventHandler {
   private static final String TAG_CHAT_PREFIX = "moreiotas:prefix";
   private static final Map<UUID, String> lastMessages = new HashMap<>();
   @Nullable
   private static String lastMessage = null;

   public static void setPrefix(Player player, @Nullable String prefix) {
      if (prefix == null) {
         player.getPersistentData().remove("moreiotas:prefix");
      } else {
         player.getPersistentData().putString("moreiotas:prefix", prefix);
      }
   }

   @Nullable
   public static String getPrefix(Player player) {
      return !player.getPersistentData().contains("moreiotas:prefix") ? null : player.getPersistentData().getString("moreiotas:prefix");
   }

   @Nullable
   public static String getLastMessage(@Nullable Player player) {
      return player == null ? lastMessage : lastMessages.get(player.getUUID());
   }

   public static void chatMessageSent(ServerChatEvent event) {
      ServerPlayer player = event.getPlayer();
      UUID uuid = player.getUUID();
      String text = event.getRawText();
      if (!event.isCanceled()) {
         String prefix = getPrefix(player);
         if (prefix == null) {
            lastMessages.put(uuid, text);
            lastMessage = text;
         } else if (text.startsWith(prefix)) {
            event.setCanceled(true);
            lastMessages.put(uuid, text.substring(prefix.length()));
         } else {
            lastMessage = text;
         }
      }
   }
}
