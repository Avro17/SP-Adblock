## English Description

# SP Adblock - Your Personal Chat Filter

Tired of spam, ads, and unwanted messages cluttering your chat? **SP Adblock** is a powerful client-side mod that gives you complete control over what you see by allowing you to create and manage flexible filter lists.

This mod is made for blocking ads in chat on SPWorlds SP5 server, but it should work anywhere. It works silently in the background, cleaning up your chat feed from everything you don't want to see.

### 🚀 Key Features

*   **🛡️ Cloud-Powered Dynamic Filters:** The mod automatically downloads and updates SP5 chat blocklists from GitHub URLs every time you launch the game. You'll always be protected by the most up-to-date filters!
*   **⚙️ Fully Customizable Local Filter:** Create your own personal blocklist of words using simple in-game commands.
*   **🔇 Persistent Player Muting:** Mute any player by their username. The mod saves their unique ID (UUID), so the mute will persist even if they change their name.
*   **🖥️ Convenient GUI via ModMenu:** Easily enable or disable any of your available filter lists (two defaults, your local list, and any custom ones you've added) through the ModMenu interface.
*   **📝 Support for Custom Filter Lists:** Simply drop your own `.json` file with keywords into the mod's config folder, and it will automatically appear in the GUI as a new, toggleable filter.
*   **🧠 Intelligent Chat Parsing:** The mod is designed to understand custom chat formats (e.g., `[Prefix] Nickname: message`), accurately separating usernames from the message content to avoid false positives.

### 🔧 How It Works

All settings are stored in the `config/spadblock` folder:
*   **`config1.json` / `config2.json`:** Local copies of the filters downloaded from GitHub.
*   **`local_words.json`:** Your personal blocklist, managed via in-game commands.
*   **`muted_players.json`:** The list of muted players (stores both username and UUID).
*   **any_other_file.json:** Just add your own file with keywords to this folder, and it will show up in the config menu!

### ⌨️ Commands

#### Filter Management
*   `/adblock add [word]` - Add a word to your local filter.
*   `/adblock remove [word]` - Remove a word from your local filter.
*   `/adblock list` - Display all words in your local filter.

#### Mute Management
*   `/mute [nickname]` - Mute a player. `Tab` completion will suggest online players.
*   `/unmute [nickname]` - Unmute a player.
*   `/mutelist` - Display a list of all players you have muted.

## Описание на русском языке

# SP Adblock - Ваш Персональный Фильтр Чата

Устали от спама, рекламы и нежелательных сообщений в чате? **SP Adblock** — это мощный клиентский мод, который дает вам полный контроль над тем, что вы видите, позволяя создавать и использовать гибкие списки фильтрации.

Этот мод был создан для блокировки рекламы в чате на сервере СП5. Он работает в фоне, незаметно очищая ваш чат от всего, что вы не хотите видеть.

### 🚀 Ключевые особенности

*   **🛡️ Динамические фильтры из облака:** Мод автоматически скачивает и обновляет списки блокировки по ссылкам на GitHub при каждом запуске игры. Вы всегда будете защищены актуальными фильтрами!
*   **⚙️ Полная кастомизация:** Создайте свой собственный локальный список блокировки слов с помощью простых команд прямо в игре.
*   **🔇 Гибкая система мьюта:** Замьютте любого игрока по нику. Мод запомнит его уникальный ID (UUID), поэтому мьют будет работать, даже если игрок сменит никнейм.
*   **🖥️ Удобный GUI:** Через **ModMenu** вы можете легко включать и отключать любые из доступных фильтров (Прыград, полная блокировка, локальный и любые добавленные вами).
  
*   **❗ Учтите, что по умолчанию включен только конфиг "Прыград".❗**
  ![Config](https://cdn.modrinth.com/data/cached_images/4449ad82e49177b9d27ee8729cd02e9d244455e7.png)
  
* **📝 Поддержка своих фильтров:** Просто добавьте свой `.json` файл со словами в папку мода, и он автоматически появится в GUI как новый доступный фильтр.
![Custom Config](https://cdn.modrinth.com/data/cached_images/41f81aac569ce50086217063f1374416da50d37c.png)
*   **🧠 Умная фильтрация:** Мод умеет анализировать сообщения с кастомными префиксами (например, `[Префикс] Ник: сообщение`), чтобы точно отделять ники от текста и избегать ложных срабатываний.

### 🔧 Как это работает

Все настройки хранятся в папке `config/spadblock`:
*   **`config1.json` / `config2.json`:** Локальные копии фильтров, скачанные с GitHub.
*   **`local_words.json`:** Ваш персональный список блокировки, управляемый через команды.
*   **`muted_players.json`:** Список замьюченных игроков (хранятся ник и UUID).
*   **`Любой_другой_файл.json`:** Просто добавьте свой файл с ключевыми словами в эту папку, и он появится в меню настроек!                                 
![config folder](https://cdn.modrinth.com/data/cached_images/701640348e6f66dde3e8c4f01d62ff1ae0d2c7cc.png)

### ⌨️ Команды

#### Управление фильтром
*   `/adblock add [слово]` — добавить слово в ваш локальный фильтр.
*   `/adblock remove [слово]` — удалить слово из вашего локального фильтра.
*   `/adblock list` — показать все слова из вашего локального фильтра.
![Block Words](https://cdn.modrinth.com/data/cached_images/1e2175b08e880a1b72cc1589d503774d4fd8c9cb.png)
#### Управление мьютами
*   `/mute [ник]` — замьютить игрока. Автодополнение по `Tab` покажет игроков онлайн.
*   `/unmute [ник]` — размьютить игрока.
*   `/mutelist` — показать список всех замьюченных вами игроков.
![Mute](https://cdn.modrinth.com/data/cached_images/cc4947a26b5f66d34e710265298a86f130a4246d.png) 
