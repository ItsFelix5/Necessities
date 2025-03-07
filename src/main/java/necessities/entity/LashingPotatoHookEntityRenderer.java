package necessities.entity;

import necessities.item.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.GuardianEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class LashingPotatoHookEntityRenderer extends EntityRenderer<LashingPotatoHookEntity, LashingPotatoHookEntityRenderer.RenderState> {
	public LashingPotatoHookEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public RenderState createRenderState() {
		return new RenderState();
	}

	@Override
	protected boolean canBeCulled(LashingPotatoHookEntity entity) {
		return false;
	}

	@Override
	public void updateRenderState(LashingPotatoHookEntity entity, RenderState state, float tickDelta) {
		super.updateRenderState(entity, state, tickDelta);

		PlayerEntity holder = entity.getOwner();
		int i = (holder.getMainArm() == Arm.RIGHT? 1 : -1) * (holder.getMainHandStack().isOf(ModItems.LASHING_POTATO) ? 1 : -1);
		if (this.dispatcher.gameOptions.getPerspective().isFirstPerson() && holder == MinecraftClient.getInstance().player) {
			double m = 960.0 / (double) this.dispatcher.gameOptions.getFov().getValue();
			float f = MathHelper.sin(MathHelper.sqrt(entity.getOwner().getHandSwingProgress(tickDelta)) * (float) Math.PI);
			Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float)i * 0.525F, -0.1F).multiply(m).rotateY(f * 0.5F).rotateX(-f * 0.7F);
			state.handPos = holder.getCameraPosVec(tickDelta).add(vec3d);
		} else {
			float g = MathHelper.lerp(tickDelta, holder.prevBodyYaw, holder.bodyYaw) * 0.017453292F;
			double d = MathHelper.sin(g);
			double e = MathHelper.cos(g);
			float h = holder.getScale();
			double j = (double)i * 0.35 * (double)h;
			double k = 0.8 * (double)h;
			float l = holder.isInSneakingPose() ? -0.1875F : 0.0F;
			state.handPos = holder.getCameraPosVec(tickDelta).add(-e * j - d * k, (double)l - 0.45 * (double)h, -d * j + e * k);
		}
	}

	@Override
	public void render(RenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		matrixStack.push();
		float j = state.age * 0.15F % 1.0F;
		Vec3d vec3d3 = state.handPos.subtract(state.x, state.y + state.standingEyeHeight, state.z);
		float k = (float) (vec3d3.length() + 0.1);
		vec3d3 = vec3d3.normalize();
		float l = (float) Math.acos(vec3d3.y);
		float m = (float) Math.atan2(vec3d3.z, vec3d3.x);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) (Math.PI / 2) - m) * (180.0F / (float) Math.PI)));
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(l * (180.0F / (float) Math.PI)));
		float n = state.age * 0.05F * -1.5F;
		float p = MathHelper.cos(n + (float) Math.PI) * 0.2F;
		float q = MathHelper.sin(n + (float) Math.PI) * 0.2F;
		float r = MathHelper.cos(n + 0.0F) * 0.2F;
		float s = MathHelper.sin(n + 0.0F) * 0.2F;
		float t = MathHelper.cos(n + (float) (Math.PI / 2)) * 0.2F;
		float u = MathHelper.sin(n + (float) (Math.PI / 2)) * 0.2F;
		float v = MathHelper.cos(n + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
		float w = MathHelper.sin(n + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
		float aa = -1.0F + j;
		float ab = k * 2.5F + aa;
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(GuardianEntityRenderer.LAYER);
		MatrixStack.Entry entry = matrixStack.peek();
		vertex(vertexConsumer, entry, p, k, q, 0.4999F, ab);
		vertex(vertexConsumer, entry, p, 0.0F, q, 0.4999F, aa);
		vertex(vertexConsumer, entry, r, 0.0F, s, 0.0F, aa);
		vertex(vertexConsumer, entry, r, k, s, 0.0F, ab);
		vertex(vertexConsumer, entry, t, k, u, 0.4999F, ab);
		vertex(vertexConsumer, entry, t, 0.0F, u, 0.4999F, aa);
		vertex(vertexConsumer, entry, v, 0.0F, w, 0.0F, aa);
		vertex(vertexConsumer, entry, v, k, w, 0.0F, ab);
		matrixStack.pop();
		super.render(state, matrixStack, vertexConsumerProvider, light);
	}

	private static void vertex(VertexConsumer vertexConsumer, MatrixStack.Entry entry, float f, float g, float h, float i, float j) {
		vertexConsumer.vertex(entry, f, g, h)
			.color(128, 255, 128, 255)
			.texture(i, j)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
			.normal(0.0F, 1.0F, 0.0F);
	}

	public static class RenderState extends EntityRenderState {
		public Vec3d handPos = Vec3d.ZERO;
	}
}
