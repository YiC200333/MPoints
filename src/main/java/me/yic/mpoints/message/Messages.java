package me.yic.mpoints.message;

import me.yic.mpoints.MPoints;

public class Messages {

	public static String systemMessage(String message) {
		String newMessage = message;
		String lang = MPoints.getInstance().lang();
		switch (message) {
			case "数据保存方式 - SQLite":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "Saving method - SQLite";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Méthode de sauvegarde - SQLite";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Metodo de Guardado - SQLite";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Метод сохранения - SQLite";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "數據保存方式- SQLite";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "Method Kaydediliyor - SQLite";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "データを保存の方法 - SQLite";
				}
				break;
			case "SQLite连接正常":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "SQLite successfully connected";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "SQLite connecté avec succès";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Conectado con SQLite";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Успешное подключение к SQLite";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "SQLite連接正常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "SQLite bağlantısı başarılı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "SQLite連結正常";
				}
				break;
			case "SQLite连接异常":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "SQLite unsuccessfully connected";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Connexion infructueuse à SQLite";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Fallo al conectar con SQLite";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Не успешное подключение к SQLite";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "SQLite連接異常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "SQLite tekrar bağlantı başarılı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "SQLite連結エラー";
				}
				break;
			case "自定义文件夹路径不存在":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "The custom folder path does not exist";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "The custom folder path does not exist";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "The custom folder path does not exist";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "The custom folder path does not exist";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "自定義文件夾路徑不存在";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "The custom folder path does not exist";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "選択したのフォルダは不存在です";
				}
				break;
			case "数据保存方式 - MySQL":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "Saving method - MySQL";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Méthode de sauvegarde - MySQL";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Metodo de Guardado - MySQL";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Метод сохранения - MySQL";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "數據保存方式- MySQL";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "Method Kaydediliyor - MySQL";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "データを保存の方法 - MySQL";
				}
				break;
			case "MySQL连接正常":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "MySQL successfully connected";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "MySQL connecté avec succès";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Conectado con MySQL";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Успешное подключение к MySQL";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "MySQL連接正常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "MySQL bağlantısı başarılı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "MySQL連結正常";
				}
				break;
			case "MySQL连接异常":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "MySQL unsuccessfully connected";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Connexion infructueuse à MySQL";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Fallo al conectar con MySQL";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Не успешное подключение к MySQL";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "MySQL連接異常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "MySQL tekrar bağlantı başarılı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "MySQL連結異常";
				}
				break;
			case "MySQL重新连接成功":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "MySQL successfully reconnected";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "MySQL reconnecté avec succès";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Reconectado con MySQL";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Успешное переподключение к MySQL";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "MySQL重新連接成功";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "MySQL bağlantısı başarısız";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "MySQL再連結成功";
				}
				break;
			case "MPoints加载成功":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "MPoints successfully enabled";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "MPoints activé avec succès";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "MPoints Habilitado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "MPoints успешно включен";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "MPoints加載成功";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "MPoints aktif edildi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "MPointsロード完了";
				}
				break;
			case "MPoints已成功卸载":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "MPoints successfully disabled";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "MPoints désactivé avec succès";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "MPoints Deshabilitado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "MPoints успешно выключен";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "MPoints已成功卸載";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "MPoints devredışı bırakıldı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "MPointsアンインストール成功";
				}
				break;
			case "已开启BC同步":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "BungeeCord mode enabled";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Mode BungeeCord activé";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Modo BungeeCord Habilitado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Режим BungeeCord включен";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "已開啟BC同步";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "BungeeCord modu aktif edildi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "BungeeCord mode enabled";
				}
				break;
			case "SQLite文件路径设置错误":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "SQLite file path setting error";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "SQLite file path setting error";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "SQLite file path setting error";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "SQLite file path setting error";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "SQLite文件路徑設置錯誤";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "SQLite file path setting error";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "SQLiteファイル位置(Path)設置エラー";
				}
				break;
			case "BC同步未开启":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "BungeeCord mode disabled";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Mode BungeeCord désactivé";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Modo BungeeCord Deshabilitado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Режим BungeeCord выключен";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "BC同步未開啓";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "BungeeCord modu kapatıldı";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "BungeeCord mode disabled";
				}
				break;
			case "无法连接到数据库-----":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "Unable to connect to database -----";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Impossible de se connecter à la base de données -----";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Incapaz de conectar a la Base de Datos -----";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Невозможно подключиться к базе данных -----";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "無法連接到數據庫-----";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "Veritabanına bağlanılamıyor -----";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "データベースが連結できませんでした -----";
				}
				break;
			case "JDBC驱动加载失败":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "JDBC driver load failed";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Le chargement du pilote JDBC a échoué";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Controlador JDBC Fallo al Cargar";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Не удалось загрузить драйвер JDBC";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "JBDC驅動加載失敗";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "JDBC sürücüsü yüklenemedi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "JDBCドライブロードエラー";
				}
				break;
			case "MySQL连接断开失败":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "MySQL disconnect failed";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Logging out of MySQL failed";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "La desconexion con MySQL fallo";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Не удалось отключиться от MySQL";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "MySQL連接斷開失敗";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "MySQL bağlantısı kesilemedi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "MySQL切断エラー";
				}
				break;
			case "已创建一个新的语言文件":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "The new language file has been created";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Le nouveau fichier linguistique a été créé";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "El nuevo Archivo de Lenguaje fue creado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Новый языковой файл был создан";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "已創建一個新的語言檔案";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "Yeni dil dosyası oluşturuldu";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "新しいの言語ファイルが作成できました";
				}
				break;
			case "语言文件创建异常":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "Exception in creating language file";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Exception à la création d'un fichier linguistique";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Excepcion al crear archivo de lenguaje";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Исключение при создании языкового файла";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "語言檔案創建异常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "Dil dosyası oluşturulurken hata oluştu";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "新しいの言語ファイルが作成エラー";
				}
				break;
			case "文件创建异常":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "Exception in creating file";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Exception in creating file";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Exception in creating file";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Exception in creating file";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "文件創建异常";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "Exception in creating file";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "Exception in creating file";
				}
				break;
			case "发现 PlaceholderAPI":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "Found PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Found PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Found PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Found PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "發現 PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "Found PlaceholderAPI";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "PlaceholderAPIが発見された";
				}
				break;
			case "发现 DatabaseDrivers":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "Found DatabaseDrivers";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Found DatabaseDrivers";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Found DatabaseDrivers";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Found DatabaseDrivers";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "發現 DatabaseDrivers";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "Found DatabaseDrivers";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "DatabaseDriversが発見された";
				}
				break;
			case "已是最新版本":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "Is the latest version";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Est-ce la dernière version";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Es la version mas nueva";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Последняя версия";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "已是最新版本";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "En son sürüm";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "既に最新バージョンです";
				}
				break;
			case "检查更新失败":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "Check update failed";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "Échec de la mise à jour des contrôles";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "Fallo al buscar actualizaciones";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "Проверить обновление не удалось";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "檢查更新失敗";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "Güncelleme kontrol edilemedi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "アップデート検査が失敗しました";
				}
				break;
			case "§amessage.yml重载成功":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "§amessage.yml reloaded successfully";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "§amessage.yml reloaded successfully";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "§amessage.yml reloaded successfully";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "§amessage.yml reloaded successfully";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "§amessage.yml重載成功";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "§amessage.yml reloaded successfully";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "§amessage.ymlリロード";
				}
				// -----------------------------------------------------------------------------------------
				break;
			case " 名称已更改!":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = " username has been changed";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = " le nom d'utilisateur a été modifié";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = " El Nombre de usuario fue cambiado";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = " имя пользователя было изменено";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = " 名稱已更改！";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = " kullanıcı adı değiştirildi";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = " username 変更成功";
				}
				break;
			case "§cBC模式开启的情况下,无法在无人的服务器中使用OP命令":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "§cWhen the BC mode is enabled, the op command cannot be used in the server without player";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "§cLorsque le mode BC est activé, la commande op ne peut pas être utilisée dans le serveur sans joueur";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "§cCuando el modo BC esta habilitado, el comando op no puede ser utilizado en el servidor sin jugadores";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "§cКогда режим BC включен, команда op не может использоваться на сервере без игрока";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "§cBC模式開啟的情况下，無法在無人的服務器中使用OP命令";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "§cBC modu etkinleştirildiğinde, op komutları oyuncu sunucuda değilse kullanılamaz";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "§cBCモードONの状態で，人がないの時 OP命令が使用できません";
				}
				break;
			case "§6控制台无法使用该指令":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "§6This command cannot be used by the console";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "§6Cette commande ne peut pas être utilisée par la console";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "§6Este comando no puede ser utilizado por la Consola";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "§6Эта команда не может быть использована консолью";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "§6控制台無法使用該指令";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "§6Bu komut sadece konsoldan uygulanabilir";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "§6コンソールがこの命令を使えできません";
				}
				break;
			case "MySQL 的 wait_timeout 值过小,不推荐开启连接池":
				if (lang.equalsIgnoreCase("English")) {
					newMessage = "’wait_timeout‘ of value is too small in MySQL. It is not recommended to use the connection pool";
				}
				if (lang.equalsIgnoreCase("French")) {
					newMessage = "’wait_timeout‘ of value is too small in MySQL. It is not recommended to use the connection pool";
				}
				if (lang.equalsIgnoreCase("Spanish")) {
					newMessage = "’wait_timeout‘ of value is too small in MySQL. It is not recommended to use the connection pool";
				}
				if (lang.equalsIgnoreCase("Russian")) {
					newMessage = "’wait_timeout‘ of value is too small in MySQL. It is not recommended to use the connection pool";
				}
				if (lang.equalsIgnoreCase("ChineseTW")) {
					newMessage = "MySQL 的 wait_timeout 值過小,不推薦開啓連接池";
				}
				if (lang.equalsIgnoreCase("Turkish")) {
					newMessage = "’wait_timeout‘ of value is too small in MySQL. It is not recommended to use the connection pool";
				}
				if (lang.equalsIgnoreCase("Japanese")) {
					newMessage = "’wait_timeout‘ of value is too small in MySQL. It is not recommended to use the connection pool";
				}
				break;
		}
		return newMessage;
	}


	public static String getAuthor() {
		if (MPoints.getInstance().lang().equalsIgnoreCase("Chinese")
				| MPoints.getInstance().lang().equalsIgnoreCase("ChineseTW")) {
			return "伊C";
		} else {
			return "YiC";
		}
	}

}
