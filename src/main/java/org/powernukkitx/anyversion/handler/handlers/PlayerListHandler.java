package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.payload.list.PlayerListAddEntry;
import org.cloudburstmc.protocol.bedrock.data.payload.skin.AnimatedImageData;
import org.cloudburstmc.protocol.bedrock.data.payload.skin.SerializedSkin;
import org.cloudburstmc.protocol.bedrock.data.payload.skin.SkinImage;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimationData;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimationExpressionType;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimatedTextureType;
import org.cloudburstmc.protocol.bedrock.data.skin.ImageData;
import org.cloudburstmc.protocol.bedrock.data.skin.PersonaPieceData;
import org.cloudburstmc.protocol.bedrock.data.skin.PersonaPieceTintData;
import org.cloudburstmc.protocol.bedrock.data.skin.Skin;
import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;

import java.util.List;

public class PlayerListHandler extends PacketHandler<PlayerListPacket> {

    @Override
    public void handle(ProtocolPlayer player, PlayerListPacket packet) {
        packet.getEntries().stream()
                .filter(PlayerListAddEntry.class::isInstance)
                .map(PlayerListAddEntry.class::cast)
                .filter(entry -> entry.getSkin() == null && entry.getSerializedSkin() != null)
                .forEach(entry -> entry.setSkin(toLegacySkin(entry.getSerializedSkin())));
    }

    private static Skin toLegacySkin(SerializedSkin skin) {
        List<AnimationData> animations = skin.getAnimatedImageData().stream()
                .map(PlayerListHandler::toLegacyAnimation)
                .toList();
        List<PersonaPieceData> personaPieces = skin.getPersonaPieces().stream()
                .map(piece -> new PersonaPieceData(piece.getPieceId(), piece.getPieceType().getPersonaId(),
                        piece.getPackId().toString(), piece.isDefaultPiece(), piece.getProductId()))
                .toList();
        List<PersonaPieceTintData> tintColors = skin.getPieceTintColors().entrySet().stream()
                .map(entry -> new PersonaPieceTintData(entry.getKey().getPersonaId(), entry.getValue().getColors().stream()
                        .map(PlayerListHandler::toLegacyColor)
                        .toList()))
                .toList();

        return Skin.builder()
                .skinId(skin.getID())
                .playFabId(skin.getPlayFabID())
                .skinResourcePatch(skin.getResourcePatch())
                .skinData(toLegacyImage(skin.getImageData()))
                .animations(animations)
                .capeData(toLegacyImage(skin.getCapeImageData()))
                .geometryData(skin.getGeometryData())
                .geometryDataEngineVersion(skin.getGeometryDataMinEngineVersion())
                .animationData(skin.getAnimationData())
                .capeId(skin.getCapeID())
                .fullSkinId(skin.getFullID())
                .armSize(skin.getArmSize().name())
                .skinColor(toLegacyColor(skin.getSkinColor()))
                .personaPieces(personaPieces)
                .tintColors(tintColors)
                .premium(skin.isPremium())
                .persona(skin.isPersona())
                .capeOnClassic(skin.isPersonaCapeOnClassicSkin())
                .primaryUser(skin.isPrimaryUser())
                .overridingPlayerAppearance(skin.isOverridesPlayerAppearance())
                .build();
    }

    private static AnimationData toLegacyAnimation(AnimatedImageData animation) {
        return new AnimationData(toLegacyImage(animation.getSkinImage()),
                AnimatedTextureType.valueOf(animation.getAnimatedTextureType().name()), animation.getFrames(),
                AnimationExpressionType.valueOf(animation.getAnimationExpression().name()));
    }

    private static ImageData toLegacyImage(SkinImage image) {
        return image == null ? ImageData.EMPTY : ImageData.of(image.getWidth(), image.getHeight(), image.getImageBytes());
    }

    private static String toLegacyColor(int color) {
        return String.format("#%08X", color);
    }
}
