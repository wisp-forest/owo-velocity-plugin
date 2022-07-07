package me.basiqueevangelist.owovelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

@Plugin(
    id = "owo-velocity",
    name = "oωo Velocity Plugin",
    version = "0.1.1",
    authors = {"BasiqueEvangelist"}
)
public class OwoVelocityPlugin {
    public static final ChannelIdentifier HANDSHAKE_ID = MinecraftChannelIdentifier.create("owo", "handshake");

    @Inject private ProxyServer proxyServer;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getChannelRegistrar().register(HANDSHAKE_ID);

        proxyServer.getEventManager().register(this, new OwoHandshakeHandler());
    }
}
