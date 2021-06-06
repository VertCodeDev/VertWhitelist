package dev.vertcode.vertwhitelist.redis.event;

import dev.vertcode.vertwhitelist.VertWhitelistPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.redisson.api.listener.MessageListener;

@RequiredArgsConstructor
@Getter
public class PubSubEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final String channel, message;

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static class Handler implements MessageListener<String> {
        @Override
        public void onMessage(CharSequence charSequence, String s) {
            VertWhitelistPlugin.getInstance().getServer().getPluginManager().callEvent(new PubSubEvent(String.valueOf(charSequence), s));
        }
    }
}
