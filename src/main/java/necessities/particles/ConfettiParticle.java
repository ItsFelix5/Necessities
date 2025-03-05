package necessities.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class ConfettiParticle extends SpriteBillboardParticle {
    private float rotationX, rotationY, rotationZ;
    private final float rotationXSpeed, rotationYSpeed, rotationZSpeed;
    private float offset = 0f;

    protected ConfettiParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider provider) {
        super(clientWorld, x, y, z, clientWorld.random.nextGaussian() / 8f, Math.abs(clientWorld.random.nextGaussian() / 8f), clientWorld.random.nextGaussian() / 8f);
        setSprite(provider);

        this.rotationX = world.getRandom().nextFloat() * 360f;
        this.rotationY = world.getRandom().nextFloat() * 360f;
        this.rotationZ = world.getRandom().nextFloat() * 360f;
        this.rotationXSpeed = world.getRandom().nextFloat() * 10f * (random.nextBoolean() ? -1 : 1);
        this.rotationYSpeed = world.getRandom().nextFloat() * 10f * (random.nextBoolean() ? -1 : 1);
        this.rotationZSpeed = world.getRandom().nextFloat() * 10f * (random.nextBoolean() ? -1 : 1);

        this.maxAge = (int) (550 + 100 * world.random.nextFloat());
        this.red = world.random.nextFloat();
        this.blue = world.random.nextFloat();
        this.green = world.random.nextFloat();
        this.scale *= 0.2f + world.random.nextFloat() * 0.3f;
        this.gravityStrength = 0.1f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d cameraPos = camera.getPos();
        Vector3f[] vec3fs = new Vector3f[]{new Vector3f(-1, -1, 0), new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0)};
        float size = this.getSize(tickDelta);
        if (!this.onGround) {
            rotationX += rotationXSpeed;
            rotationY += rotationYSpeed;
            rotationZ += rotationZSpeed;
        } else if(offset == 0f) {
            rotationX = 90f;
            rotationY = 0;
            offset = world.getRandom().nextFloat() / 100f + 0.001f;
        }
        for (Vector3f vec3f : vec3fs) {
            Vector3f dest = new Vector3f(rotationX * 0.00872664625997f, rotationY * 0.00872664625997f, rotationZ * 0.00872664625997f);

            float sinX = MathHelper.sin(dest.x);
            float cosX = MathHelper.cos(dest.x);
            float sinY = MathHelper.sin(dest.y);
            float cosY = MathHelper.cos(dest.y);
            float sinZ = MathHelper.sin(dest.z);
            float cosZ = MathHelper.cos(dest.z);
            float x = sinX * cosY * cosZ + cosX * sinY * sinZ;
            float y = cosX * sinY * cosZ - sinX * cosY * sinZ;
            float z = sinX * sinY * cosZ + cosX * cosY * sinZ;
            float w = cosX * cosY * cosZ - sinX * sinY * sinZ;

            float xx = x * x, yy = y * y, zz = z * z, ww = w * w;
            float xy = x * y, xz = x * z, yz = y * z, xw = x * w;
            float zw = z * w, yw = y * w, k = 1 / (xx + yy + zz + ww);
            vec3f = vec3f.set(org.joml.Math.fma((xx - yy - zz + ww) * k, vec3f.x, org.joml.Math.fma(2 * (xy - zw) * k, vec3f.y, (2 * (xz + yw) * k) * vec3f.z)),
                    org.joml.Math.fma(2 * (xy + zw) * k, vec3f.x, org.joml.Math.fma((yy - xx - zz + ww) * k, vec3f.y, (2 * (yz - xw) * k) * vec3f.z)),
                    org.joml.Math.fma(2 * (xz - yw) * k, vec3f.x, org.joml.Math.fma(2 * (yz + xw) * k, vec3f.y, ((zz - xx - yy + ww) * k) * vec3f.z)));
            vec3f.normalize(size);
            vec3f.add((float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.getX()),
                    (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.getY() + offset),
                    (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.getZ()));
        }

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int light = this.getBrightness(tickDelta);

        vertexConsumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).texture(maxU, maxV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).texture(maxU, minV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).texture(minU, minV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).texture(minU, maxV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).texture(maxU, maxV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).texture(maxU, minV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).texture(minU, minV).color(red, green, blue, alpha).light(light);
        vertexConsumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).texture(minU, maxV).color(red, green, blue, alpha).light(light);
    }
}