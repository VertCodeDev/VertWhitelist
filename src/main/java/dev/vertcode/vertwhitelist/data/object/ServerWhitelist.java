package dev.vertcode.vertwhitelist.data.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Getter
public class ServerWhitelist {

    @Id
    private final String serverId;
    private final List<UUID> whitelistedPlayers = new ArrayList<>();

}
