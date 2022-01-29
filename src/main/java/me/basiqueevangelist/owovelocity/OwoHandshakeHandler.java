package me.basiqueevangelist.owovelocity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.ServerLoginPluginMessageEvent;
import com.velocitypowered.api.proxy.LoginPhaseConnection;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import java.util.concurrent.TimeUnit;

public class OwoHandshakeHandler {
    private static final ChannelIdentifier HANDSHAKE_ID = MinecraftChannelIdentifier.create("owo", "handshake");

    private final Cache<String, byte[]> handshakesPreLogin = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
    private final Cache<Player, byte[]> handshakes = CacheBuilder.newBuilder().weakKeys().build();

    @Subscribe(order = PostOrder.LAST)
    public void onPreLogin(PreLoginEvent event) {
        if (!event.getResult().isAllowed()) return;

        LoginPhaseConnection conn = (LoginPhaseConnection) event.getConnection();

        conn.sendLoginPluginMessage(HANDSHAKE_ID, new byte[0], responseBody -> {
            if (responseBody == null) return;

            handshakesPreLogin.put(event.getUsername(), responseBody.clone());
        });
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        byte[] hs = handshakesPreLogin.getIfPresent(event.getPlayer().getUsername());
        if (hs != null) {
            handshakes.put(event.getPlayer(), hs);
        }
    }

    @Subscribe
    public void onServerLoginPluginMessage(ServerLoginPluginMessageEvent event) {
        if (event.getIdentifier().equals(HANDSHAKE_ID)) {
            byte[] hs = handshakes.getIfPresent(event.getConnection().getPlayer());
            if (hs != null) {
                event.setResult(ServerLoginPluginMessageEvent.ResponseResult.reply(hs));
            }
        }
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        this.handshakesPreLogin.invalidate(event.getPlayer().getUsername());
        this.handshakes.invalidate(event.getPlayer());
    }
}
