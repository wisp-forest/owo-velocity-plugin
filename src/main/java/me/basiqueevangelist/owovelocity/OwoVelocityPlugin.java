package me.basiqueevangelist.owovelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(
    id = "owo-velocity",
    name = "oωo Velocity Plugin",
    version = "0.1.0",
    authors = {"BasiqueEvangelist"}
)
public class OwoVelocityPlugin {
    @Inject private ProxyServer proxyServer;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getEventManager().register(this, new OwoHandshakeHandler());
    }
}
