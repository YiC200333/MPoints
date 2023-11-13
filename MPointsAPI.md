##About MPointsAPI

For MPoints 2.0.2

Chinese
```java
MPointsAPI.getversion();
```
获取MPoints版本号，返回String

```java
MPointsAPI.getSyncChannalType();
```
获取数据同步模式，返回SyncChannalType  
SyncChannalType.OFF 未开启数据同步  
SyncChannalType.BUNGEECORD 开启了数据同步，通道为Bungeecord  
SyncChannalType.REDIS 开启了数据同步，通道为Redis

```java
MPointsAPI.isExistsign(String sign);
```
检测点数标识是否存在，返回Boolean

```java
MPointsAPI.getPointsList();
```
获取点数标识列表，返回Set&lt;String&gt;

```java
MPointsAPI mpapi = new MPointsAPI(sign);
```

```java
mpapi.formatdouble(String amount);
```
格式化amount，返回BigDecimal

```java
mpapi.getdisplay(BigDecimal balance);
```
将金额转为显示消息，返回String  
比如 5,000 元

```java
mpapi.createPlayerData(uid, name);
```
创建玩家数据，返回boolean

```java
mpapi.getPlayerData(UUID uid);
mpapi.getPlayerData(String name);
```
获取玩家数据，返回PlayerData  
如果返回null，表示该玩家不存在

```java
//获取玩家UUID
PlayerData.getUniqueId();
//获取玩家名称
PlayerData.getName();
//获取玩家金额，返回BigDecimal
PlayerData.getBalance();
//获取数据的标签，返回String
PlayerData.getPsign();
```
关于PlayerData

```java
mpapi.ismaxnumber(BigDecimal amount);
```
检查金额是否为最大值，返回boolean

```java
mpapi.changePlayerBalance(UUID u, String playername, BigDecimal amount, Boolean isadd);
mpapi.changePlayerBalance(UUID u, String playername, BigDecimal amount, Boolean isadd, String pluginname);
```
对金额进行修改，返回Integer  
isadd = true 为增加金额  
isadd = false 为扣除金额  
isadd = null 为设置金额  
返回0 表示成功  
返回1 失败，表示BC模式且没有玩家存在  
返回2 失败,表示玩家余额不足  
返回3 失败,表示玩家余额超出最大值

```java
mpapi.getbalancetop();
```
获取TOP10名单，返回List<String>

```java
mpapi.getsumbalance();
```
获取服务器金额总数，返回BigDecimal



****


English
```java
MPointsAPI.getversion();
```
Gets the mpoints version number, return String

```java
MPointsAPI.getSyncChannalType();
```
Get the data synchronization mode, return SyncChanalType  
SyncChannalType.OFF Data synchronization is not enabled  
SyncChannalType.BUNGEECORD Data synchronization is enabled, and the channel is Bungerecord  
SyncChannalType.REDIS Data synchronization is enabled, and the channel is Redis

```java
MPointsAPI.isExistsign(String sign);
```
Check whether the point sign exists, return Boolean

```java
MPointsAPI.getPointsList();
```
Get point sign list，return Set&lt;String&gt;

```java
MPointsAPI mpapi = new MPointsAPI(sign);
```

```java
mpapi.formatdouble(String amount);
```
Format amount, return BigDecimal

```java
mpapi.getdisplay(BigDecimal balance);
```
Convert the amount to display message, return String  
For example: 5,000 dollars

```java
mpapi.createPlayerData(uid, name);
```
Creating player data, return boolean

```java
mpapi.getPlayerData(UUID uid);
mpapi.getPlayerData(String name);
```
Get player data，return PlayerData  
If return null, this player is not existed

```java
//Get player UUID
PlayerData.getUniqueId();
//Get player name
PlayerData.getName();
//Get player balance，return BigDecimal
PlayerData.getBalance();
//Get the sign of data，return String
PlayerData.getPsign();
```
About PlayerData

```java
mpapi.ismaxnumber(BigDecimal amount);
```
Check whether the amount is the maximum value, return boolean

```java
mpapi.changePlayerBalance(UUID u, String playername, BigDecimal amount, Boolean isadd);
mpapi.changePlayerBalance(UUID u, String playername, BigDecimal amount, Boolean isadd, String pluginname);
```
Modify the amount, return Integer  
isadd = true, add amount to balacne  
isadd = false, take amount from balance  
isadd = null, set amount to balance  
return 0 means success  
return 1 means failure that BungeeCord mode is enabled and no player is online  
return 2 means failure that the player's balance is insufficient  
return 3 means failure that the player's balance exceeds the maximum value

```java
mpapi.getbalancetop();
```
Get the list of TOP10, return List<String>

```java
mpapi.getsumbalance();
```
Get the total amount of the server, return BigDecimal