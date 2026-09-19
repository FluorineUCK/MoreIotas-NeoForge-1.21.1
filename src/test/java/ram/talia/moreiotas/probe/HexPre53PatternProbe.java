package ram.talia.moreiotas.probe;

import at.petrak.hexcasting.api.casting.math.HexAngle;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.math.HexSignature;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.RegistryAccess;

/** Runs against the unmodified official provider, not a compatibility stub. */
public final class HexPre53PatternProbe {
    public static void main(String[] args) {
        if (args.length != 0 && args[0].equals("repeated-builder")) {
            HexSignature.Builder builder = new HexSignature.Builder(false);
            builder.addAngle(HexAngle.RIGHT);
            HexSignature first = builder.build();
            HexSignature second = builder.build();
            require(first.equals(second), "Repeated build changed signature identity: first="
                + first + " second=" + second + " builderSize=" + builder.size());
            require(builder.size() == 1, "build changed builder size");
            System.out.println("[HEX-PRE53-PROBE] repeated_builder=PASS");
            return;
        }
        for (int length : new int[] {0, 1, 9, 10, 11, 19, 20, 21, 100}) {
            String angles = "w".repeat(length);
            for (HexDir orientation : HexDir.values()) {
                HexPattern original = HexPattern.fromAngleString(angles, orientation);
                require(original.getSignature().toAnglesString().equals(angles), "signature truncated at " + length);
                require(original.positions().size() == length + 2, "positions length mismatch");
                JsonObject encoded = HexPattern.CODEC.encodeStart(JsonOps.INSTANCE, original).getOrThrow().getAsJsonObject();
                require(encoded.has("orientation") && encoded.has("signature"), "not the pre53 format");
                require(!encoded.has("start_dir") && !encoded.has("angles"), "legacy format written");
                require(original.equals(HexPattern.CODEC.parse(JsonOps.INSTANCE, encoded).getOrThrow()), "current codec mismatch");
                JsonObject legacy = new JsonObject();
                legacy.addProperty("start_dir", orientation.name());
                legacy.addProperty("angles", angles);
                require(original.equals(HexPattern.CODEC.parse(JsonOps.INSTANCE, legacy).getOrThrow()), "legacy codec mismatch");
                RegistryFriendlyByteBuf bytes = new RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY);
                try {
                    HexPattern.STREAM_CODEC.encode(bytes, original);
                    require(original.equals(HexPattern.STREAM_CODEC.decode(bytes)), "stream mismatch");
                    require(bytes.readableBytes() == 0, "stream trailing bytes");
                } finally { bytes.release(); }
            }
        }
        HexPattern east = HexPattern.fromAngleString("aqwe", HexDir.EAST);
        HexPattern west = HexPattern.fromAngleString("aqwe", HexDir.WEST);
        require(east.getSignature().equals(west.getSignature()), "rotated matching must compare signatures");
        require(!east.equals(west), "full pattern equality must preserve orientation");
        boolean rejected = false;
        try { HexPattern.fromAngleString("s", HexDir.EAST); } catch (IllegalStateException expected) { rejected = true; }
        require(rejected, "checked factory accepted overlapping edge");
        require(HexPattern.fromAngleString("s", HexDir.EAST, false).getSignature().toAnglesString().equals("s"), "unchecked factory rejected old data");
        HexSignature.Builder reusable = new HexSignature.Builder(false);
        reusable.addAngle(HexAngle.RIGHT);
        HexSignature beforeReset = reusable.build();
        reusable.reset(false);
        reusable.addAngle(HexAngle.LEFT);
        require(beforeReset.toAnglesString().equals("e"), "reset mutated prior immutable signature");
        require(reusable.build().toAnglesString().equals("q"), "reset did not clear builder");
        System.out.println("[HEX-PRE53-PROBE] aggregate=PASS codec_cases=54 legacy_cases=54 stream_cases=54 orientation=true overlap=true reset=true");
    }
    private static void require(boolean test, String message) { if (!test) throw new AssertionError(message); }
}
