package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.codec.v1001.Bedrock_v1001;
import org.cloudburstmc.protocol.bedrock.codec.v2168.Bedrock_v2168;
import org.cloudburstmc.protocol.bedrock.codec.v2168.Bedrock_v2168_hotfix4;
import org.junit.jupiter.api.Test;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.junit.jupiter.api.Assertions.assertSame;

class LoginCodecSelectionTest {
    @Test
    void keepsTheClientCodecForOlderProtocols() {
        assertSame(Bedrock_v1001.CODEC, ProtocolVersion.codecForGameVersion(ProtocolVersion.get(1001), "1.26.30"));
    }

    @Test
    void usesGameVersionOnlyToPickBetweenSharedProtocolCodecs() {
        ProtocolVersion version = ProtocolVersion.get(2168);
        assertSame(Bedrock_v2168_hotfix4.CODEC, ProtocolVersion.codecForGameVersion(version, "1.26.44"));
        assertSame(Bedrock_v2168.CODEC, ProtocolVersion.codecForGameVersion(version, "1.26.40"));
    }
}
