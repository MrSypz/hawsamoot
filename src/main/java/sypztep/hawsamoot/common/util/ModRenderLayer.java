package sypztep.hawsamoot.common.util;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public abstract class ModRenderLayer extends RenderPhase {
    public static RenderLayer getGuiTextured(Identifier texture) {
        return GUI_TEXTURED.apply(texture);
    }
    private static final Function<Identifier, RenderLayer> GUI_TEXTURED = Util.memoize(
            texture -> RenderLayer.of(
                    "gui_textured",
                    VertexFormats.POSITION_TEXTURE_COLOR,
                    VertexFormat.DrawMode.QUADS,
                    786432,
                    RenderLayer.MultiPhaseParameters.builder()
                            .texture(new Texture(texture, false, true)) // Enable mipmap, disable blur
                            .program(POSITION_TEXTURE_PROGRAM)
                            .transparency(TRANSLUCENT_TRANSPARENCY)
                            .depthTest(LEQUAL_DEPTH_TEST)
                            .build(false)
            )
    );
    public ModRenderLayer(String name, Runnable beginAction, Runnable endAction) {
        super(name, beginAction, endAction);
    }
}
