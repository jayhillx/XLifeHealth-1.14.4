package me.daqem.xlifehealth.events;

import me.daqem.xlifehealth.XLifeHealth;
import me.daqem.xlifehealth.utils.SecurityCheck;
import me.daqem.xlifehealth.utils.SendMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.daqem.xlifehealth.utils.XLifeModifiers.applyMaxHealthModifier;
import static me.daqem.xlifehealth.utils.XLifeModifiers.removeMaxHealthModifiers;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = XLifeHealth.MOD_ID)
public class HealthEvents {
    public static final ResourceLocation BLUE_HEARTS = new ResourceLocation("xlifehealth:textures/gui/blue_hearts.png");
    public static final ResourceLocation GREEN_HEARTS = new ResourceLocation("xlifehealth:textures/gui/green_hearts.png");
    public static final ResourceLocation ORANGE_HEARTS = new ResourceLocation("xlifehealth:textures/gui/orange_hearts.png");
    public static final ResourceLocation PINK_HEARTS = new ResourceLocation("xlifehealth:textures/gui/pink_hearts.png");
    public static final ResourceLocation PURPLE_HEARTS = new ResourceLocation("xlifehealth:textures/gui/purple_hearts.png");
    public static final ResourceLocation YELLOW_HEARTS = new ResourceLocation("xlifehealth:textures/gui/yellow_hearts.png");
    public static final ResourceLocation CYAN_HEARTS = new ResourceLocation("xlifehealth:textures/gui/cyan_hearts.png");
    public static final ResourceLocation LIME_HEARTS = new ResourceLocation("xlifehealth:textures/gui/lime_hearts.png");
    public static final ResourceLocation BLACK_HEARTS = new ResourceLocation("xlifehealth:textures/gui/black_hearts.png");

    private float maxHealth = 0;

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }
    @SubscribeEvent
    public void onPlayerDie(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            setMaxHealth(player.getMaxHealth());
        }
    }
    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();
        SecurityCheck.securityCheck(player);
        setHealthAfterDeath(player);
        setMaxHealth(player.getMaxHealth());
    }

    @SubscribeEvent
    public void onEnterNether(PlayerEvent.PlayerChangedDimensionEvent event) {
        PlayerEntity player = event.getPlayer();
        SecurityCheck.securityCheck(player);
        applyMaxHealthModifier(player, 0f);
    }

    @SubscribeEvent
    public void onFirstJoin(PlayerEvent.PlayerLoggedInEvent event){
        PlayerEntity player = event.getPlayer();
        CompoundNBT entityData = player.getPersistentData();
        if(!entityData.getBoolean("xlifehealth.firstJoin") && player.getMaxHealth() == 20f) {
            entityData.putBoolean("xlifehealth.firstJoin", true);
            removeMaxHealthModifiers(player);
            applyMaxHealthModifier(player, -18f);
            SendMessage.sendMessage(player, TextFormatting.YELLOW + "=====================================");
            SendMessage.sendMessage(player, " ");
            SendMessage.sendMessage(player, TextFormatting.GOLD + "Welcome to X Life!");
            SendMessage.sendMessage(player, " ");
            SendMessage.sendMessage(player, TextFormatting.WHITE + "You'll start off with only one heart.");
            SendMessage.sendMessage(player, TextFormatting.WHITE + "Every time you die, a heart will be added.");
            SendMessage.sendMessage(player, TextFormatting.WHITE + "When you die with 10 hearts, you will be banned!");
            SendMessage.sendMessage(player, " ");
            SendMessage.sendMessage(player, TextFormatting.GOLD + "Good Luck!");
            SendMessage.sendMessage(player, " ");
            SendMessage.sendMessage(player, TextFormatting.YELLOW + "=====================================");
        }
    }

    public void setHealthAfterDeath(PlayerEntity player) {
        if (getMaxHealth() >= 2 && getMaxHealth() <= 18) {
            float maxHealth = getMaxHealth();
            int lives = (int) (10 - (maxHealth / 2));
            int modifierAmount = (lives * 2 - 2) - ((lives * 2 - 2) + (lives * 2 - 2));
            removeMaxHealthModifiers(player);
            applyMaxHealthModifier(player, modifierAmount);
            if (lives < 10) {
                if (lives >= 2) {
                    SendMessage.sendMessage(player, TextFormatting.RED + "Be careful, you only have " + lives + " lives left.");
                    removeMaxHealthModifiers(player);
                    applyMaxHealthModifier(player, modifierAmount);
                }
                if (lives == 1) {
                    SendMessage.sendMessage(player, TextFormatting.RED + "Be careful, this is your last life!!!");
                }
            }
        } else if (getMaxHealth() == 20) {
            player.setGameType(GameType.SPECTATOR);
            SendMessage.sendMessage(player,TextFormatting.RED + "You have died with 10 hearts remaining.");
            SendMessage.sendMessage(player,TextFormatting.RED + "You have been put in spectator mode.");
            removeMaxHealthModifiers(player);
            applyMaxHealthModifier(player, 20);
        } else {
            removeMaxHealthModifiers(player);
            applyMaxHealthModifier(player, -18);
            XLifeHealth.LOGGER.info("[XLIFEHEALTHMOD] Error ID: 100");
            XLifeHealth.LOGGER.info("[XLIFEHEALTHMOD] Please report this error to DaqEm.");
            XLifeHealth.LOGGER.info("[XLIFEHEALTHMOD] Email: daqemyt@gmail.com");
        }
    }

    @SubscribeEvent
    public void setHeartColors(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
            Minecraft mc = Minecraft.getInstance();
            if (getMaxHealth() == 4) {
                mc.getTextureManager().bindTexture(BLUE_HEARTS);
            }
            else if (getMaxHealth() == 6) {
                mc.getTextureManager().bindTexture(GREEN_HEARTS);
            }
            else if (getMaxHealth() == 8) {
                mc.getTextureManager().bindTexture(ORANGE_HEARTS);
            }
            else if (getMaxHealth() == 10) {
                mc.getTextureManager().bindTexture(PINK_HEARTS);
            }
            else if (getMaxHealth() == 12) {
                mc.getTextureManager().bindTexture(PURPLE_HEARTS);
            }
            else if (getMaxHealth() == 14) {
                mc.getTextureManager().bindTexture(YELLOW_HEARTS);
            }
            else if (getMaxHealth() == 16) {
                mc.getTextureManager().bindTexture(CYAN_HEARTS);
            }
            else if (getMaxHealth() == 18) {
                mc.getTextureManager().bindTexture(LIME_HEARTS);
            }
            else if (getMaxHealth() == 20) {
                mc.getTextureManager().bindTexture(BLACK_HEARTS);
            }
        }
    }
}
