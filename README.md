![VertWhitelist](./img/VertWhitelist.png)

A global per-server whitelist controllable from anywhere.

---

### Information

---

**What is VertWhitelist?**

VertWhitelist is a whitelist plugin that creates a synced whitelist between different servers, they just have to match
serverID (Can be changed in the config).

For example:
You set all the hub servers their id to `lobbies`, now when you type /whitelist on lobbies it will whitelist them all.

**But what if you want to whitelist 1 of the lobby but want the other lobbies to keep being unwhitelisted?**

Join that specific lobby and type /whitelist on.
(/whitelist on will only enable the whitelist for the server you are on)

### Configs

---

**config.yml**

```yaml
server-id: server01
data:
  save-method: MONGODB #CAN BE: MONGODB & MYSQL
  mongo-db-uri: MONGO_URI_HERE
  mysql:
    host-name: localhost
    port: 3306
    database: hexaiplock
    username: username
    password: password

```

**language.yml**

```yaml
generic:
  wrong-usage: "&c&lVertWhitelist &8\xbb &cWrong usage, usage: &f%usage%"
  cannot-find-player: "&c&lVertWhitelist &8\xbb &cCannot find a player with the name\
    \ &f%player%&c."
  cannot-find-server: "&c&lVertWhitelist &8\xbb &cCannot find a whitelist server with\
    \ that id."
join:
  not-whitelisted: |-
    &8&m--------&r&8[ &c&lWhitelist &8&m&r&8]&m-------

    &cYou are not whitelisted to this server.

    &8&m--------&r&8[ &c&lWhitelist &8&m&r&8]&m-------
command:
  help:
    header: '&8&m------------&r&8[ &c&lCommands &8&m&r&8]&m------------'
    entry: '&c%usage% &8- &f%description%'
    footer: '&8&m------------&r&8[ &c&lCommands &8&m&r&8]&m------------'
  whitelist:
    remove:
      not-whitelisted: "&c&lVertWhitelist &8\xbb &f%player% &cis not whitelisted."
      unwhitelisted: "&c&lVertWhitelist &8\xbb &7Successfully removed &c%player% &7from\
        \ the whitelist."
    add:
      already-whitelisted: "&c&lVertWhitelist &8\xbb &f%player% &cis already whitelisted."
      whitelisted: "&c&lVertWhitelist &8\xbb &7Successfully whitelisted &c%player%&7."
    clear:
      success: "&c&lVertWhitelist &8\xbb &7Successfully cleared the whitelist."
    list:
      list: "&c&lVertWhitelist &8\xbb &7Whitelisted players: &c%players%"
    enable:
      already-enabled: "&c&lVertWhitelist &8\xbb &cThe whitelist is already enabled."
      success: "&c&lVertWhitelist &8\xbb &7Successfully &aenabled &7the whitelist."
    disable:
      already-disabled: "&c&lVertWhitelist &8\xbb &cThe whitelist is already disabled."
      success: "&c&lVertWhitelist &8\xbb &7Successfully &cdisabled &7the whitelist."
```

**redis.yml**

```yaml
singleServerConfig:
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  password: ""
  subscriptionsPerConnection: 5
  clientName: null
  address: "redis://host:6379"
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionConnectionPoolSize: 50
  connectionMinimumIdleSize: 10
  connectionPoolSize: 64
  database: 12
  dnsMonitoringInterval: 5000
threads: 0
nettyThreads: 0
```

### Support

---

Discord » https://vertcode.dev
