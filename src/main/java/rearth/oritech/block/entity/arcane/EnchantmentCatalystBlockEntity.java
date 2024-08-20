package rearth.oritech.block.entity.arcane;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;
import rearth.oritech.client.init.ModScreens;
import rearth.oritech.client.init.ParticleContent;
import rearth.oritech.client.ui.CatalystScreenHandler;
import rearth.oritech.init.BlockEntitiesContent;
import rearth.oritech.network.NetworkContent;
import rearth.oritech.util.InventoryInputMode;
import rearth.oritech.util.InventoryProvider;
import rearth.oritech.util.ScreenProvider;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.HashSet;
import java.util.List;

public class EnchantmentCatalystBlockEntity extends BlockEntity
  implements InventoryProvider, ScreenProvider, BlockEntityTicker<EnchantmentCatalystBlockEntity>, GameEventListener.Holder<EnchantmentCatalystBlockEntity.DeathListener>, ExtendedScreenHandlerFactory<ModScreens.BasicData> {
    
    private final DeathListener listener;
    public final int baseSoulCapacity = 50;
    public final int maxProgress = 20;
    
    // working data
    public int collectedSouls;
    public int maxSouls = 50;
    private int progress;
    private boolean isHyperEnchanting;
    private boolean dirty;
    
    public final SimpleInventory inventory = new SimpleInventory(2) {
        @Override
        public void markDirty() {
            EnchantmentCatalystBlockEntity.this.markDirty();
        }
    };
    
    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(50000, 50000, 0) {
        @Override
        protected void onFinalCommit() {
            EnchantmentCatalystBlockEntity.this.markDirty();
        }
    };
    
    protected final InventoryStorage inventoryStorage = InventoryStorage.of(inventory, null);
    
    public EnchantmentCatalystBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesContent.ENCHANTMENT_CATALYST_BLOCK_ENTITY, pos, state);
        listener = new DeathListener(pos);
    }
    
    @Override
    public void tick(World world, BlockPos pos, BlockState state, EnchantmentCatalystBlockEntity blockEntity) {
        
        if (world.isClient) return;
        
        // check if powered, and adjust soul capacity
        if (energyStorage.amount > 0) {
            var gainedSoulCapacity = energyStorage.amount / 20;
            energyStorage.amount = 0;
            var newMax = baseSoulCapacity + gainedSoulCapacity;
            adjustMaxSouls(newMax);
        } else if (maxSouls > baseSoulCapacity) {
            adjustMaxSouls(baseSoulCapacity);
        }
        
        // explode if unstable
        if (collectedSouls > maxSouls) {
            doExplosion();
            return;
        }
        
        // check if output is empty
        // check if a book is in slot 0
        // check if an item is in slot 1
        if (canProceed()) {
            dirty = true;
            progress++;
            
            if (progress >= maxProgress) {
                enchantInput();
                
                progress = 0;
                isHyperEnchanting = false;
            }
        } else {
            progress = 0;
        }
        
        if (dirty) {
            dirty = false;
            updateNetwork();
            DeathListener.resetEvents();
        }
        
    }
    
    private void doExplosion() {
        
        var center = pos.toCenterPos();
        var strength = Math.sqrt(collectedSouls - baseSoulCapacity);
        
        world.createExplosion(null, center.x, center.y, center.z, (int) strength, true, World.ExplosionSourceType.BLOCK);
        world.removeBlock(pos, false);
    }
    
    private void adjustMaxSouls(long target) {
        if (maxSouls > target) {
            maxSouls--;
        } else if (maxSouls < target) {
            maxSouls++;
        }
        
        this.dirty = true;
    }
    
    private void enchantInput() {
        
        var bookCandidate = inventory.getStack(0);
        if (!bookCandidate.getItem().equals(Items.ENCHANTED_BOOK) || !bookCandidate.contains(DataComponentTypes.STORED_ENCHANTMENTS))
            return;
        
        var enchantment = bookCandidate.get(DataComponentTypes.STORED_ENCHANTMENTS).getEnchantments().stream().findFirst().get();
        
        var inputStack = inventory.getStack(1);
        var toolLevel = inputStack.getEnchantments().getLevel(enchantment);
        inputStack.addEnchantment(enchantment, toolLevel + 1);
        
        collectedSouls -= getEnchantmentCost(enchantment.value(), toolLevel + 1, isHyperEnchanting);
        
        if (isHyperEnchanting)
            inventory.setStack(0, ItemStack.EMPTY);
        
    }
    
    private boolean hasEnoughSouls(Enchantment enchantment, int targetLevel) {
        var resultingCost = getEnchantmentCost(enchantment, targetLevel, isHyperEnchanting);
        return collectedSouls >= resultingCost;
    }
    
    private int getEnchantmentCost(Enchantment enchantment, int targetLevel, boolean hyper) {
        var baseCost = enchantment.getAnvilCost();
        var resultingCost = baseCost * targetLevel;
        if (hyper) resultingCost = resultingCost * 2 + 50;
        return resultingCost;
    }
    
    // for UI
    public int getDisplayedCost() {
        if (inventory.getStack(0).isEmpty() || inventory.getStack(1).isEmpty()) return 0;
        var bookCandidate = inventory.getStack(0);
        
        if (bookCandidate.getItem().equals(Items.ENCHANTED_BOOK) && bookCandidate.contains(DataComponentTypes.STORED_ENCHANTMENTS)) {
            
            var enchantment = bookCandidate.get(DataComponentTypes.STORED_ENCHANTMENTS).getEnchantments().stream().findFirst().get();
            var maxLevel = enchantment.value().getMaxLevel();
            
            var inputStack = inventory.getStack(1);
            var toolLevel = inputStack.getEnchantments().getLevel(enchantment);
            var isHyper = toolLevel >= maxLevel;
            
            return getEnchantmentCost(enchantment.value(), toolLevel + 1, isHyper);
        }
        
        return 0;
    }
    
    private boolean canProceed() {
        
        if (inventory.getStack(0).isEmpty() || inventory.getStack(1).isEmpty()) return false;
        
        var bookCandidate = inventory.getStack(0);
        if (bookCandidate.getItem().equals(Items.ENCHANTED_BOOK) && bookCandidate.contains(DataComponentTypes.STORED_ENCHANTMENTS)) {
            
            var enchantment = bookCandidate.get(DataComponentTypes.STORED_ENCHANTMENTS).getEnchantments().stream().findFirst().get();
            var maxLevel = enchantment.value().getMaxLevel();
            var level = bookCandidate.get(DataComponentTypes.STORED_ENCHANTMENTS).getLevel(enchantment);
            
            var inputStack = inventory.getStack(1);
            var toolLevel = inputStack.getEnchantments().getLevel(enchantment);
            this.isHyperEnchanting = toolLevel >= maxLevel;
            
            return level == maxLevel && hasEnoughSouls(enchantment.value(), toolLevel + 1);
        }
        
        return false;
    }
    
    @Override
    public DeathListener getEventListener() {
        return listener;
    }
    
    private void onSoulIncoming(Vec3d source) {
        var distance = (float) source.distanceTo(pos.toCenterPos());
        collectedSouls++;
        dirty = true;
        
        var soulPath = pos.toCenterPos().subtract(source);
        var animData = new ParticleContent.SoulParticleData(soulPath, (int) getSoulTravelDuration(distance));
        
        ParticleContent.WANDERING_SOUL.spawn(world, source.add(0, 0.7f, 0), animData);
    }
    
    private boolean canAcceptSoul() {
        return collectedSouls < maxSouls;
    }
    
    private static float getSoulTravelDuration(float distance) {
        return (float) (Math.sqrt(distance * 20) * 3);
    }
    
    private void updateNetwork() {
        NetworkContent.MACHINE_CHANNEL.serverHandle(this).send(new NetworkContent.CatalystSyncPacket(pos, collectedSouls, progress, isHyperEnchanting, maxSouls));
    }
    
    public void handleNetworkPacket(NetworkContent.CatalystSyncPacket packet) {
        this.isHyperEnchanting = packet.isHyperEnchanting();
        this.progress = packet.progress();
        this.collectedSouls = packet.storedSouls();
        this.maxSouls = packet.maxSouls();
    }
    
    @Override
    public ModScreens.BasicData getScreenOpeningData(ServerPlayerEntity player) {
        return new ModScreens.BasicData(pos);
    }
    
    @Override
    public Text getDisplayName() {
        return Text.literal("");
    }
    
    @Override
    public boolean showProgress() {
        return false;
    }
    
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        updateNetwork();
        return new CatalystScreenHandler(syncId, playerInventory, this);
    }
    
    @Override
    public InventoryStorage getInventory(Direction direction) {
        return inventoryStorage;
    }
    
    @Override
    public List<GuiSlot> getGuiSlots() {
        return List.of(
          new GuiSlot(0, 56, 26),
          new GuiSlot(1, 56, 44));
    }
    
    @Override
    public float getDisplayedEnergyUsage() {
        return 0;
    }
    
    @Override
    public float getProgress() {
        return progress / (float) maxProgress;
    }
    
    @Override
    public InventoryInputMode getInventoryInputMode() {
        return InventoryInputMode.FILL_LEFT_TO_RIGHT;
    }
    
    @Override
    public Inventory getDisplayedInventory() {
        return inventory;
    }
    
    @Override
    public ScreenHandlerType<?> getScreenHandlerType() {
        return ModScreens.CATALYST_SCREEN;
    }
    
    @Override
    public boolean inputOptionsEnabled() {
        return false;
    }
    
    // this is used as soul display instead
    @Override
    public boolean showEnergy() {
        return true;
    }
    
    public class DeathListener implements GameEventListener {
        
        private final PositionSource position;
        
        private static final HashSet<Vec3d> consumedEvents = new HashSet<>();
        
        public static void resetEvents() {
            consumedEvents.clear();
        }
        
        public DeathListener(BlockPos pos) {
            this.position = new BlockPositionSource(pos);
        }
        
        @Override
        public PositionSource getPositionSource() {
            return position;
        }
        
        @Override
        public int getRange() {
            return 23;
        }
        
        @Override
        public TriggerOrder getTriggerOrder() {
            return TriggerOrder.BY_DISTANCE;
        }
        
        @Override
        public boolean listen(ServerWorld world, RegistryEntry<GameEvent> event, GameEvent.Emitter emitter, Vec3d emitterPos) {
            if (event.matchesKey(GameEvent.ENTITY_DIE.registryKey()) && canAcceptSoul() && !consumedEvents.contains(emitterPos)) {
                System.out.println("Death event! " + emitterPos + " " + emitter.sourceEntity().getName());
                EnchantmentCatalystBlockEntity.this.onSoulIncoming(emitterPos);
                consumedEvents.add(emitterPos);
                return true;
            }
            
            return false;
        }
    }
    
}
