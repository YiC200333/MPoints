##About XConomyAPI

For MPoints 1.1.10

Chinese
```xml
MPointsAPI mpapi = new MPointsAPI;
```

```xml
mpapi.getversion();
```
获取XConomy版本号，返回String

```xml
mpapi.isbungeecordmode();
```
是否启用BC模式，返回Boolean

```xml
mpapi.translateUUID(String playername);
```
将玩家名称转换为UUID，返回UUID
如果玩家不存在，返回null

```xml
mpapi.isexistsign(String sign);
```
检测点数标识是否存在，返回Boolean

```xml
mpapi.formatdouble(String sign, String amount);
```
格式化amount，返回BigDecimal

```xml
mpapi.getdisplay(String sign, BigDecimal balance);
```
将金额转为显示消息，返回String
比如 5,000 元

```xml
mpapi.getbalance(String sign, UUID uid);
```
获取玩家金额，返回BigDecimal

```xml
mpapi.ismaxnumber(String sign, BigDecimal amount);
```
检查金额是否为最大值，返回Boolean

```xml
mpapi.changebalance(String sign, UUID u, String playername, BigDecimal amount, Boolean isadd);
```
对金额进行修改，返回Integer
isadd = true 为增加金额
isadd = false 为扣除金额
isadd = null 为设置金额
返回0 表示成功
返回1 失败，表示BC模式且没有玩家存在
返回2 失败,表示玩家余额不足
返回3 失败,表示玩家余额超出最大值

```xml
mpapi.getbalancetop(String sign);
```
获取TOP10名单，返回List<String>

```xml
mpapi.getsumbalance(String sign);
```
获取服务器金额总数，返回BigDecimal


****


English
```xml
MPointsAPI mpapi = new MPointsAPI;
```

```xml
mpapi.getversion();
```
Gets the xconomy version number, return String

```xml
mpapi.isbungeecordmode();
```
Check whether BC mode is enabled, return Boolean

```xml
mpapi.translateUUID(String playername);
```
Convert the player name to UUID, return UUID
If the player does not exist, return null

```xml
mpapi.isexistsign(String sign);
```
Check whether the point sign exists, return Boolean

```xml
mpapi.formatdouble(String sign, String amount);
```
Format amount, return BigDecimal

```xml
mpapi.getdisplay(String sign, BigDecimal balance);
```
Convert the amount to display message, return String
For example: 5,000 dollars

```xml
mpapi.getbalance(String sign, UUID uid);
```
Get player amount, return BigDecimal

```xml
mpapi.ismaxnumber(String sign, BigDecimal amount);
```
Check whether the amount is the maximum value, return Boolean

```xml
mpapi.changebalance(String sign, UUID u, String playername, BigDecimal amount, Boolean isadd);
```
Modify the amount, return Integer
isadd = true, add amount to balacne
isadd = false, take amount from balance
isadd = null, set amount to balance
return 0 means success
return 1 means failure that BungeeCord mode is enabled and no player is online
return 2 means failure that the player's balance is insufficient
return 3 means failure that the player's balance exceeds the maximum value

```xml
mpapi.getbalancetop(String sign);
```
Get the list of TOP10, return List<String>

```xml
mpapi.getsumbalance(String sign);
```
Get the total amount of the server, return BigDecimal
```