package de.mj.skywars.listener;

import de.mj.skywars.SkyWars;
import de.mj.skywars.utils.GameEnum;
import de.mj.skywars.utils.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class DamageListener implements Listener {

    private final SkyWars skyWars;

    public DamageListener (@NotNull SkyWars skyWars) {
        this.skyWars = skyWars;
        skyWars.setListener(this);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent damageEvent) {
        GameState gameState = skyWars.getGameState();
        if (gameState.getGameState().equals(GameEnum.LOBBY) || gameState.getGameState().equals(GameEnum.END))
            damageEvent.setCancelled(true);
        else damageEvent.setCancelled(false);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent damageByEntityEvent) {
        GameState gameState = skyWars.getGameState();
        if (gameState.getGameState().equals(GameEnum.LOBBY) || gameState.getGameState().equals(GameEnum.END)
                || gameState.getGameState().equals(GameEnum.EQUIP))
            damageByEntityEvent.setCancelled(true);
        else damageByEntityEvent.setCancelled(false);
    }

    @EventHandler
    public void onBlockDamager(EntityDamageByBlockEvent damageByBlockEvent) {
        GameState gameState = skyWars.getGameState();
        if (gameState.getGameState().equals(GameEnum.LOBBY) || gameState.getGameState().equals(GameEnum.END))
            damageByBlockEvent.setCancelled(true);
        else damageByBlockEvent.setCancelled(false);
    }

}
