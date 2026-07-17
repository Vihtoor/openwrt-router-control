package com.example

object IperfLocalizations {
    fun getDialogBody(lang: String): String {
        return when (lang) {
            "ru" -> """
iperf3 — это культовый, признанный во всем мире консольный инструмент для измерения максимальной пропускной способности сети между двумя устройствами.

В отличие от всех предыдущих тестов (M-Lab, Cloudflare, Fast.com), iperf3 не использует интернет-серверы и не зависит от вашего интернет-провайдера. Он измеряет чистую скорость кабеля или Wi-Fi внутри вашей локальной сети, либо на конкретном выделенном участке между вашим роутером и удаленным сервером.

## Подробное описание iperf3

**Суть:** Профессиональный эталон тестирования сетевого железа «всухую».

Если M-Lab оценивает, как работает интернет-стандарт TCP в глобальной сети, то iperf3 создан для жесткого стресс-тестирования сетевых карт, процессоров роутеров, качества Wi-Fi покрытия и пропускной способности Ethernet-кабелей.

**Как работает (Клиент-Серверная архитектура):** Для теста всегда нужны два устройства. Одно запускается в режиме сервера (оно просто слушает порт и принимает или отправляет данные), а второе — в режиме клиента (оно генерирует трафик и инициирует тест).

**Генерация трафика:** iperf3 создает искусственный, максимально плотный поток неподписанных данных. Он может гонять трафик по протоколу TCP (проверяя надежность передачи и скорость с учетом подтверждения пакетов) или по протоколу UDP (проверяя максимальную пропускную способность без подтверждения, что идеально для выявления реальной потери пакетов — Packet Loss и джиттера).

**Зачем он нужен на роутере:** Роутер — это специализированный компьютер. Когда вы запускаете тест iperf3 прямо на роутере под управлением OpenWrt, вы можете узнать, справляется ли его процессор (CPU) с прокачкой гигабитного трафика, или узким местом является беспроводной чип Wi-Fi.

## Инструкция по установке и настройке iperf3 на OpenWrt

Поскольку iperf3 — утилита консольная, мы рассмотрим самый надежный способ установки через SSH, а также альтернативный вариант через графический интерфейс LuCI.

### Шаг 1. Установка пакета

**Вариант А: Через консоль (SSH) — самый быстрый способ**
Обновите список доступных пакетов OpenWrt введя в консоли терминала роутера:
`opkg update`
Установите iperf3:
`opkg install iperf3`

**Вариант Б: Через графический интерфейс LuCI**
1. Откройте в браузере админку роутера (обычно 192.168.1.1).
2. Перейдите в меню Система (System) -> Программное обеспечение (Software).
3. Нажмите кнопку Обновить списки (Update lists...).
4. В поле «Фильтр» (Filter) введите iperf3.
5. Найдите пакет iperf3 в списке и нажмите кнопку Установить (Install).

## Как запускать тест

⚠️ **Важное правило для мобильных устройств:** Экран смартфона во время теста должен быть включен, а само приложение — активно (открыто на экране). Как только экран гаснет или приложение сворачивается, агрессивные энергосберегающие алгоритмы Android/iOS мгновенно урезают скорость Wi-Fi чипа или замораживают фоновые процессы, что полностью исказит результаты теста.

### Запуск команд из консоли роутера OpenWrt

В командах используется IP-адрес вашего смартфона (мы возьмем для примера 192.168.1.150).

**Тест 1: Проверка скорости отправки данных с роутера на смартфон (Download для смартфона) (Увеличенное время)**
Роутер будет непрерывно генерировать и отправлять TCP-трафик на ваш смартфон в течение одной минуты. Этого времени достаточно, чтобы зафиксировать, не перегревается ли процессор роутера при длительной нагрузке.
`iperf3 -c 192.168.1.150 -t 60`
Что происходит: Роутер в течение 60 секунд забивает беспроводной канал данными, отправляя их на телефон. Вы увидите отчет прямо в консоли роутера.

**Тест 2: Длительный реверсивный тест (Скачивание со смартфона)**
Проверяем, как смартфон передает данные на роутер в течение 45 секунд. Отличный способ проверить, не начинает ли смартфон сбрасывать скорость (троттлить) из-за нагрева беспроводного модуля. На роутере используется флаг -R (Reverse — реверсивный режим):
`iperf3 -c 192.168.1.150 -R -t 45`
Что происходит: Роутер дает команду серверу на смартфоне: «Теперь ты генерируй трафик и шли его мне, а я буду мерить скорость приема». Это идеальный способ проверить качество передатчика внутри смартфона.

**Тест 3: Стресс-тест в несколько потоков на полторы минуты**
Запуск 4 параллельных потоков на 90 секунд. Позволяет детально отследить стабильность распределения ресурсов процессора OpenWrt и равномерность заполнения буфера. Если у вашего роутера слабый одноядерный процессор, запуск теста в один поток может уткнуться в производительность CPU. Чтобы распределить нагрузку, запустите тест с помощью флага -P:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Тест 4: Проверка потерь в Wi-Fi через UDP (Тест на 2 минуты)**
Когда мы проверяем качество покрытия Wi-Fi, нам нужно время, чтобы физически отойти со смартфоном в другую комнату, встать за несущую стену или повернуться спиной к роутеру. Запустим UDP-тест на 120 секунд с полосой 200 Мегабит (флаг -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
На что смотреть в результате: В финальном отчете обратите внимание на графу Lost/Total Jitter. Если процент потерь (Lost) выше 1-2%, значит, в этой точке квартиры Wi-Fi эфир сильно зашумлен или роутеру не хватает мощности.

## Что делать, если "не соединяется"?

**Проверьте изоляцию клиентов (Client Isolation):** В настройках Wi-Fi на OpenWrt (меню Network -> Wireless -> Edit вашей сети) убедитесь, что выключена галочка Isolate Clients. Если она включена, роутер аппаратно запрещает устройствам внутри Wi-Fi общаться друг с другом и с самим роутером напрямую.

**Файрвол смартфона:** На Android-устройствах (особенно с кастомными прошивками) встроенные "оптимизаторы батареи" или мобильные антивирусы могут блокировать входящие подключения на порт 5201.

**Частота Wi-Fi:** Для объективного теста убедитесь, что смартфон подключен именно к сети 5 ГГц (если роутер двухдиапазонный). На частоте 2.4 ГГц из-за соседских роутеров скорость редко поднимается выше 50-70 Мбит/с.
""".trimIndent()

            "uk" -> """
iperf3 — це культовий, визнаний у всьому світі консольний інструмент для вимірювання максимальної пропускної здатності мережі між двома пристроями.

На відміну від усіх попередніх тестів (M-Lab, Cloudflare, Fast.com), iperf3 не використовує інтернет-сервери та не залежить від вашого інтернет-провайдера. Він вимірює чисту швидкість кабелю або Wi-Fi всередині вашої локальної мережі, або на конкретній виділеній ділянці між вашим роутером і віддаленим сервером.

## Детальний опис iperf3

**Суть:** Професійний еталон тестування мережевого заліза «всуху».

Якщо M-Lab оцінює, як працює інтернет-стандарт TCP у глобальній мережі, то iperf3 створений для жорсткого стрес-тестування мережевих карт, процесорів роутерів, якості Wi-Fi покриття та пропускної здатності Ethernet-кабелів.

**Як працює (Клієнт-Серверна архітектура):** Для тесту завжди потрібні два пристрої. Один запускається в режимі сервера (він просто слухає порт і приймає чи відправляє дані), а другий — в режимі клієнта (він генерує трафік та ініціює тест).

**Генерація трафіку:** iperf3 створює штучний, максимально щільний потік непідписаних даних. Він може ганяти трафік за протоколом TCP (перевіряючи надійність передачі та швидкість з урахуванням підтвердження пакетів) або за протоколом UDP (перевіряючи максимальну пропускну здатність без підтвердження, що ідеально для виявлення реальної втрати пакетів — Packet Loss і джиттеру).

**Навіщо він потрібен на роутері:** Роутер — це спеціалізований комп'ютер. Коли ви запускаєте тест iperf3 прямо на роутері під керуванням OpenWrt, ви можете дізнатися, чи справляється його процесор (CPU) з прокачуванням гігабітного трафіку, чи вузьким місцем є бездротовий чип Wi-Fi.

## Інструкція зі встановлення та налаштування iperf3 на OpenWrt

Оскільки iperf3 — утиліта консольна, ми розглянемо найнадійніший спосіб встановлення через SSH, а також альтернативний варіант через графічний інтерфейс LuCI.

### Крок 1. Встановлення пакета

**Варіант А: Через консоль (SSH) — найшвидший спосіб**
Оновіть список доступних пакетів OpenWrt, ввівши в консолі терміналу роутера:
`opkg update`
Встановіть iperf3:
`opkg install iperf3`

**Варіант Б: Через графічний інтерфейс LuCI**
1. Відкрийте в браузері адмінку роутера (зазвичай 192.168.1.1).
2. Перейдіть до меню Система (System) -> Програмне забезпечення (Software).
3. Натисніть кнопку Оновити списки (Update lists...).
4. У полі «Фільтр» (Filter) введіть iperf3.
5. Знайдіть пакет iperf3 у списку та натисніть кнопку Встановити (Install).

## Як запускати тест

⚠️ **Важливе правило для мобільних пристроїв:** Екран смартфона під час тесту має бути увімкненим, а сам додаток — активним (відкритим на екрані). Як тільки екран гасне або додаток згортається, агресивні енергозберігаючі алгоритми Android/iOS миттєво урізають швидкість Wi-Fi чипа або заморожують фонові процеси, що повністю спотворить результати тесту.

### Запуск команд із консолі роутера OpenWrt

У командах використовується IP-адреса вашого смартфона (ми візьмемо для прикладу 192.168.1.150).

**Тест 1: Перевірка швидкості відправки даних з роутера на смартфон (Download для смартфона) (Збільшений час)**
Роутер безперервно генеруватиме і відправлятиме TCP-трафік на ваш смартфон протягом однієї хвилини. Цього часу достатньо, щоб зафіксувати, чи не перегрівається процесор роутера при тривалому навантаженні.
`iperf3 -c 192.168.1.150 -t 60`
Що відбувається: Роутер протягом 60 секунд забиває бездротовий канал даними, відправляючи їх на телефон. Ви побачите звіт прямо в консолі роутера.

**Тест 2: Тривалий реверсивний тест (Завантаження зі смартфона)**
Перевіряємо, як смартфон передає дані на роутер протягом 45 секунд. Відмінний спосіб перевірити, чи не починає смартфон скидати швидкість (тротлитися) через нагрівання бездротового модуля. На роутері використовується прапор -R (Reverse — реверсивний режим):
`iperf3 -c 192.168.1.150 -R -t 45`
Що відбувається: Роутер дає команду серверу на смартфоні: «Тепер ти генеруй трафік і шли його мені, а я буду міряти швидкість прийому». Це ідеальний спосіб перевірити якість передавача всередині смартфона.

**Тест 3: Стрес-тест у кілька потоків на півтори хвилини**
Запуск 4 паралельних потоків на 90 секунд. Дозволяє детально відстежити стабільність розподілу ресурсів процесора OpenWrt та рівномірність заповнення буфера. Якщо у вашого роутера слабкий одноядерний процесор, запуск тесту в один потік може упертися в продуктивність CPU. Щоб розподілити навантаження, запустіть тест за допомогою прапора -P:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Тест 4: Перевірка втрат у Wi-Fi через UDP (Тест на 2 хвилини)**
Коли ми перевіряємо якість покриття Wi-Fi, нам потрібен час, щоб фізично відійти зі смартфоном в іншу кімнату, встати за несучу стіну або повернутися спиною до роутера. Запустимо UDP-тест на 120 секунд із смугою 200 Мегабіт (прапор -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
На що дивитися в результаті: У фінальному звіті зверніть увагу на графу Lost/Total Jitter. Якщо відсоток втрат (Lost) вищий за 1-2%, значить, у цій точці квартири Wi-Fi ефір сильно зашумлений або роутеру не вистачає потужності.

## Що робити, якщо "не з'єднується"?

**Перевірте ізоляцію клієнтів (Client Isolation):** У налаштуваннях Wi-Fi на OpenWrt (меню Network -> Wireless -> Edit вашої мережі) переконайтеся, що вимкнено галочку Isolate Clients. Якщо вона увімкнена, роутер апаратно забороняє пристроям усередині Wi-Fi спілкуватися один з одним і з самим роутером безпосередньо.

**Файрвол смартфона:** На Android-пристроях (особливо з кастомними прошивками) вбудовані "оптимізатори батареї" або мобільні антивіруси можуть блокувати вхідні підключення на порт 5201.

**Частота Wi-Fi:** Для об'єктивного тесту переконайтеся, що смартфон підключений саме до мережі 5 ГГц (якщо роутер дводіапазонний). На частоті 2.4 ГГц через сусідські роутери швидкість рідко піднімається вище 50-70 Мбіт/с.
""".trimIndent()

            "be" -> """
iperf3 — гэта культавы, прызнаны ва ўсім свеце кансольны інструмент для вымярэння максімальнай прапускной здольнасці сеткі паміж двума прыладамі.

У адрозненне ад усіх папярэдніх тэстаў (M-Lab, Cloudflare, Fast.com), iperf3 не выкарыстоўвае інтэрнэт-серверы і не залежыць ад вашага інтэрнэт-правайдэра. Ён вымярае чыстую хуткасць кабелю або Wi-Fi унутры вашай лакальнай сеткі, альбо на канкрэтным вылучаным участку паміж вашым роўтарам і аддаленым серверам.

## Падрабязнае апісанне iperf3

**Сутнасць:** Прафесійны эталон тэставання сеткавага абсталявання «ўсухую».

Калі M-Lab ацэньвае, як працуе інтэрнэт-стандарт TCP у глабальнай сетцы, то iperf3 створаны для жорсткага стрэс-тэставання сеткавых карт, працэсараў роўтараў, якасці Wi-Fi пакрыцця і прапускной здольнасці Ethernet-кабеляў.

**Як працуе (Кліент-Серверная архітэктура):** Для тэсту заўсёды патрэбны дзве прылады. Адно запускаецца ў рэжыме сервера (яно проста слухае порт і прымае ці адпраўляе даныя), а другое — у рэжыме кліента (яно генеруе трафік і ініцыюе тэст).

**Генерацыя трафіку:** iperf3 стварае штучны, максімальна шчыльны паток непадпісаных даных. Ён можа ганяць трафік па пратаколе TCP (правяраючы надзейнасць перадачы і хуткасць з улікам пацвярджэння пакетаў) або па пратаколе UDP (правяраючы максімальную прапускную здольнасць без пацвярджэння, што ідэальна для выяўлення рэальнай страты пакетаў — Packet Loss і джытэру).

**Навошта ён патрэбен на роўтары:** Роўтар — гэта спецыялізаваны камп'ютар. Калі вы запускаеце тэст iperf3 прама на роўтары пад кіраваннем OpenWrt, вы можаце даведацца, ці спраўляецца яго працэсар (CPU) з прапампоўкай гігабітнага трафіку, ці вузкім месцам з'яўляецца бесправадны чып Wi-Fi.

## Інструкцыя па ўсталёўцы і наладзе iperf3 на OpenWrt

Паколькі iperf3 — утыліта кансольная, мы разгледзім самы надзейны спосаб усталёўкі праз SSH, а таксама альтэрнатыўны варыянт праз графічны інтэрфейс LuCI.

### Крок 1. Усталёўка пакета

**Варыянт А: Праз кансоль (SSH) — самы хуткі спосаб**
Абнавіце спіс даступных пакетаў OpenWrt увёўшы ў кансолі тэрмінала роўтара:
`opkg update`
Усталюйце iperf3:
`opkg install iperf3`

**Варыянт Б: Праз графічны інтэрфейс LuCI**
1. Адкрыйце ў браўзеры адмінку роўтара (звычайна 192.168.1.1).
2. Перайдзіце ў меню Сістэма (System) -> Програмнае забеспячэнне (Software).
3. Націсніце кнопку Абнавіць спісы (Update lists...).
4. У полі «Фільтр» (Filter) увядзіце iperf3.
5. Знайдзіце пакет iperf3 у спісе і націсніце кнопку Усталяваць (Install).

## Як запускаць тэст

⚠️ **Важнае правіла для мабільных прылад:** Экран смартфона падчас тэсту павінен быць уключаны, а само прыкладанне — актыўна (адкрыта на экране). Як толькі экран гасне або прыкладанне згортваецца, агрэсіўныя энергазберагальныя алгарытмы Android/iOS імгненна зразаюць хуткасць Wi-Fi чыпа або замарожваюць фонавыя працэсы, што цалкам сказіць вынікі тэсту.

### Запуск каманд з кансолі роўтара OpenWrt

У камандах выкарыстоўваецца IP-адрас вашага смартфона (мы возьмем для прыкладу 192.168.1.150).

**Тэст 1: Праверка хуткасці адпраўкі даных з роўтара на смартфон (Download для смартфона) (Павялічаны час)**
Роўтар будзе бесперапынна генераваць і адпраўляць TCP-трафік на ваш смартфон на працягу адной хвіліны. Гэтага часу дастаткова, каб зафіксаваць, ці не пераграваецца працэсар роўтара пры працяглай нагрузцы.
`iperf3 -c 192.168.1.150 -t 60`
Што адбываецца: Роўтар на працягу 60 секунд забівае бесправадны канал данымі, адпраўляючы іх на тэлефон. Вы ўбачыце справаздачу прама ў кансолі роўтара.

**Тэст 2: Працяглы рэверсіўны тэст (Спампоўка са смартфона)**
Правяраем, як смартфон перадае даныя на роўтар на працягу 45 секунд. Выдатны спосаб праверыць, ці не пачынае смартфон скідаць хуткасць (тротліцца) з-за нагрэву бесправаднога модуля. На роўтары выкарыстоўваецца сцяг -R (Reverse — рэверсіўны рэжым):
`iperf3 -c 192.168.1.150 -R -t 45`
Што адбываецца: Роўтар дае каманду серверу на смартфоне: «Цяпер ты генеруй трафік і шлі яго мне, а я буду мерыць хуткасць прыёму». Гэта ідэальны спосаб праверыць якасць перадатчыка ўнутры смартфона.

**Тэст 3: Стрэс-тэст у некалькі патокаў на паўтары хвіліны**
Запуск 4 паралельных патокаў на 90 секунд. Дазваляе дэталёва адсачыць стабільнасць размеркавання рэсурсаў працэсара OpenWrt і раўнамернасцьзапаўнення буфера. Калі ў вашага роўтара слабы аднаядравы працэсар, запуск тэсту ў адзін паток можа ўперціся ў прадукцыйнасць CPU. Каб размеркаваць нагрузку, запусціце тэст з дапамогай сцяга -P:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Тэст 4: Праверка страт у Wi-Fi праз UDP (Тэст на 2 хвіліны)**
Калі мы правяраем якасць пакрыцця Wi-Fi, нам патрэбен час, каб фізічна адысці са смартфонам у іншы пакой, устаць за нясучую сцяну або павярнуцца спіной да роўтара. Запусцім UDP-тэст на 120 секунд з паласой 200 Мегабіт (сцяг -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
На што глядзець у выніку: У фінальнай справаздачы звярніце ўвагу на графу Lost/Total Jitter. Калі працэнт страт (Lost) вышэй за 1-2%, значыць, у гэтай кропцы кватэры Wi-Fi эфір моцна зашумлены або роўтару не хапае магутнасці.

## Што рабіць, калі "не злучаецца"?

**Праверце ізаляцыю кліентаў (Client Isolation):** У наладах Wi-Fi на OpenWrt (меню Network -> Wireless -> Edit вашай сеткі) пераканайцеся, што выключана галачка Isolate Clients. Калі яна ўключана, роўтар апаратна забараняе прыладам унутры Wi-Fi мець зносіны адзін з адным і з самім роўтарам наўпрост.

**Файрвол смартфона:** На Android-прыладах (асабліва з кастомнымі прашыўкамі) убудаваныя "аптымізатары батарэі" або мабільныя антывірусы могуць блакаваць уваходныя падключэнні на порт 5201.

**Частата Wi-Fi:** Для аб'ектыўнага тэсту пераканайцеся, што смартфон падлучаны менавіта да сеткі 5 ГГц (калі роўтар двухдыяпазонны). На частаце 2.4 ГГц з-за суседскіх роўтараў хуткасць рэдка падымаецца вышэй за 50-70 Мбіт/с.
""".trimIndent()

            "de" -> """
iperf3 ist ein ikonisches, weltweit anerkanntes Befehlszeilen-Tool zur Messung des maximalen Netzwerkdurchsatzes zwischen zwei Geräten.

Im Gegensatz zu allen vorherigen Tests (M-Lab, Cloudflare, Fast.com) benötigt iperf3 keinen Internet-Server und ist völlig unabhängig von Ihrem Internetdienstanbieter. Es misst die reine Kabel- oder Wi-Fi-Geschwindigkeit in Ihrem lokalen Netzwerk (LAN) oder auf einer dedizierten Strecke zwischen Ihrem Router und einem Remote-Server.

## Detaillierte iperf3-Beschreibung

**Konzept:** Professioneller Benchmark für Netzwerk-Hardware unter realen Bedingungen.

Während M-Lab bewertet, wie die TCP-Übertragung im globalen Netz arbeitet, ist iperf3 für extreme Stresstests von Netzwerkkarten, Router-CPUs, Wi-Fi-Abdeckung und Ethernet-Kabeldurchsätzen konzipiert.

**Client-Server-Architektur:** Für den Test sind immer zwei Geräte erforderlich. Eines läuft im Server-Modus (es lauscht auf Port 5201 und empfängt/sendet Daten), während das andere im Client-Modus läuft (es generiert den Datenstrom und initiiert den Test).

**Verkehrsgenerierung:** iperf3 erzeugt einen künstlichen, extrem dichten Datenstrom. Es kann Daten über das TCP-Protokoll übertragen (Überprüfung der Übertragungssicherheit durch Paketbestätigung) oder über das UDP-Protokoll (Überprüfung des maximalen Durchsatzes ohne Bestätigung, perfekt zur Ermittlung von Paketverlusten und Jitter).

**Nutzen auf dem Router:** Ein Router ist ein spezialisierter Computer. Wenn Sie iperf3 direkt auf einem OpenWrt-Router ausführen, können Sie feststellen, ob die Router-CPU dem Gigabit-Netzwerkverkehr gewachsen ist oder ob der Wi-Fi-Chip den Flaschenhals bildet.

## Anleitung zur Installation und Konfiguration auf OpenWrt

Da iperf3 ein Konsolen-Dienstprogramm ist, betrachten wir die zuverlässigste Installationsmethode via SSH sowie die alternative Methode über das LuCI-Webmenü.

### Schritt 1: Paketinstallation

**Option A: Über die Konsole (SSH) — der schnellste Weg**
Aktualisieren Sie die Paketliste Ihres OpenWrt-Routers per SSH:
`opkg update`
Installieren Sie iperf3:
`opkg install iperf3`

**Option B: Über das LuCI-Webmenü**
1. Öffnen Sie die Router-Admin-Seite im Browser (normalerweise 192.168.1.1).
2. Gehen Sie zu System -> Software.
3. Klicken Sie auf "Update lists..." (Paketlisten aktualisieren).
4. Geben Sie "iperf3" im Filter-Feld ein.
5. Suchen Sie nach dem Paket iperf3 und klicken Sie auf "Install" (Installieren).

## Test durchführen

⚠️ **Wichtige Regel für Mobilgeräte:** Das Smartphone-Display muss während des gesamten Tests eingeschaltet und die App im Vordergrund aktiv sein. Sobald das Display ausgeht oder die App minimiert wird, drosseln Android/iOS-Energiesparalgorithmen die Wi-Fi-Geschwindigkeit oder frieren den Prozess ein, was die Messung verfälscht.

### Befehlsausführung über die OpenWrt-Konsole

In den Beispielen wird die IP-Adresse des Smartphones verwendet (z. B. 192.168.1.150).

**Test 1: Überprüfung der Senderate vom Router zum Smartphone (Download für Smartphone) (Erhöhte Testzeit)**
Der Router generiert und sendet 60 Sekunden lang TCP-Daten an das Smartphone. Diese Zeit reicht aus, um festzustellen, ob sich die CPU des Routers unter Dauerlast erwärmt.
`iperf3 -c 192.168.1.150 -t 60`
Was passiert: Der Router füllt den WLAN-Kanal 60 Sekunden lang mit Daten. Der Bericht wird direkt im Router-Terminal angezeigt.

**Test 2: Langer Reverse-Test (Senden vom Smartphone an den Router)**
Messung der Datenübertragung vom Smartphone zum Router über 45 Sekunden. Perfekt, um festzustellen, ob das Smartphone aufgrund von Erhitzung des WLAN-Chips drosselt. Auf dem Router wird das Flag -R (Reverse) hinzugefügt:
`iperf3 -c 192.168.1.150 -R -t 45`
Was passiert: Der Router steuert den iperf3-Server auf dem Telefon an: "Sende mir jetzt Daten, während ich die Empfangsrate messe." Ideal zur Überprüfung der WLAN-Sendeleistung des Telefons.

**Test 3: Multithread-Stresstest für 1,5 Minuten**
Führt 4 parallele Datenströme über 90 Sekunden aus. Ermöglicht die genaue Beobachtung der OpenWrt-CPU-Auslastung und Pufferstabilität. Wenn Ihr Router eine schwache Single-Core-CPU hat, kann ein einzelner Thread die CPU limitieren. Um die Last zu verteilen, nutzen Sie das Flag -P:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Test 4: WLAN-Paketverlust-Test über UDP (2-Minuten-Test)**
Wenn Sie die WLAN-Abdeckung testen, müssen Sie Zeit haben, um sich mit dem Smartphone in andere Räume zu bewegen oder sich hinter Wände zu stellen. Wir starten einen UDP-Test für 120 Sekunden mit einer festen Bandbreite von 200 Megabit (Flag -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
Worauf zu achten ist: Beachten Sie im Abschlussbericht die Spalten "Lost/Total Jitter". Wenn der Paketverlust (Lost) über 1-2% liegt, ist das WLAN-Signal an dieser Stelle zu schwach oder gestört.

## Fehlerbehebung: "Keine Verbindung"?

**Client-Isolierung prüfen (Client Isolation):** Stellen Sie im OpenWrt-Menü (Netzwerk -> Drahtlos -> Bearbeiten) sicher, dass die Option "Isolate Clients" deaktiviert ist. Wenn sie aktiv ist, blockiert der Router die direkte Kommunikation zwischen WLAN-Geräten.

**Smartphone-Firewall:** Auf Android-Geräten können aggressive Akku-Optimierer oder Drittanbieter-Antivirenprogramme eingehende Verbindungen auf Port 5201 blockieren.

**WLAN-Frequenz:** Verwenden Sie für objektive Tests die 5-GHz-Frequenz des Routers. Auf 2,4 GHz ist die Datenrate aufgrund von Nachbarnetzen meist auf 50-70 Mbit/s limitiert.
""".trimIndent()

            "es" -> """
iperf3 es una herramienta de consola icónica y mundialmente reconocida para medir el rendimiento máximo de red entre dos dispositivos.

A diferencia de las pruebas anteriores (M-Lab, Cloudflare, Fast.com), iperf3 no utiliza servidores de Internet y es totalmente independiente de su ISP. Mide la velocidad neta del cable o Wi-Fi dentro de su red de área local (LAN), o en un enlace específico entre su enrutador y un servidor remoto.

## Descripción detallada de iperf3

**Concepto:** El estándar profesional de pruebas de estrés directo para hardware de red.

Mientras que M-Lab evalúa el comportamiento general del protocolo TCP en la red global, iperf3 está diseñado para pruebas extremas en tarjetas de red, procesadores de enrutadores, cobertura de Wi-Fi y rendimiento de cables Ethernet.

**Arquitectura Cliente-Servidor:** Para la prueba siempre se requieren dos dispositivos. Uno se ejecuta en modo servidor (simplemente escucha en el puerto 5201 y recibe o envía datos), mientras que el otro se ejecuta en modo cliente (genera el tráfico e inicia la prueba).

**Generación de tráfico:** iperf3 crea un flujo de datos artificial y sumamente denso. Puede transmitir tráfico utilizando el protocolo TCP (verificando la confiabilidad con confirmación de paquetes) o el protocolo UDP (verificando el rendimiento bruto sin confirmaciones, ideal para detectar pérdidas de paquetes y jitter).

**Por qué se necesita en el enrutador:** Un enrutador es una computadora especializada. Cuando ejecuta iperf3 directamente en OpenWrt, puede determinar si la CPU del enrutador soporta el procesamiento de tráfico gigabit o si el chip inalámbrico Wi-Fi es el cuello de botella.

## Instrucciones de instalación y configuración de iperf3 en OpenWrt

Debido a que iperf3 es una utilidad de consola, cubriremos el método de instalación más confiable a través de SSH, así como el método gráfico alternativo mediante LuCI.

### Paso 1: Instalación del paquete

**Opción A: A través de la consola (SSH) — el método más rápido**
Actualice la lista de paquetes de su enrutador OpenWrt mediante terminal SSH:
`opkg update`
Instale iperf3:
`opkg install iperf3`

**Opción B: A través de la interfaz gráfica LuCI**
1. Abra el panel de administración de su enrutador en un navegador web (generalmente 192.168.1.1).
2. Vaya a Sistema -> Software.
3. Haga clic en el botón "Update lists..." (Actualizar listas de paquetes).
4. Escriba "iperf3" en el campo Filtro.
5. Busque el paquete iperf3 en la lista y haga clic en "Install" (Instalar).

## Cómo ejecutar la prueba

⚠️ **Regla crítica para dispositivos móviles:** La pantalla del teléfono inteligente debe permanecer encendida durante todo el test y la aplicación debe estar activa en primer plano. Si la pantalla se apaga o la app se minimiza, los algoritmos de ahorro de batería de Android/iOS disminuirán inmediatamente la velocidad del chip Wi-Fi, distorsionando por completo los resultados.

### Ejecución de comandos en la consola de OpenWrt

En estos ejemplos se utiliza la dirección IP de su teléfono inteligente (tomaremos como referencia la IP 192.168.1.150).

**Prueba 1: Medición de velocidad del enrutador al teléfono (Descarga para smartphone) (Tiempo extendido)**
El enrutador generará y enviará tráfico TCP a su teléfono continuamente por un minuto. Esta duración es idónea para comprobar si la CPU del enrutador se sobrecalienta bajo carga sostenida.
`iperf3 -c 192.168.1.150 -t 60`
Qué ocurre: El enrutador satura el canal Wi-Fi con datos durante 60 segundos de envío. Verá el informe final directamente en el terminal.

**Prueba 2: Test reversible prolongado (Carga del teléfono al enrutador)**
Mide la velocidad de transmisión del teléfono al enrutador durante 45 segundos. Es útil para verificar si el teléfono inteligente reduce su velocidad (throttling) por calentamiento del chip inalámbrico. En el enrutador se utiliza el flag -R (Reverse):
`iperf3 -c 192.168.1.150 -R -t 45`
Qué ocurre: El enrutador ordena al servidor del smartphone: "Ahora tú genera el tráfico y envíamelo, mediré la velocidad de recepción." Ideal para auditar la potencia de transmisión inalámbrica del móvil.

**Prueba 3: Test de estrés multiproceso por 1.5 minutos**
Ejecuta 4 flujos paralelos de datos durante 90 segundos. Permite observar detalladamente la asignación de recursos en la CPU de OpenWrt y la estabilidad de los búferes. Si su enrutador tiene un procesador débil de un solo núcleo, un único flujo puede limitar el rendimiento general. Use el flag -P para distribuir la carga:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Prueba 4: Test de pérdida de paquetes por UDP (Test de 2 minutos)**
Cuando pruebe la cobertura Wi-Fi, necesita tiempo para trasladarse entre habitaciones o colocarse detrás de paredes de concreto. Iniciamos una transmisión UDP por 120 segundos indicando un límite de banda de 200 Megabits (flag -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
En qué fijarse: En el informe final, observe los valores de "Lost/Total Jitter". Si la pérdida de paquetes (Lost) supera el 1-2%, la cobertura en ese punto es deficiente o el Wi-Fi está muy congestionado.

## Resolución de problemas: "¿No conecta?"

**Verifique el aislamiento de clientes (Client Isolation):** En OpenWrt (Red -> Inalámbrico -> Editar red), asegúrese de desactivar la opción "Isolate Clients". Si está activa, el enrutador bloquea la comunicación inalámbrica directa entre dispositivos de la red.

**Cortafuegos del móvil:** En Android, los optimizadores integrados de batería o antivirus del sistema pueden bloquear los intentos de conexión entrantes por el puerto 5201.

**Frecuencia Wi-Fi:** Use siempre la frecuencia de 5 GHz de su enrutador para pruebas confiables. En 2.4 GHz las interferencias de redes vecinas limitarán la tasa a unos 50-70 Mbps.
""".trimIndent()

            "fr" -> """
iperf3 est un outil de ligne de commande légendaire et mondialement reconnu pour mesurer le débit réseau maximal entre deux appareils.

Contrairement aux tests précédents (M-Lab, Cloudflare, Fast.com), iperf3 n'utilise aucun serveur Internet et ne dépend aucunement de votre FAI. Il mesure la vitesse brute du câble ou du Wi-Fi au sein de votre réseau local (LAN), ou sur une liaison dédiée spécifique entre votre routeur et un serveur distant.

## Description détaillée d'iperf3

**Concept :** La référence professionnelle des tests de stress brut pour le matériel réseau.

Alors que M-Lab évalue le comportement général du protocole TCP sur le réseau public global, iperf3 est conçu pour soumettre les cartes réseau, les processeurs de routeurs, la couverture Wi-Fi et les câbles Ethernet à des stress tests intenses.

**Architecture Client-Serveur :** Deux appareils sont indispensables pour réaliser la mesure. L'un est configuré en mode serveur (il écoute le port 5201 et reçoit/émet les données), tandis que l'autre fonctionne en mode client (il génère le trafic et lance la session).

**Génération de trafic :** iperf3 crée un flux artificiel de données extrêmement dense. Il peut envoyer du trafic via le protocole TCP (vérifiant la fiabilité par acquittement des paquets) ou le protocole UDP (mesurant le débit maximal sans acquittement, idéal pour identifier la perte de paquets brute et le jitter).

**Utilité sur le routeur :** Un routeur est un ordinateur spécialisé. En exécutant iperf3 directement sur un routeur OpenWrt, vous pouvez évaluer si son processeur (CPU) parvient à traiter un trafic gigabit ou si le composant Wi-Fi constitue le goulot d'étranglement.

## Instructions pour installer et configurer iperf3 sur OpenWrt

Comme iperf3 est un utilitaire en ligne de commande, nous allons explorer la méthode d'installation la plus robuste via SSH, suivie d'une alternative via l'interface graphique LuCI.

### Étape 1 : Installation du paquet

**Option A : Via le terminal (SSH) — la méthode la plus rapide**
Mettez à jour la liste des paquets de votre routeur OpenWrt via SSH :
`opkg update`
Installez iperf3 :
`opkg install iperf3`

**Option B : Via l'interface graphique LuCI**
1. Accédez à l'administration de votre routeur dans un navigateur web (généralement 192.168.1.1).
2. Naviguez vers Système -> Logiciels (Software).
3. Cliquez sur le bouton "Update lists..." (Mettre à jour les listes).
4. Saisissez "iperf3" dans le champ de filtre.
5. Localisez le paquet iperf3 de la liste et cliquez sur "Install" (Installer).

## Comment exécuter le test

⚠️ **Règle essentielle pour les mobiles :** L'écran du smartphone doit rester allumé pendant toute la durée de la mesure, et l'application doit être active au premier plan. Si l'écran s'éteint ou si l'app est minimisée, les fonctions d'économie d'énergie d'Android/iOS brident instantanément la puce Wi-Fi, faussant totalement la mesure.

### Lancement des commandes depuis la console OpenWrt

Ces exemples s'appuient sur l'adresse IP de votre smartphone (par exemple 192.168.1.150).

**Test 1 : Mesure du débit du routeur vers le smartphone (Download pour smartphone) (Durée étendue)**
Le routeur génère et émet du trafic TCP en continu vers votre téléphone pendant une minute. C'est l'idéal pour vérifier si le processeur du routeur surchauffe en cas de forte charge prolongée.
`iperf3 -c 192.168.1.150 -t 60`
Ce qui se passe : Le routeur sature la bande Wi-Fi avec un flux intense durant 60 secondes. Le rapport détaillé s'affiche directement dans la console du routeur.

**Test 2 : Test inverse prolongé (Émission depuis le mobile vers le routeur)**
Mesure de la vitesse d'envoi du smartphone vers le routeur pendant 45 secondes. Utile pour vérifier si le smartphone bride son débit (throttling) en raison de la chauffe de son module sans fil. On ajoute l'argument -R (Reverse) sur le routeur :
`iperf3 -c 192.168.1.150 -R -t 45`
Ce qui se passe : Le routeur indique au serveur iperf3 du smartphone : "Génère le flux maintenant, je mesure la vitesse à la réception." Parfait pour analyser l'antenne émettrice du mobile.

**Test 3 : Test de stress multiprocessus de 1,5 minute**
Exécute 4 flux de données parallèles pendant 90 secondes. Cela permet d’analyser finement la répartition des ressources CPU d'OpenWrt et la stabilité des tampons. Si votre routeur a un processeur simple cœur limité, un flux unique peut saturer le processeur. Utilisez l'option -P pour diviser la charge :
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Test 4 : Test de perte de paquets en UDP (Test de 2 minutes)**
Lors de l'analyse de votre couverture sans fil, il est nécessaire d'avoir le temps de se déplacer ou de se placer derrière des obstacles. Nous lançons un flux UDP pendant 120 secondes en limitant le débit à 200 Mégabits (argument -b) :
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
Indicateurs importants : Dans le rapport final, examinez la colonne "Lost/Total Jitter". Si la perte de paquets dépasse 1-2%, la couverture est lacunaire ou le spectre Wi-Fi est saturé à cet emplacement.

## Que faire en cas de problème de connexion ?

**Vérifiez l'isolation des clients (Client Isolation) :** Dans vos réglages OpenWrt (Réseau -> Sans fil -> Modifier votre réseau), assurez-vous que l'option "Isolate Clients" est décochée. Si elle est active, le routeur bloque la liaison Wi-Fi interne directe entre appareils.

**Pare-feu du smartphone :** Sur Android, des optimiseurs agressifs d'énergie ou des antivirus peuvent rejeter les flux entrants sur le port 5201.

**Fréquence Wi-Fi :** Utilisez impérativement la fréquence de 5 GHz pour des résultats fiables. En 2,4 GHz, les interférences provoquées par les routeurs environnants limitent généralement le débit entre 50 et 70 Mbps.
""".trimIndent()

            "it" -> """
iperf3 è uno strumento a riga di comando storico e mondialmente riconosciuto per misurare la larghezza di banda massima della rete tra due dispositivi.

A differenza dei test precedenti (M-Lab, Cloudflare, Fast.com), iperf3 non utilizza server Internet esterni e non dipende dal vostro operatore. Misura la velocità reale del cavo o del Wi-Fi all'interno della rete locale (LAN), o su una tratta specifica configurata tra il router e un server remoto.

## Dettagli tecnici di iperf3

**Significato:** Standard di riferimento professionale per lo stress test diretto dell'hardware di rete.

Mentre M-Lab rileva le prestazioni generali del protocollo TCP sulla rete globale, iperf3 è ottimizzato per eseguire severi stress test su schede di rete, CPU di router, copertura Wi-Fi e throughput delle tratte cablate Ethernet.

**Architettura Client-Server:** Sono richiesti due dispositivi. Uno opera in modalità server (resta in ascolto sulla porta standard 5201, ricevendo o trasmettendo i dati), l'altro in modalità client (genera il volume di traffico e avvia la misurazione).

**Generazione di traffico:** iperf3 immette un flusso dati artificiale estremamente saturo. Può operare sia con il protocollo TCP (confermando la stabilità mediante la tracciabilità dei pacchetti di riscontro) sia con il protocollo UDP (verificando la pura portata di picco senza riscontri, ideale per l'analisi del jitter e della reale perdita di pacchetti).

**Utilità sul router:** Un router è un computer dedicato. Eseguendo iperf3 a bordo di OpenWrt, potrete certificare se la CPU di sistema è in grado di veicolare un traffico a velocità gigabit o se il modulo Wi-Fi fa da collo di bottiglia.

## Guida all'installazione ed abilitazione di iperf3 su OpenWrt

Dato che iperf3 è un servizio ad interfaccia riga di comando, analizzeremo il metodo di distribuzione rapido tramite SSH ed anche l'opzione grafica LuCI.

### Passo 1: Installazione del modulo

**Opzione A: Via terminale (SSH) — il sistema più efficiente**
Aggiornate l'archivio dei pacchetti installabili sul vostro OpenWrt via SSH:
`opkg update`
Selezionate l'installazione di iperf3:
`opkg install iperf3`

**Opzione B: Tramite interfaccia grafica LuCI**
1. Accedete al pannello amministrativo del router via browser (solitamente 192.168.1.1).
2. Posizionatevi in Sistema -> Software.
3. Selezionate il pulsante "Update lists..." (Aggiorna pacchetti).
4. Digitate "iperf3" nella barra di ricerca Filtro.
5. Selezionate il pacchetto iperf3 nell'elenco risultante e premete "Install".

## Esecuzione del test

⚠️ **Regola tassativa per dispositivi mobili:** Lo schermo dello smartphone deve restare attivo durante tutto il rilevamento e l'app deve essere visualizzata in primo piano. Nel momento in cui lo schermo si spegne, le logiche di risparmio energetico di Android/iOS limitano drasticamente il Wi-Fi, azzerando l'attendibilità del test.

### Comandi applicabili via terminale OpenWrt

Per questi esempi viene assunta l'IP fittizia dello smartphone (es. 192.168.1.150).

**Test 1: Verifica velocità di invio dal router al telefono (Download per smartphone) (Durata prolungata)**
Il router genererà flussi TCP diretti allo smartphone per ben un minuto. Questa tempistica consente di valutare le temperature o problemi di CPU del router a pieno carico continuativo.
`iperf3 -c 192.168.1.150 -t 60`
Cosa succede: Il router riempie la banda Wi-Fi per 60 secondi di trasmissione dati. L'output comparirà in tempo reale sulla console del router.

**Test 2: Stress test bidirezionale prolungato (Telefono a Router)**
Monitora la capacità di invio dello smartphone al router per 45 secondi. Strumento eccezionale per verificare se il chip Wi-Fi interno del telefono comincia a tagliare la frequenza (throttling) per surriscaldamento. Sul router si usa il parametro -R (Reverse):
`iperf3 -c 192.168.1.150 -R -t 45`
Cosa succede: Il router ordina allo smartphone: "Genera traffico dati, io analizzerò la velocità di ricezione nel mio buffer." Ottimo per testare le antenne del dispositivo.

**Test 3: Stress test multiprocesso da 1.5 minuti**
Gestisce 4 connessioni simultanee parallele per 90 secondi. Ottimizzato per verificare la ripartizione dei core CPU su OpenWrt. Se il vostro router ha prestazioni limitate o un core singolo, un test a singolo flusso potrebbe saturare l'intero processore. Distribuite il traffico con il parametro -P:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Test 4: Verifica stabilità e perdita pacchetti via UDP (Test da 2 minuti)**
Utilizzato per verificare le prestazioni negli spostamenti domestici o stanze separate da muri spessi. Avviamo un flusso UDP per 120 secondi configurando un tetto di 200 Megabit (parametro -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
Cosa controllare: Nel prospetto finale, verificate la colonna "Lost/Total Jitter". Se i pacchetti persi (Lost) superano l'1-2%, la ricezione in quel punto è insufficiente o i canali Wi-Fi circostanti sono saturi.

## Diagnostica: "Mancata connessione"?

**Verificate l'isolamento client (Client Isolation):** Su OpenWrt (Rete -> Wireless -> Modifica rete), accertatevi che la spunta "Isolate Clients" sia disabilitata. Se attiva, il router impedisce la comunicazione Wi-Fi interna tra dispositivi.

**Firewall dello Smartphone:** Su sistemi Android, configurazioni antivirus agguerrite o ottimizzatori di risorse predefiniti possono bloccare il traffico in transito sulla porta 5201.

**Uso frequenza corretta:** Per test professionali, collegate lo smartphone esclusivamente sulla banda a 5 GHz. Sulle frequenze a 2.4 GHz, l'affollamento delle reti vicine limiterà la velocità nell'orbita dei 50-70 Mbps.
""".trimIndent()

            "pt" -> """
O iperf3 é uma ferramenta de consola icónica e mundialmente reconhecida para medir o débito de rede máximo entre dois dispositivos.

Diferente de todos os testes anteriores (M-Lab, Cloudflare, Fast.com), o iperf3 não recorre a servidores de Internet e é totalmente independente da sua operadora. Mede a velocidade real do cabo ou do Wi-Fi na sua rede de área local (LAN), ou num troço dedicado específico entre o seu router e um servidor remoto.

## Descrição detalhada do iperf3

**Conceito:** O referencial profissional para testes de esforço direto de hardware de rede.

Enquanto o M-Lab avalia o comportamento geral do protocolo TCP na rede pública global, o iperf3 foi desenhado para testes de esforço intenso em placas de rede, processadores de routers, cobertura Wi-Fi e rendimento de cabos Ethernet.

**Arquitetura Cliente-Servidor:** São necessários dois aparelhos para efetuar a medição. Um executa no modo servidor (escuta na porta padrão 5201 e recebe/envia dados), enquanto o outro opera no modo cliente (gera o tráfego e inicia a sessão).

**Geração de tráfego:** O iperf3 cria um fluxo de dados artificial altamente denso. Pode transmitir tráfego recorrendo ao protocolo TCP (garantindo estabilidade com validação de receção de pacotes) ou ao protocolo UDP (medindo o rendimento máximo sem confirmações, ideal para detetar perda de pacotes bruta e jitter).

**Utilidade no router:** Um router é um computador especializado. Ao executar o iperf3 diretamente no OpenWrt, pode confirmar se o processador (CPU) do router suporta o débito gigabit ou se o chip Wi-Fi sem fios constitui o obstáculo.

## Instruções de instalação e configuração do iperf3 no OpenWrt

Dado que o iperf3 é um utilitário de consola, focaremos no método de instalação mais fiável via SSH, seguido da alternativa através da interface gráfica de utilizador LuCI.

### Passo 1: Instalação do pacote

**Opção A: Através do terminal (SSH) — o método mais rápido**
Atualize o repositório de pacotes do seu router OpenWrt via SSH:
`opkg update`
Instale o iperf3:
`opkg install iperf3`

**Opção B: Através da interface gráfica LuCI**
1. Abra a administração do seu router num navegador web (geralmente 192.168.1.1).
2. Aceda a Sistema -> Software.
3. Clique no botão "Update lists..." (Atualizar listas de pacotes).
4. Escreva "iperf3" no campo de filtro.
5. Localize o pacote iperf3 na lista e clique em "Install" (Instalar).

## Como executar o teste

⚠️ **Regra obrigatória para dispositivos móveis:** O ecrã do smartphone deve permanecer ligado durante toda a execução do teste e a aplicação deve estar visível em primeiro plano. Quando o ecrã se desliga ou a app é minimizada, as funções de poupança energética do Android/iOS reduzem de imediato a velocidade da placa Wi-Fi, corrompendo a medição.

### Execução de comandos através da consola OpenWrt

Estes exemplos utilizam o endereço IP do seu smartphone (por exemplo 192.168.1.150).

**Teste 1: Verificação da velocidade do router para o telemóvel (Download para smartphone) (Duração alargada)**
O router irá gerar e enviar tráfego TCP em fluxo contínuo para o seu telemóvel durante um minuto. Isto permite auditar se a CPU do router aquece sob carga contínua.
`iperf3 -c 192.168.1.150 -t 60`
O que acontece: O router satura o Wi-Fi com dados durante os 60 segundos de envio. Verá o relatório detalhado no terminal.

**Teste 2: Teste invertido prolongado (Tráfego vindo do telemóvel ao router)**
Mede a taxa de transferência do telemóvel para o router ao longo de 45 segundos. Útil para verificar se o smartphone reduz velocidade (throttling) devido ao calor gerado pelo chip de rede móvel. No router, usa-se o argumento -R (Reverse):
`iperf3 -c 192.168.1.150 -R -t 45`
O que acontece: O router ordena ao servidor do telemóvel: "Começa a enviar tráfego dados agora, medirei a velocidade na receção." Excelente para testar a antena transmissora do telemóvel.

**Teste 3: Teste de esforço multithread durante 1,5 minutos**
Inicia 4 fluxos paralelos de tráfego por 90 segundos. Permite analisar as alocações na CPU de múltiplos núcleos no OpenWrt. Se o seu router tiver uma CPU de núcleo único limitada, um só fluxo pode sobrecarregar o processador. Distribua o tráfego com a opção -P:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Teste 4: Teste de perda de pacotes em UDP (Teste de 2 minutos)**
Útil para testar Wi-Fi ao percorrer divisões ou ao colocar-se atrás de paredes de betão obstaculizadas. Iniciamos o teste UDP por 120 segundos regulando um teto de 200 Megabits (argumento -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
Pontos importantes: No relatório final, analise as colunas "Lost/Total Jitter". Se a perda de pacotes (Lost) exceder 1-2%, o Wi-Fi está muito congestionado ou a cobertura está instável nessa divisão.

## Resolução de problemas: "Não comunica"?

**Verifique o isolamento de clientes (Client Isolation):** No OpenWrt (Rede -> Sem fios -> Editar rede), certifique-se de que a opção "Isolate Clients" está desmarcada. Se estiver ativa, o router bloqueia a ligação Wi-Fi privada interna entre dispositivos.

**Firewall do Telemóvel:** No Android, aplicações otimizadoras de bateria ou antivírus de sistema agressivos podem rejeitar os pacotes de entrada na porta padrão 5201.

**Frequência correta:** Conecte o smartphone exclusivamente à frequência de 5 GHz para obter testes confiáveis. Em 2.4 GHz, interferências derivadas de vizinhos irão limitar a largura de banda à volta dos 50-70 Mbps.
""".trimIndent()

            "da" -> """
iperf3 er et ikonisk, globalt anerkendt kommandolinjeværktøj til måling af den maksimale netværkshastighed mellem to enheder.

I modsætning til alle tidligere tests (M-Lab, Cloudflare, Fast.com) bruger iperf3 ikke eksterne internetservere og er helt uafhængig af din internetudbyder. Det måler den rene kabel- eller Wi-Fi-hastighed i dit lokale netværk (LAN) eller på en dedikeret strækning direkte mellem din router og en ekstern server.

## Detaljeret iperf3-beskrivelse

**Koncept:** Den professionelle standard til stresstest af netværkshårdware.

Mens M-Lab måler den generelle ydeevne for TCP over det globale netværk, er iperf3 bygget til ekstrem stresstest af netværkskort, router-processorer, Wi-Fi-dækning og Ethernet-kabelkapaciteter.

**Klient-Server arkitektur:** Testen kræver altid to enheder. Den ene kører i server-tilstand (lytter på port 5201 og modtager/sender data), mens den anden kører i klient-tilstand (genererer datastrømmen og starter testen).

**Trafikgenerering:** iperf3 opretter en kunstig, ekstremt tæt datastrøm. Den kan sende data via TCP-protokollen (sikrer overførselspålidelighed ved brug af pakkebekræftelse) eller via UDP-protokollen (måler maksimal rå kapacitet uden bekræftelse, ideelt til at afsløre pakketab og jitter).

**Fordelen på routeren:** En router er en specialiseret computer. Ved at køre iperf3 direkte på en OpenWrt-router, kan du finde ud af, om routerens processor (CPU) kan klare gigabit-netværkstrafik, eller om det er Wi-Fi-chippen, der danner flaskehalsen.

## Installationsvejledning på OpenWrt

Da iperf3 er et konsolværktøj, dækker vi den mest pålidelige installationsmetode via SSH samt det grafiske alternativ via LuCI-websiden.

### Trin 1: Pakkeinstallation

**Mulighed A: Via SSH-konsollen — den hurtigste løsning**
Opdater routerens pakkeliste per SSH:
`opkg update`
Installer iperf3:
`opkg install iperf3`

**Mulighed B: Via det grafiske LuCI-webinterface**
1. Åbn routerens administration i en browser (typisk 192.168.1.1).
2. Gå til System -> Software (Programmer).
3. Klik på knappen "Update lists..." (Opdater listen).
4. Indtast "iperf3" i filterfeltet.
5. Find iperf3-pakken på listen og klik på "Install" (Installer).

## Hvordan testen køres

⚠️ **Vigtig regel for mobiltelefoner:** Mobilskærmen skal holdes tændt under hele målingen, og appen skal køre aktivt i forgrunden. Hvis skærmen slukker eller appen minimeres, drosler Android/iOS med det samme Wi-Fi-chippen pga. strømbesparelse, hvilket ødelægger resultatet helt.

### Kommandokørsel via OpenWrt-konsollen

I eksemplerne bruges din smartphones IP-adresse (f.eks. 192.168.1.150).

**Test 1: Måling af sendehastighed fra router til telefon (Download for smartphone) (Forlænget tid)**
Routeren vil generere og sende TCP-data uafbrudt til telefonen i et minut. Dette giver et præcist billede af, om routerens CPU overopheder under vedvarende fuld belastning.
`iperf3 -c 192.168.1.150 -t 60`
Hvad sker der: Routeren fylder WLAN-kanalen med data i 60 sekunder. Rapporten vises direkte i terminalvinduet.

**Test 2: Lang omvendt test (Sende fra mobiltelefon til router)**
Måler overførselshastighed fra telefon til router i 45 sekunder. Godt til at teste, om telefonen drosler ydeevnen (throttling) pga. varme i Wi-Fi-chippen. På routeren tilføjes flaget -R (Reverse):
`iperf3 -c 192.168.1.150 -R -t 45`
Hvad sker der: Routeren giver smartphoneservern besked på: "Send nu data til mig, mens jeg måler min modtagehastighed." Perfekt til at analysere mobilens sendeeffekt.

**Test 3: Multitrådet stresstest i 1,5 minut**
Kører 4 parallelle datastrømme over 90 sekunder. Det lader dig analysereOpenWrt-routerens CPU-resursefordeling og bufferevne. Har din router en svag enkeltkernet processor, kan én tråd låse CPU'en. Fordel belastningen med flaget -P:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Test 4: Måling af pakketab i Wi-Fi via UDP (2-minutters test)**
Når dækningen testes i andre lokaler eller bag tykke betonmure. Vi starter en UDP-strøm i 120 sekunder med en fast grænse på 200 Megabit (flag -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
Hvad du skal se efter: Se på kolonnerne "Lost/Total Jitter" i slutrapporten. Hvis pakketab (Lost) overstiger 1-2%, er dækningen for ringe i det pågældende rum.

## Fejlfinding: "Ingen forbindelse"?

**Tjek klientisolering (Client Isolation):** Sørg for at deaktivere indstillingen "Isolate Clients" i OpenWrt (Netværk -> Trådløst -> Rediger netværk). Hvis den er aktiveret, blokerer routeren for, at trådløse enheder kan kommunikere direkte internt.

**Smartphone firewall:** På Android kan indbyggede strømoptimeringer eller antivirus slå ned på forbindelser modtaget på port 5201.

**WLAN Frekvens:** Brug udelukkende 5-GHz frekvensbåndet til målingen. På 2,4 GHz er signalet ofte forstyrret af nabonetværk, hvilket begrænser farten til 50-70 Mbps.
""".trimIndent()

            "fi" -> """
iperf3 on legendaarinen, maailmanlaajuisesti tunnettu komentorivityökalu verkon maksimikapasiteetin mittaamiseen kahden laitteen välillä.

Toisin kuin muut verkkotestit (M-Lab, Cloudflare, Fast.com), iperf3 ei käytä ulkoisia nettipalevelimia eikä ole riippuvainen operaattoristasi. Se mittaa suoran kaapeli- tai langattoman Wi-Fi-verkon nopeuden lähiverkossasi (LAN), tai routerisi ja etäpalvelimen välisessä suorassa yhteydessä.

## Yksityiskohtainen iperf3-kuvaus

**Konsepti:** Ammattilaistason kuormitusstandardi verkkorautatestaukseen ilman välikäsiä.

Siinä missä M-Lab arvioi yleistä TCP-tiedonsiirtoa julkisessa verkossa, iperf3 on luotu verkkokorttien, reititinprosessoreiden, ja Wi-Fi-kattavuuden äärimmäiseen testaukseen.

**Client-Server-rakenne:** Testi vaatii aina kaksi laitetta. Toinen ajetaan palvelintilassa (kuuntelee porttia 5201 ja vastaanottaa/lähettää dataa), kun taas toinen toimii asiakastilassa (luo tiedonsiirtopaineen ja aloittaa testin).

**Kuormituksen luonti:** iperf3 luo keinotekoisen, erittäin tiiviin tietovirran. Se voi testata verkon TCP-protokollalla (varmistaa siirron vakauden pakettikuittausten avulla) tai UDP-protokollalla (mittaa puhtaan maksiminopeuden ilman kuittauksia, täydellinen pakettihäviöiden ja jitterin tutkimiseen).

**Hyöty reitittimessä:** Reititin on erikoistunut tietokone. Kun ajat iperf3-testin OpenWrtlillä, saat selville, selviytyykö laitteen suoritin (CPU) gigabitin kuorma-ajosta, vai onko Wi-Fi-siru laitteen pullonkaula.

## iperf3 asentaminen ja määrittäminen OpenWrt:ssä

Koska iperf3 on konsolityökalu, käymme läpi luotettavimman SSH-asennustavan sekä graafisen LuCI-käyttöliittymän asennuksen.

### Vaihe 1: Paketin asennus

**Vaihtoehto A: Terminaalin kautta (SSH) — nopein tapa**
Päivitä reitittimen pakettiluettelo SSH-yhteyden avulla:
`opkg update`
Asenna iperf3:
`opkg install iperf3`

**Vaihtoehto B: LuCI-käyttöliittymän kautta**
1. Avaa reitittimen hallintasivu selaimessa (yleensä 192.168.1.1).
2. Mene valikkoon Järjestelmä (System) -> Ohjelmistot (Software).
3. Klikkaa "Update lists..." (Päivitä paketit).
4. Kirjoita "iperf3" hakukenttään.
5. Etsi iperf3-paketti ja klikkaa "Install" (Asenna).

## Testin ajaminen

⚠️ **Kriittinen sääntö mobiililaitteille:** Älypuhelimen näytön on pysyttävä päällä testin aikana ja sovelluksen oltava aktiivisena edustalla. Mikäli näyttö sammuu tai sovellus suljetaan taustalle, Androidin/iOS:n säästöasetukset kuristavat välittömästi Wi-Fi-nopeuden pilaten mittaustuloksen.

### Komennot OpenWrt-konsolista käsin

Esimerkeissä käytetään älypuhelimesi IP-osoitetta (esimerkkiosoitteena 192.168.1.150).

**Testi 1: Tiedonsiirto reitittimestä puhelimeen (Lataus älypuhelimeen) (Pidennetty testiaika)**
Reititin luo ja lähettää TCP-tietovirtaa puhelimeesi yhtäjaksoisesti minuutin ajan. Aika on riittävä havaitsemaan, kuumeneeko reitittimen prosessori pitkäaikaisessa paineessa.
`iperf3 -c 192.168.1.150 -t 60`
Mitä tapahtuu: Reititin varaa WLAN-kaistan kokonaan 60 sekunniksi. Raportti ilmestyy suoraan reitittimen terminaaliin.

**Testi 2: Pitkä käänteistesti (Lähetys puhelimesta reitittimeen)**
Mittaa siirtonopeutta puhelimesta reitittimeen 45 sekunnin ajan. Sopii sen tarkistamiseen, kuristaako puhelin suorituskykyään (throttling) Wi-Fi-sirun lämpenemisen vuoksi. Reitittimessä käytetään -R (Reverse) -toimintoa:
`iperf3 -c 192.168.1.150 -R -t 45`
Mitä tapahtuu: Reititin antaa käskyn puhelimen palvelimelle: "Aloita tiedon lähetys minulle, mittaan vastaanottonopeutta." Parasta älypuhelimen oman lähettimen testaamista.

**Testi 3: Monisäikeinen stressitesti 1,5 minuuttia**
Ajaa 4 rinnakkaista tietovirtaa 90 sekunnin ajan. Sen avulla voidaan tutkia tarkasti OpenWrt-suorittimen resurssienjakoa ja puskurointikykyä. Mikäli reitittimessä on heikko yksikuituinen prosessori, yksi säie voi tukkia sen. Jaa kuormaa -P-parametrilla:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Testi 4: Wifi-pakettihäviöiden testaus UDP-protokollalla (2 minuutin testi)**
Sopii kuuluvuuden mittaamiseen liikuttaessa huoneesta toiseen tai mentäessä kantavien seinien taakse. Aloitamme UDP-testin 120 sekunniksi rajaten siirron 200 Megabittiin (parametri -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
Tarkkailtavat tulokset: Kiinnitä huomiota "Lost/Total Jitter" -sarakkeeseen. Mikäli pakettihäviöt (Lost) ylittävät 1-2%, kuuluvuus on kyseisessä huoneessa huono tai kanavilla on häiriöitä.

## Vianmääritys: "Ei yhteyttä"?

**Varmista asiakaseristys (Client Isolation):** Varmista OpenWrt:ssä (Verkko -> Langaton -> Muokkaa langatonta verkkoa), että asetus "Isolate Clients" on pois päältä. Jos se on päällä, reititin estää langattomien laitteiden keskinäisen yhteydenoton.

**Puhelimen palomuuri:** Android-laitteilla järjestelmän akkuoptimoinnit tai virustorjuntaohjelmat saattavat hylätä porttiin 5201 tulevat yhteydet.

**Taajuusalueen valinta:** Käytä testeihin ainoastaan reitittimen 5 GHz:n taajuutta. 2,4 GHz:n alueella naapuriverkkojen aiheuttamat häiriöt rajaavat nopeuden useimmiten 50-70 Mbps:n tasolle.
""".trimIndent()

            "kk" -> """
iperf3 — бұл екі құрылғы арасындағы желінің максималды өткізу қабілетін өлшеуге арналған әлемге танымал және аңызға айналған консольдік құрал.

Алдыңғы барлық сынақтардан (M-Lab, Cloudflare, Fast.com) айырмашылығы, iperf3 сыртқы интернет серверлерін пайдаланбайды және провайдерге тәуелді емес. Ол жергілікті желідегі (LAN) кабельдің немесе Wi-Fi-дың немесе роутер мен қашықтағы сервер арасындағы тікелей арнаның таза жылдамдығын өлшейді.

## iperf3 туралы егжей-тегжейлі сипаттама

**Суть (Негізі):** Желілік жабдықты тікелей стресс-тестілеудің кәсіби эталоны.

M-Lab жаһандық желідегі TCP хаттамасының жалпы жұмысын бағаласа, iperf3 желілік карталарды, роутер процессорларын, Wi-Fi қамту сапасын және Ethernet кабельдерінің өткізу қабілетін қатаң сынауға арналған.

**Клиент-Сервер архитектурасы:** Сынақ үшін әрқашан екі құрылғы қажет. Біреуі сервер режимінде жұмыс істейді (5201 портын тыңдайды және деректерді қабылдайды/жібереді), ал екіншісі клиент режимінде жұмыс істейді (деректер ағынын жасайды және сынақты бастайды).

**Трафикті генерациялау:** iperf3 жасанды әрі тығыз деректер ағынын жасайды. Деректерді TCP хаттамасы (пакеттердің жеткенін растау арқылы тұрақтылықты тексеру) немесе UDP хаттамасы (растаусыз максималды өткізу қабілетін тексеру, пакеттердің жоғалуы мен джиттерді зерттеу үшін өте қолайлы) арқылы жібере алады.

**Роутердегі пайдасы:** Роутер — бұл мамандандырылған компьютер. iperf3-ті тікелей OpenWrt жүйесінде іске қосу арқылы роутер процессорының (CPU) гигабиттік трафикті өңдеуге күші жететінін немесе Wi-Fi чипінің шектеу салып тұрғанын білуге болады.

## OpenWrt жүйесінде iperf3 орнату және баптау нұсқаулығы

iperf3 консольдік құрал болғандықтан, біз SSH арқылы орнатудың ең сенімді әдісін, сондай-ақ LuCI графикалық интерфейсі арқылы баламалы жолды қарастырамыз.

### 1-қадам: Пакетті орнату

**А нұсқасы: SSH терминалы арқылы — ең жылдам жол**
Роутердің пакеттер тізімін SSH арқылы жаңартыңыз:
`opkg update`
iperf3 пакетін орнатыңыз:
`opkg install iperf3`

**Б нұсқасы: Графикалық LuCI интерфейсі арқылы**
1. Браузерде роутердің басқару панелін ашыңыз (әдетте 192.168.1.1).
2. Жүйе (System) -> Бағдарламалық қамтамасыз ету (Software) мәзіріне өтіңіз.
3. "Update lists..." (Тізімдерді жаңарту) батырмасын басыңыз.
4. "Filter" (Сүзгі) өрісіне "iperf3" деп жазыңыз.
5. Тізімнен iperf3 пакетін тауып, "Install" (Орнату) батырмасын басыңыз.

## Сынақты қалай іске қосу керек

⚠️ **Мобильді құрылғылар үшін маңызды ереже:** Сынақ кезінде смартфонның экраны қосулы, ал қолданба белсенді (экранда ашық) болуы тиіс. Экран сөнгенде немесе қолданба жиырылғанда, Android/iOS жүйелерінің қуатты үнемдеу алгоритмдері Wi-Fi чипінің жылдамдығын бірден шектейді, бұл сынақ нәтижелерін толығымен бұрмалайды.

### OpenWrt консолінен командаларды іске қосу

Мысалдарда смартфонның IP-мекенжайы пайдаланылады (мысалы, 192.168.1.150).

**1-сынақ: Роутерден смартфонға деректерді жіберу жылдамдығын тексеру (Смартфон үшін жүктеу) (Ұзартылған уақыт)**
Роутер бір минут бойы смартфонға үздіксіз TCP трафигін жібереді. Бұл роутер процессорының ұзақ жүктеме кезінде қызып кетпейтінін анықтау үшін жеткілікті.
`iperf3 -c 192.168.1.150 -t 60`
Не болады: Роутер 60 секунд бойы Wi-Fi арнасын деректермен толтырады. Толық есеп тікелей роутер терминалында шығады.

**2-сынақ: Ұзақ реверсивті сынақ (Смартфоннан роутерге жүктеу)**
Смартфонның роутерге деректерді жіберу жылдамдығын 45 секунд бойы өлшейді. Смартфонның Wi-Fi чипі қызған кезде оның жылдамдығын төмендететінін (throttling) тексерудің жақсы әдісі. Роутерде -R (Reverse) жалаушасы қосылады:
`iperf3 -c 192.168.1.150 -R -t 45`
Не болады: Роутер смартфондағы серверге: "Енді сен деректерді жібер, мен қабылдау жылдамдығын өлшеймін" деген команда береді. Бұл смартфон таратқышының сапасын тексерудің тамаша әдісі.

**3-сынақ: 1,5 минуттық көп ағынды стресс-тест**
90 секунд бойы 4 параллельді деректер ағынын орындайды. Бұл OpenWrt процессорының ресурстарды үлестіруін және буфер тұрақтылығын егжей-тегжейлі зерттеуге мүмкіндік береді. Егер роутердің процессоры әлсіз болса, бір ағын оның жұмысын шектеуі мүмкін. Жүктемені бөлу үшін -P жалаушасын пайдаланыңыз:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**4-сынақ: UDP хаттамасы арқылы Wi-Fi пакеттерінің жоғалуын тексеру (2 минуттық сынақ)**
Басқа бөлмелерге ауысқанда немесе қабырғалардың артына өткенде Wi-Fi сапасын сынау үшін қолданылады. Біз UDP сынағын 120 секундқа іске қосып, оған 200 Мегабит шектеу қоямыз (-b жалаушасы):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
Нәтижелерге не нәрсеге назар аудару керек: Соңғы есептегі "Lost/Total Jitter" бағанын қараңыз. Егер пакеттердің жоғалуы (Lost) 1-2%-дан асса, сол жерде Wi-Fi қамтуы әлсіз немесе кедергілер көп.

## Ақаулықтарды жою: "Қосылым жоқ"?

**Клиенттерді оқшаулауды тексеру (Client Isolation):** OpenWrt жүйесінде (Желі -> Сымсыз -> Желіні өңдеу) "Isolate Clients" функциясының өшірулі екеніне көз жеткізіңіз. Егер ол қосулы болса, роутер сымсыз құрылғылардың өзара тікелей байланысуына блок қояды.

**Смартфон файрволы:** Android құрылғыларында жүйелік қуатты үнемдеу функциялары немесе вирусқа қарсы бағдарламалар 5201 портына келетін қосылымдарды қабылдамауы мүмкін.

**Wi-Fi жиілігі:** Сенімді сынақ үшін смартфонды тек роутердің 5 ГГц жиілігіне қосыңыз. 2.4 ГГц жиілігінде көрші желілердің кедергісіне байланысты жылдамдық әдетте 50-70 Мбит/с деңгейінен аспайды.
""".trimIndent()

            "lt" -> """
iperf3 – tai kultinis, visame pasaulyje pripažintas konsolės įrankis, skirtas matuoti maksimalų tinklo pralaidumą tarp dviejų įrenginių.

Skirtingai nuo visų ankstesnių testų (M-Lab, Cloudflare, Fast.com), iperf3 nenaudoja interneto serverių ir nepriklauso nuo jūsų interneto tiekėjo. Jis matuoja grynąjį kabelio arba Wi-Fi greitį jūsų vietiniame tinkle (LAN) arba konkrečiame dedikuotame ruože tarp jūsų maršruto parinktuvo ir nuotolinio serverio.

## Išsamus iperf3 aprašymas

**Esmė:** Profesionalus tinklo įrangos tiesioginio streso testavimo etalonas „sausai“.

Kol M-Lab vertina bendrą TCP protokolo veikimą pasauliniame tinkle, iperf3 yra sukurtas griežtam tinklo kortų, maršruto parinktuvų procesorių, Wi-Fi aprėpties kokybės ir Ethernet kabelių pralaidumo streso testavimui.

**Kliento-Serverio architektūra:** Testui visada reikalingi du įrenginiai. Vienas paleidžiamas serverio režimu (jis tiesiog klauso 5201 prievado ir priima arba siunčia duomenis), o antrasis – kliento režimu (jis generuoja srautą ir inicijuoja testą).

**Srauto generavimas:** iperf3 sukuria dirbtinį, itin tankų duomenų srautą. Jis gali siųsti duomenis per TCP protokolą (tikrinant perdavimo patikimumą su paketų patvirtinimu) arba per UDP protokolą (tikrinant maksimalų pralaidumą be patvirtinimų, kas idealiai tinka paketų praradimui ir jitter tyrimui).

**Kam jis reikalingas maršruto parinktuve:** Maršruto parinktuvas – tai specializuotas kompiuteris. Paleidę iperf3 testą tiesiai OpenWrt sistemoje, galite sužinoti, ar įrenginio procesorius (CPU) susidoroja su gigabitinio srauto pralaidumu, ar bevielis Wi-Fi lustas yra sistemos butelio kaklelis.

## Instrukcija kaip įdiegti ir sukonfigūruoti iperf3 OpenWrt sistemoje

Kadangi iperf3 atliekamas per konsolę, aptarsime patikimiausią diegimo būdą per SSH, taip pat alternatyvų variantą per grafinę LuCI sąsają.

### 1 žingsnis. Paketo diegimas

**A variantas: Per SSH terminalą — greičiausias būdas**
Atnaujinkite maršruto parinktuvo paketų sąrašą per SSH ryšį:
`opkg update`
Įdiekite iperf3 paketą:
`opkg install iperf3`

**B variantas: Per grafinę LuCI sąsają**
1. Naršyklėje atidarykite maršruto parinktuvo valdymo puslapį (dažniausiai 192.168.1.1).
2. Eikite į meniu Sistema (System) -> Programinė įranga (Software).
3. Spustelėkite mygtuką "Update lists..." (Atnaujinti paketų sąrašus).
4. Filtro lauke „Filter“ įrašykite iperf3.
5. Sąraše suraskite iperf3 paketą ir spustelėkite mygtuką „Install“ (Įdiegti).

## Kaip paleisti testą

⚠️ **Svarbi taisyklė mobiliesiems įrenginiams:** Išmaniojo telefono ekranas testo metu turi būti įjungtas, o pati programėlė – aktyvi (atidaryta ekrane). Kai tik ekranas užgęsta arba programėlė sumažinama, agresyvūs Android/iOS energijos taupymo algoritmai akimirksniu sumažina Wi-Fi lusto greitį arba užšaldo fono procesus, kas visiškai iškreipia testo rezultatus.

### Komandų paleidimas iš OpenWrt parinktuvo konsolės

Pavyzdžiuose naudojamas išmaniojo telefono IP adresas (pavyzdžiui, 192.168.1.150).

**1 testas: Siuntimo greičio iš parinktuvo į telefoną tikrinimas (Download išmaniajam telefonui) (Pailgintas laikas)**
Parinktuvas nepertraukiamai generuos ir siųs TCP srautą į jūsų telefoną vieną minutę. Šio laiko pakanka nustatyti, ar parinktuvo procesorius neperkaista esant ilgalaikei apkrovai.
`iperf3 -c 192.168.1.150 -t 60`
Kas vyksta: Parinktuvas 60 sekundžių užpildo WLAN kanalą duomenimis. Ataskaitą pamatysite tiesiai parinktuvo konsolėje.

**2 testas: Ilgas atvirkštinis testas (Išsiuntimas iš telefono į parinktuvą)**
Matuoja išsiuntimo greitį iš telefono į parinktuvą per 45 sekundes. Puikus būdas patikrinti, ar telefonas nepradeda mažinti greičio (throttling) dėl Wi-Fi lusto įkaitimo. Parinktuve pridedamas flagas -R (Reverse):
`iperf3 -c 192.168.1.150 -R -t 45`
Kas vyksta: Parinktuvas duoda komandą telefono serveriui: „Dabar tu generuok srautą, o aš matuosiu priėmimo greitį“. Puikiai tinka patikrinti telefono siųstuvo galingumą.

**3 testas: Daugiagijis streso testas 1,5 minutės**
Vykdo 4 lygiagrečius duomenų srautus 90 sekundžių. Tai leidžia detaliai ištirti OpenWrt procesoriaus išteklių paskirstymą bei buferio stabilumą. Jei jūsų parinktuvas turi silpną vieno branduolio procesorių, viena gija gali jį visiškai užkrauti. Paskirstykite apkrovą su parametru -P:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**4 testas: Paketų praradimo Wi-Fi tinkle tikrinimas per UDP (2 minučių testas)**
Tinka matuoti ryšio kokybę judant per kambarius arba pasislėpus už nešančiųjų sienų. Pradedame UDP testą 120 sekundžių apribodami pralaidumą iki 200 Megabitų (parametras -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
Į ką atkreipti dėmesį rezultatuose: Galutinėje ataskaitoje žiūrėkite į „Lost/Total Jitter“ stulpelį. Jei paketų praradimas (Lost) viršija 1-2%, ryšys tame kambaryje yra prastas arba kanaluose yra trikdžių.

## Trikčių šalinimas: „Nėra ryšio“?

**Patikrinkite klientų izoliaciją (Client Isolation):** OpenWrt nustatymuose (Tinklas -> Belaidis -> Redaguoti tinklą) įsitikinkite, kad išjungta parinktis „Isolate Clients“. Jei ji įjungta, parinktuvas aparatiniu būdu draudžia belaidžiams įrenginiams tiesiogiai bendrauti tarpusavyje.

**Telefono ugniasienė:** Android įrenginiuose agresyvūs akumuliatoriaus optimizatoriai arba integruotos antivirusinės programos gali blokuoti įeinančius ryšius į 5201 prievadą.

**Wi-Fi dažnis:** Kad testas būtų objektyvus, įsitikinkite, kad telefonas prijungtas būtent prie 5 GHz dažnio. 2,4 GHz dažnio diapazone dėl kaimyninių parinktuvų greitis retai pakyla virš 50-70 Mbps.
""".trimIndent()

            "lv" -> """
iperf3 ir kulta, visā pasaulē atzīta komandrindas utilīta maksimālā tīkla caurlaidības mērīšanai starp divām ierīcēm.

Atšķirībā no visiem iepriekšējiem testiem (M-Lab, Cloudflare, Fast.com), iperf3 neizmanto interneta serverus un nav atkarīgs no jūsu pakalpojumu sniedzēja. Tas mēra tīro kabeļa vai Wi-Fi ātrumu jūsu lokālajā tīklā (LAN) vai konkrētā maršrutā starp jūsu maršrutētāju un attālo serveri.

## Detalizēts iperf3 apraksts

**Būtība:** Profesionāls tīkla aparatūras tiešās stresa testēšanas standarts "sausi".

Kamēr M-Lab novērtē TCP protokola vispārējo darbību globālajā tīklā, iperf3 ir paredzēts stingrai stresa testēšanai tīkla kartēm, maršrutētāju procesoriem, Wi-Fi pārklājuma kvalitātei un Ethernet kabeļu caurlaidībai.

**Klienta-Servera arhitektūra:** Testam vienmēr ir nepieciešamas divas ierīces. Viena darbojas servera režīmā (tā vienkārši klausās 5201 portu un saņem/sūta datus), bet otrā – klienta režīmā (tā ģenerē trafiku un sāk testu).

**Trafika ģenerēšana:** iperf3 rada mākslīgu, ļoti blīvu datu plūsmu. Tas var sūtīt datus, izmantojot TCP protokolu (pārbaudot pārraides stabilitāti ar pakotņu apstiprinājumiem) vai UDP protokolu (pārbaudot maksimālo caurlaidību bez apstiprinājumiem, kas ir ideāli piemērots pakotņu zudumu un džitera izpētei).

**Kāpēc tas ir nepieciešams maršrutētājā:** Maršrutētājs ir specializēts dators. Palaidot iperf3 testu tieši OpenWrt sistēmā, varat uzzināt, vai ierīces procesors (CPU) spēj tikt galā ar gigabitu plūsmas caurlaidību, vai arī bezvadu Wi-Fi mikroshēma ir sistēmas vājā vieta.

## Instrukcija iperf3 uzstādīšanai un konfigurēšanai OpenWrt

Tā kā iperf3 tiek veikts caur konsoli, mēs apskatīsim uzticamāko uzstādīšanas veidu caur SSH, kā arī alternatīvo variantu caur grafisko LuCI saskarni.

### 1. solis. Pakotnes uzstādīšana

**A variants: Caur SSH termināli — ātrākais veids**
Atjauniniet maršrutētāja pakotņu sarakstu caur SSH savienojumu:
`opkg update`
Uzstādiet iperf3 pakotni:
`opkg install iperf3`

**B variants: Caur grafisko LuCI saskarni**
1. Pārlūkprogrammā atveriet maršrutētāja vadības lapu (parasti 192.168.1.1).
2. Dodieties uz izvēlni Sistēma (System) -> Programmatūra (Software).
3. Noklikšķiniet uz pogas "Update lists..." (Atjaunināt pakotņu sarakstus).
4. Filtra laukā "Filter" ierakstiet iperf3.
5. Sarakstā atrodiet iperf3 pakotni un noklikšķiniet uz pogas "Install" (Uzstādīt).

## Kā palaist testu

⚠️ **Svarīgs noteikums mobilajām ierīcēm:** Viedtālruņa ekrānam testa laikā ir jābūt ieslēgtam, un pašai lietotnei jābūt aktīvai (atvērtai ekrānā). Tiklīdz ekrāns nodziest vai lietotne tiek minimizēta, agresīvi Android/iOS enerģijas taupīšanas algoritmi uzreiz samazina Wi-Fi mikroshēmas ātrumu vai iesaldē fona procesus, kas pilnībā sabojā testa rezultātus.

### Komandu palaišana no OpenWrt maršrutētāja konsoles

Piemēros tiek izmantota jūsu viedtālruņa IP adrese (piemēram, 192.168.1.150).

**1. tests: Sūtīšanas ātruma pārbaude no maršrutētāja uz viedtālruni (Download viedtālrunim) (Pagarināts laiks)**
Maršrutētājs nepārtraukti ģenerēs un sūtīs TCP plūsmu uz jūsu telefonu vienu minūti. Šis laiks ir pietiekams, lai fiksētu, vai maršrutētāja procesors nepārkarst pie ilgstošas slodzes.
`iperf3 -c 192.168.1.150 -t 60`
Kas notiek: Maršrutētājs 60 sekundes aizpilda WLAN kanālu ar datiem. Atskaiti redzēsiet tieši maršrutētāja konsolē.

**2. tests: Ilgs apgrieztais tests (Augšupielāde no telefona uz maršrutētāju)**
Mēra augšupielādes ātrumu no telefona uz maršrutētāju 45 sekunžu laikā. Lielisks veids, kā pārbaudīt, vai telefons nesāk samazināt ātrumu (throttling) Wi-Fi mikroshēmas uzkaršanas dēļ. Maršrutētājā tiek pievienots karodziņš -R (Reverse):
`iperf3 -c 192.168.1.150 -R -t 45`
Kas notiek: Maršrutētājs dod komandu telefona serverim: "Tagad tu ģenerē plūsmu, bet es mērīšu saņemšanas ātrumu". Lieliski piemērots viedtālruņa raidītāja jaudas pārbaudei.

**3. tests: Multi-pavedienu stresa tests 1,5 minūtes**
Veic 4 paralēlas datu plūsmas 90 sekundes. Tas ļauj detalizēti izpētīt OpenWrt procesora resursu sadali un bufera stabilitāti. Ja jūsu maršrutētājam ir vājš viena kodola procesors, viens pavediens var to pilnībā noslogot. Sadaliet slodzi ar parametru -P:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**4. tests: Wi-Fi pakotņu zudumu pārbaude caur UDP (2 minūšu tests)**
Piemērots pārklājuma kvalitātes mērīšanai, pārvietojoties starp istabām vai nostājoties aiz biezām betona sienām. Sākam UDP testu uz 120 sekundēm, ierobežojot caurlaidību līdz 200 Megabitiem (parametrs -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
Kam pievērst uzmanību rezultātos: Galīgajā atskaitē skatiet "Lost/Total Jitter" kolonnu. Ja pakotņu zudums (Lost) pārsniedz 1-2%, pārklājums tajā istabā ir vājš vai kanālos ir traucējumi.

## Traucējummeklēšana: "Nav savienojuma"?

**Pārbaudiet klientu izolāciju (Client Isolation):** OpenWrt iestatījumos (Tīkls -> Bezvadu -> Labot tīklu) pārliecinieties, ka ir izslēgta opcija "Isolate Clients". Ja tā ir ieslēgta, maršrutētājs aparatūras līmenī aizliedz bezvadu ierīcēm tieši sazināties savā starpā.

**Telefona ugunsmūris:** Android ierīcēs agresīvi akumulatora optimizētāji vai iebūvētās antivīrusu programmas var bloķēt ienākošos savienojumus portā 5201.

**Wi-Fi frekvence:** Lai tests būtu objektīvs, pārliecinieties, ka viedtālrunis ir pievienots tieši 5 GHz tīklam. 2.4 GHz diapazonā kaimiņu tīklu traucējumu dēļ ātrums reti pārsniedz 50-70 Mbps.
""".trimIndent()

            "sv" -> """
iperf3 är ett ikoniskt, globalt erkänt kommandoradsverktyg för att mäta maximal nätverkskapacitet mellan två enheter.

Till skillnad från alla tidigare tester (M-Lab, Cloudflare, Fast.com) använder iperf3 inte externa internetservrar och är helt oberoende av din internetleverantör. Det mäter den rena kabel- eller Wi-Fi-hastigheten i ditt lokala nätverk (LAN) eller på en dedikerad sträcka direkt mellan din router och en fjärrserver.

## Detaljerad iperf3-beskrivning

**Koncept:** Den professionella standarden för stresstest av nätverkshårdvara.

Medan M-Lab utvärderar den allmänna prestandan för TCP över det globala nätverket, är iperf3 byggt för extremt stresstest av nätverkskort, routerprocessorer, Wi-Fi-täckning och Ethernet-kabelkapaciteter.

**Klient-Server-arkitektur:** Testet kräver alltid två enheter. Den ena körs i serverläge (lyssnar på port 5201 och tar emot/sänder data) och den andra i klientläge (genererar dataströmmen och startar testet).

**Trafikgenerering:** iperf3 skapar en artificiell, extremt tät dataström. Den kan skicka data via TCP-protokollet (verifierar överföringssäkerhet med paketbekräftelse) eller via UDP-protokollet (mäter maximal rå kapacitet utan bekräftelse, perfekt för att avslöja paketförlust och jitter).

**Fördelen på routern:** En router är en specialiserad dator. Genom att köra iperf3 direkt på en OpenWrt-router kan du ta reda på om routerns processor (CPU) klarar av gigabit-nätverkstrafik, eller om det är Wi-Fi-chippet som utgör flaskhalsen.

## Installationsguide för iperf3 på OpenWrt

Eftersom iperf3 är ett konsolverktyg täcker vi den mest pålitliga installationsmetoden via SSH samt det grafiska alternativet via LuCI-webbgränssnittet.

### Steg 1: Paketinstallation

**Alternativ A: Via SSH-konsolen — den snabbaste lösningen**
Uppdatera routerns paketlista via SSH:
`opkg update`
Installera iperf3:
`opkg install iperf3`

**Alternativ B: Via det grafiska LuCI-gränssnittet**
1. Öppna routerns administratörssida i en webbläsare (oftast 192.168.1.1).
2. Gå till System -> Software (Programvara).
3. Klicka på knappen "Update lists..." (Uppdatera paketlistor).
4. Skriv "iperf3" i filterfältet.
5. Sök efter paketet iperf3 i listan och klicka på "Install" (Installera).

## Hur testet körs

⚠️ **Viktig regel för mobiltelefoner:** Mobilskärmen måste hållas igång under hela mätningen, och appen måste köras aktivt i förgrunden. Om skärmen släcks eller appen minimeras stryper Android/iOS direkt Wi-Fi-chippet pga. strömsparfunktioner, vilket förstör testresultatet helt.

### Kommandokörning via OpenWrt-konsolen

I exemplen används din smartphones IP-adress (f.ex. 192.168.1.150).

**Test 1: Mätning av sändningshastighet från router till telefon (Download för smartphone) (Förlängd testtid)**
Routern kommer att generera och skicka TCP-data oavbrutet till telefonen i en minut. Detta ger en bra bild av om routerns CPU överhettas under ihållande full belastning.
`iperf3 -c 192.168.1.150 -t 60`
Vad som händer: Routern fyller WLAN-kanalen med data under 60 sekunder. Rapporten visas direkt i terminalfönstret.

**Test 2: Långt omvänt test (Sända från mobiltelefon till router)**
Mäter överföringshastighet från telefonen till routern under 45 sekunder. Utmärkt för att testa om telefonen stryper prestandan (throttling) pga. värme i Wi-Fi-chippet. På routern tillförs flaggan -R (Reverse):
`iperf3 -c 192.168.1.150 -R -t 45`
Vad som händer: Routern meddelar smartphoneservern: "Skicka nu data till mig, medan jag mäter min mottagningshastighet." Perfekt för att analysera mobilens sändningseffekt.

**Test 3: Flertrådat stresstest i 1,5 minuter**
Kör 4 parallella dataströmmar under 90 sekunder. Det låter dig analysera OpenWrt-routerns processorfördelning och buffertstabilitet. Har din router en svag enkelkärnig processor kan en tråd överbelasta den. Fördela belastningen med flaggan -P:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Test 4: Mätning av paketförlust i Wi-Fi via UDP (2-minuters test)**
Användbart för att testa täckningen vid förflyttning mellan rum eller bakom tjocka betongväggar. Vi startar en UDP-ström i 120 sekunder med en fast gräns på 200 Megabit (flagga -b):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
Vad man ska titta efter i resultatet: Se på kolumnerna "Lost/Total Jitter" i slutrapporten. Om paketförlusten (Lost) överstiger 1-2 % är täckningen för dålig i det rummet.

## Felsökning: "Ingen anslutning"?

**Kontrollera klientisolering (Client Isolation):** Se till att inaktivera inställningen "Isolate Clients" i OpenWrt (Nätverk -> Trådlöst -> Redigera nätverk). Om den är aktiverad blockerar routern trådlösa enheter från att kommunicera direkt internt.

**Smartphone brandvägg:** På Android kan inbyggda batterioptimeringar eller antivirus blockera anslutningar som tas emot på port 5201.

**WLAN-frekvens:** Använd uteslutande 5 GHz-frekvensbandet för mätningen. På 2,4 GHz är signalen ofta störd av grannätverk, vilket begränsar farten till 50-70 Mbps.
""".trimIndent()

            else -> """
iperf3 is an iconic, globally recognized command-line tool for measuring the maximum network throughput between two devices.

Unlike previous tests (M-Lab, Cloudflare, Fast.com), iperf3 does not use internet servers and does not depend on your ISP. It measures the net speed of your cable or Wi-Fi inside your local area network, or on a specific dedicated connection between your router and a remote server.

## Detailed iperf3 Description

**Essence:** A professional dry-run testing standard for network hardware.

While M-Lab evaluates how the global TCP internet standard performs, iperf3 is designed for rigorous stress testing of network cards, router CPUs, Wi-Fi coverage quality, and Ethernet cable throughput.

**How it works (Client-Server Architecture):** Two devices are always required for the test. One is run in server mode (it simply listens to the port and receives or sends data), and the other in client mode (it generates traffic and initiates the test).

**Traffic Generation:** iperf3 creates an artificial, highly dense stream of unsigned data. It can run traffic over the TCP protocol (verifying transmission reliability and speed with packet acknowledgments) or UDP protocol (verifying maximum throughput without acknowledgments, which is ideal for detecting real packet loss and jitter).

**Why it is needed on a router:** A router is a specialized computer. Running iperf3 directly on an OpenWrt router lets you find out if its CPU can handle pumping gigabit traffic, or if the wireless Wi-Fi chip is the bottleneck.

## Instructions for installing and configuring iperf3 on OpenWrt

Since iperf3 is a command-line utility, we will cover the most reliable installation method via SSH, as well as an alternative option using the LuCI web interface.

### Step 1. Package Installation

**Option A: Via console (SSH) — the fastest method**
Update the list of available OpenWrt packages by entering in the router's terminal console:
`opkg update`
Install iperf3:
`opkg install iperf3`

**Option B: Via LuCI graphical interface**
1. Open the router's admin panel in a browser (usually 192.168.1.1).
2. Go to System -> Software.
3. Click the "Update lists..." button.
4. Enter iperf3 in the "Filter" field.
5. Find the iperf3 package in the list and click "Install".

## How to run the test

⚠️ **Important rule for mobile devices:** The smartphone screen must remain turned on during the test, and the app itself must be active (open on screen). As soon as the screen goes dark or the app is minimized, aggressive Android/iOS power-saving algorithms instantly throttle the Wi-Fi chip speed or freeze background processes, completely distorting test results.

### Running commands from the OpenWrt router console

The commands use your smartphone's IP address (we will use 192.168.1.150 for this example).

**Test 1: Checking speed sending data from router to smartphone (Download for smartphone) (Increased time)**
The router will generate and send TCP traffic to your smartphone continuously for one minute. This duration is sufficient to determine if the router CPU overheats under prolonged load.
`iperf3 -c 192.168.1.150 -t 60`
What happens: The router fills the wireless channel with data for 60 seconds, sending it to the phone. You will see the report directly in the router's console.

**Test 2: Prolonged reverse test (Downloading from smartphone)**
We test how the smartphone transmits data to the router over 45 seconds. This is an excellent way to check if the smartphone starts throttling speed due to wireless module heating. The -R flag (Reverse) is used on the router:
`iperf3 -c 192.168.1.150 -R -t 45`
What happens: The router commands the server on the smartphone: "Now you generate traffic and send it to me, and I will measure the receive speed." This is the ideal way to check transmitter quality inside the smartphone.

**Test 3: Stress test in multiple threads for one and a half minutes**
Running 4 parallel threads for 90 seconds. Allows detailed monitoring of OpenWrt CPU resource distribution stability and buffer filling uniformity. If your router has a weak single-core CPU, running the test in one thread might hit a CPU performance bottleneck. To distribute the load, run the test with the -P flag:
`iperf3 -c 192.168.1.150 -P 4 -t 90`

**Test 4: Checking Wi-Fi packet loss via UDP (2-minute test)**
When testing Wi-Fi coverage quality, we need time to physically move with the smartphone to another room, stand behind a load-bearing wall, or turn our back to the router. Run a UDP test for 120 seconds with a 200 Megabit bandwidth limit (using the -b flag):
`iperf3 -c 192.168.1.150 -u -b 200M -t 120`
What to look out for in the results: In the final report, pay attention to the Lost/Total Jitter columns. If the loss percentage (Lost) is above 1-2%, it means the Wi-Fi airwaves are heavily congested in that spot, or the router lacks power.

## What to do if "not connecting"?

**Check Client Isolation:** In OpenWrt Wi-Fi settings (Network -> Wireless -> Edit your network), ensure that "Isolate Clients" is unchecked. If it is enabled, the router hardware prevents devices on the Wi-Fi from communicating directly with each other and with the router.

**Smartphone firewall:** On Android devices (especially with custom ROMs), built-in "battery optimizers" or mobile antivirus software may block incoming connections on port 5201.

**Wi-Fi frequency:** For an objective test, make sure the smartphone is connected to the 5 GHz band (if the router is dual-band). On the 2.4 GHz frequency, due to neighboring routers, the speed rarely rises above 50-70 Mbps.
""".trimIndent()
        }
    }

    fun getSplitterTipTitle(lang: String): String {
        return when (lang) {
            "ru" -> "Подсказка по разделителю"
            "uk" -> "Підказка щодо роздільника"
            "be" -> "Падказка па раздзяляльніку"
            "de" -> "Tipp für den Teiler"
            "es" -> "Consejo del divisor"
            "fr" -> "Astuce du séparateur"
            "it" -> "Suggerimento sul divisore"
            "pt" -> "Dica do divisor"
            "da" -> "Tip til opdeleren"
            "fi" -> "Jakajan vihje"
            "kk" -> "Бөлгіш бойынша кеңес"
            "lt" -> "Skirtuko patarimas"
            "lv" -> "Atdalītāja padoms"
            "sv" -> "Tips för delaren"
            else -> "Splitter Tip"
        }
    }

    fun getSplitterTipBody(lang: String): String {
        return when (lang) {
            "ru" -> "Потяните вниз или вверх за полоску между экранами терминалов, чтобы менять их размеры"
            "uk" -> "Потягніть вниз або вгору за смугу між екранами терміналів, щоб змінювати їхні розміри"
            "be" -> "Пацягніце ўніз або ўверх за паласу паміж экранамі тэрміналаў, каб змяняць іх памеры"
            "de" -> "Ziehen Sie die Leiste zwischen den Terminal-Bildschirmen nach unten oder oben, um deren Größe zu ändern"
            "es" -> "Arrastre hacia abajo o hacia arriba la barra entre las pantallas de la terminal para cambiar sus tamaños"
            "fr" -> "Faites glisser vers le bas ou vers le haut la barre entre les écrans du terminal pour modifier leurs tailles"
            "it" -> "Trascina verso il basso o verso l'alto la barra tra le schermate del terminale per modificarne le dimensioni"
            "pt" -> "Arraste para baixo ou para cima a barra entre as telas do terminal para alterar seus tamanhos"
            "da" -> "Træk bjælken mellem terminalskærmene op eller ned for at ændre deres størrelse"
            "fi" -> "Vedä terminaalinäyttöjen välistä palkkia alas- tai ylöspäin muuttaaksesi niiden kokoa"
            "kk" -> "Өлшемдерін өзгерту үшін терминал экрандары арасындағы жолақты жоғары немесе төмен тартыңыз"
            "lt" -> "Vilkite juostą tarp terminalo ekranų žemyn arba aukštyn, kad pakeistumėte jų dydį"
            "lv" -> "Velciet joslu starp termināļa ekrāniem uz leju vai uz augšu, lai mainītu to izmērus"
            "sv" -> "Dra fältet mellan terminalskärmarna uppåt eller nedåt för att ändra deras storlek"
            else -> "Pull the bar between the terminal screens down or up to change their sizes"
        }
    }

    fun getSplitterTipBodyTv(lang: String): String {
        return when (lang) {
            "ru" -> "Потяните влево или вправо за полоску между экранами терминалов, чтобы менять их размеры"
            "uk" -> "Потягніть вліво або вправо за смугу між екранами терміналів, щоб змінювати їхні розміри"
            "be" -> "Пацягніце ўлева або ўправа за паласу паміж экранамі тэрміналаў, каб змяняць іх памеры"
            "de" -> "Ziehen Sie die Leiste zwischen den Terminal-Bildschirmen nach links oder rechts, um deren Größe zu ändern"
            "es" -> "Arrastre hacia la izquierda o hacia la derecha la barra entre las pantallas de la terminal para cambiar sus tamaños"
            "fr" -> "Faites glisser vers la gauche ou vers la droite la barre entre les écrans du terminal pour modifier leurs tailles"
            "it" -> "Trascina verso sinistra o verso destra la barra tra le schermate del terminale per modificarne le dimensioni"
            "pt" -> "Arraste para a esquerda ou para a direita a barra entre as telas do terminal para alterar seus tamanhos"
            "da" -> "Træk bjælken mellem terminalskærmene til venstre eller højre for at ændre deres størrelse"
            "fi" -> "Vedä terminaalinäyttöjen välistä palkkia vasemmalle tai oikealle muuttaaksesi niiden kokoa"
            "kk" -> "Өлшемдерін өзгерту үшін терминал экрандары арасындағы жолақты солға немесе оңға тартыңыз"
            "lt" -> "Vilkite juostą tarp terminalo ekranų į kairę arba į dešinę, kad pakeistumėte jų dydį"
            "lv" -> "Velciet joslu starp termināļa ekrāniem pa kreisi vai pa labi, lai mainītu to izmērus"
            "sv" -> "Dra fältet mellan terminalskärmarna åt vänster eller höger för att ändra deras storlek"
            else -> "Pull the bar between the terminal screens left or right to change their sizes"
        }
    }

    fun getSplitterTipDismiss(lang: String): String {
        return when (lang) {
            "ru" -> "Понятно"
            "uk" -> "Зрозуміло"
            "be" -> "Зразумела"
            "de" -> "Verstanden"
            "es" -> "Entendido"
            "fr" -> "Compris"
            "it" -> "Ho capito"
            "pt" -> "Entendi"
            "da" -> "Forstået"
            "fi" -> "Selvä"
            "kk" -> "Түсінікті"
            "lt" -> "Supratau"
            "lv" -> "Saprasts"
            "sv" -> "Uppfattat"
            else -> "Got it"
        }
    }

    fun getMsgAdded(lang: String): String {
        return when (lang) {
            "ru" -> "Команда добавлена в избранное"
            "uk" -> "Команду додано до вибраного"
            "be" -> "Каманда додана ў абранае"
            "de" -> "Befehl zu Favoriten hinzugefügt"
            "es" -> "Comando agregado a favoritos"
            "fr" -> "Commande ajoutée aux favoris"
            "it" -> "Comando aggiunto ai preferiti"
            "pt" -> "Comando adicionado aos favoritos"
            "da" -> "Kommando føjet til favoritter"
            "fi" -> "Komento lisätty suosikkeihin"
            "kk" -> "Пәрмен таңдаулыларға қосылды"
            "lt" -> "Komanda pridėta prie parankinių"
            "lv" -> "Komanda pievienota izlasei"
            "sv" -> "Kommanod lagt till i favoriter"
            else -> "Command added to favorites"
        }
    }

    fun getMsgExist(lang: String): String {
        return when (lang) {
            "ru" -> "Команда уже в избранном"
            "uk" -> "Команда вже у вибраному"
            "be" -> "Каманда ўжо ў абраным"
            "de" -> "Befehl ist bereits in Favoriten"
            "es" -> "El comando ya está en favoritos"
            "fr" -> "La commande est déjà dans les favoris"
            "it" -> "Il comando è già nei preferiti"
            "pt" -> "O comando já está nos favoritos"
            "da" -> "Kommandoen er allerede i favoritter"
            "fi" -> "Komento on jo suosikeissa"
            "kk" -> "Пәрмен таңдаулыларда бар"
            "lt" -> "Komanda jau yra parankiniuose"
            "lv" -> "Komanda jau ir izlasē"
            "sv" -> "Kommandot finns redan i favoriter"
            else -> "Command is already in favorites"
        }
    }

    fun getMsgEmpty(lang: String): String {
        return when (lang) {
            "ru" -> "Поле ввода пусто"
            "uk" -> "Поле введення порожнє"
            "be" -> "Поле ўводу пуста"
            "de" -> "Eingabefeld ist leer"
            "es" -> "El campo de entrada está vacío"
            "fr" -> "Le champ de saisie est vide"
            "it" -> "Il campo di input è vuoto"
            "pt" -> "O campo de entrada está vazio"
            "da" -> "Indtastningsfeltet er tomt"
            "fi" -> "Syöttökenttä on tyhjä"
            "kk" -> "Енгізу өрісі бос"
            "lt" -> "Įvesties laukas tuščias"
            "lv" -> "Ievades lauks ir tukšs"
            "sv" -> "Inmatningsfältet är tomt"
            else -> "Input field is empty"
        }
    }

    fun getFavoritesTitle(lang: String): String {
        return when (lang) {
            "ru" -> "Избранные команды"
            "uk" -> "Вибрані команди"
            "be" -> "Абраныя каманды"
            "de" -> "Favoriten-Befehle"
            "es" -> "Comandos favoritos"
            "fr" -> "Commandes favorites"
            "it" -> "Comandi preferiti"
            "pt" -> "Comandos favoritos"
            "da" -> "Favoritkommandoer"
            "fi" -> "Suosikkikomennot"
            "kk" -> "Таңдаулы пәрмендер"
            "lt" -> "Parankinės komandos"
            "lv" -> "Izlases komandas"
            "sv" -> "Favoritkommandon"
            else -> "Favorite Commands"
        }
    }

    fun getFavoritesEmpty(lang: String): String {
        return when (lang) {
            "ru" -> "Список избранного пуст"
            "uk" -> "Список вибраного порожній"
            "be" -> "Спіс абранага пусты"
            "de" -> "Favoritenliste ist leer"
            "es" -> "La lista de favoritos está vacía"
            "fr" -> "La liste des favoris est vide"
            "it" -> "L'elenco dei preferiti è vuoto"
            "pt" -> "A lista de favoritos está vazia"
            "da" -> "Favoritlisten er tom"
            "fi" -> "Suosikkiluettelo on tyhjä"
            "kk" -> "Таңдаулылар тізімі бос"
            "lt" -> "Parankinių sąrašas tuščias"
            "lv" -> "Izlases saraksts ir tukšs"
            "sv" -> "Favoritlistan är tom"
            else -> "Favorites list is empty"
        }
    }

    fun getHistoryTitle(lang: String): String {
        return when (lang) {
            "ru" -> "История команд. Выберите команду из списка"
            "uk" -> "Історія команд. Виберіть команду зі списку"
            "be" -> "Гісторыя каманд. Выберыце каманду са спісу"
            "de" -> "Befehlsverlauf. Wählen Sie einen Befehl aus der Liste"
            "es" -> "Historial de comandos. Seleccione un comando de la lista"
            "fr" -> "Historique des commandes. Sélectionnez une commande dans la liste"
            "it" -> "Cronologia comandi. Seleziona un comando dalla lista"
            "pt" -> "Histórico de comandos. Selecione um comando da lista"
            "da" -> "Kommandohistorik. Vælg kommando fra listen"
            "fi" -> "Komentohistoria. Valitse komento listalta"
            "kk" -> "Пәрмендер тарихы. Тізімнен пәрменді таңдаңыз"
            "lt" -> "Komandų istorija. Pasirinkite komandą iš sąrašo"
            "lv" -> "Komandu vēsture. Izvēlieties komandu no saraksta"
            "sv" -> "Kommandohistorik. Välj kommando från listan"
            else -> "Command History. Select command from the list"
        }
    }

    fun getHistoryEmpty(lang: String): String {
        return when (lang) {
            "ru" -> "История команд пуста"
            "uk" -> "Історія команд порожня"
            "be" -> "Гісторыя каманд пустая"
            "de" -> "Befehlsverlauf ist leer"
            "es" -> "El historial de comandos está vacío"
            "fr" -> "L'historique des commandes est vide"
            "it" -> "La cronologia dei comandi è vuota"
            "pt" -> "O histórico de comandos está vazio"
            "da" -> "Kommandohistorikken er tom"
            "fi" -> "Komentohistoria on tyhjä"
            "kk" -> "Пәрмендер тарихы бос"
            "lt" -> "Komandų istorija yra tuščia"
            "lv" -> "Komandu vēsture ir tukša"
            "sv" -> "Kommandohistoriken är tom"
            else -> "Command history is empty"
        }
    }

    fun getAboutAppTitle(lang: String): String {
        return when (lang) {
            "ru" -> "О приложении"
            "uk" -> "Про програму"
            "be" -> "Аб праграме"
            "de" -> "Über die App"
            "es" -> "Acerca de la aplicación"
            "fr" -> "À propos de l'application"
            "it" -> "Informazioni sull'app"
            "pt" -> "Sobre o aplicativo"
            "da" -> "Om appen"
            "fi" -> "Tietoja sovelluksesta"
            "kk" -> "Қолданба туралы"
            "lt" -> "Apie programėlę"
            "lv" -> "Par lietotni"
            "sv" -> "Om appen"
            else -> "About App"
        }
    }

    fun getAboutAppFree(lang: String): String {
        return when (lang) {
            "ru" -> "Бесплатно для использования"
            "uk" -> "Безкоштовно для використання"
            "be" -> "Бясплатна для выкарыстання"
            "de" -> "Kostenlos zu verwenden"
            "es" -> "Uso gratuito"
            "fr" -> "Gratuit à utiliser"
            "it" -> "Gratuito da usare"
            "pt" -> "Uso gratuito"
            "da" -> "Gratis at bruge"
            "fi" -> "Ilmainen käyttää"
            "kk" -> "Пайдалануға тегін"
            "lt" -> "Nemokama naudoti"
            "lv" -> "Bezmaksas lietošana"
            "sv" -> "Gratis att använda"
            else -> "Free to use"
        }
    }

    fun getAboutAppClose(lang: String): String {
        return when (lang) {
            "ru" -> "Закрыть"
            "uk" -> "Закрити"
            "be" -> "Закрыць"
            "de" -> "Schließen"
            "es" -> "Cerrar"
            "fr" -> "Fermer"
            "it" -> "Chiudi"
            "pt" -> "Fechar"
            "da" -> "Luk"
            "fi" -> "Sulje"
            "kk" -> "Жабу"
            "lt" -> "Uždaryti"
            "lv" -> "Aizvērt"
            "sv" -> "Stäng"
            else -> "Close"
        }
    }

    fun getConsoleTipTitle(lang: String): String {
        return when (lang) {
            "ru" -> "Управление"
            "uk" -> "Керування"
            "be" -> "Кіраванне"
            "de" -> "Steuerung"
            "es" -> "Control"
            "fr" -> "Contrôle"
            "it" -> "Controllo"
            "pt" -> "Controle"
            "da" -> "Styring"
            "fi" -> "Hallinta"
            "kk" -> "Басқару"
            "lt" -> "Valdymas"
            "lv" -> "Vadība"
            "sv" -> "Kontroll"
            else -> "Control"
        }
    }

    fun getConsoleTipBodyTv(lang: String): String {
        return when (lang) {
            "ru" -> "Кнопки переключения каналов на пульте управления позволяют листать историю команд. Стрелки влево и вправо на пульте позволяют перемещать курсор по команде."
            "uk" -> "Кнопки перемикання каналів на пульті управління дозволяють гортати історію команд. Стрілки вліво і вправо на пульті дозволяють переміщати курсор по команді."
            "be" -> "Кнопкі пераключэння каналаў на пульце кіравання дазваляюць гартаць гісторыю каманд. Стрэлкі налева і направа на пульце дазваляюць перамяшчаць курсор па камандзе."
            "de" -> "Mit den Kanalwechseltasten auf der Fernbedienung können Sie durch den Befehlsverlauf blättern. Mit den Links- und Rechtspfeilen auf der Fernbedienung können Sie den Cursor im Befehl bewegen."
            "es" -> "Los botones de cambio de canal en el mando a distancia permiten desplazarse por el historial de comandos. Las flechas izquierda y derecha del mando a distancia permiten mover el cursor en el comando."
            "fr" -> "Les boutons de changement de chaîne de la télécommande vous permettent de faire défiler l'historique des commandes. Les flèches gauche et droite de la télécommande vous permettent de déplacer le curseur dans la commande."
            "it" -> "I pulsanti di cambio canale sul telecomando consentono di scorrere la cronologia dei comandi. Le frecce sinistra e destra sul telecomando consentono di spostare il cursore nel comando."
            "pt" -> "Os botões de mudança de canal no controle remoto permitem que você percorra o histórico de comandos. As setas esquerda e direita no controle remoto permitem mover o cursor no comando."
            "da" -> "Kanalvælgerknapperne på fjernbetjeningen giver dig mulighed for at rulle gennem kommandohistorikken. Venstre og højre pil på fjernbetjeningen giver dig mulighed for at flytte markøren i kommandoen."
            "fi" -> "Kaukosäätimen kanavanvaihtopainikkeilla voit selata komentohistoriaa. Kaukosäätimen vasen ja oikea nuoli mahdollistavat kohdistimen siirtämisen komennossa."
            "kk" -> "Қашықтан басқару пультіндегі арналарды ауыстыру түймелері пәрмендер тарихын айналдыруға мүмкіндік береді. Пульттегі сол және оң жақ көрсеткілер курсорды пәрмен бойынша жылжытуға мүмкіндік береді."
            "lt" -> "Nuotolinio valdymo pulto kanalų perjungimo mygtukai leidžia slinkti komandų istoriją. Nuotolinio valdymo pulto rodyklės kairėn ir dešinėn leidžia perkelti žymeklį komandoje."
            "lv" -> "Tālvadības pults kanālu pārslēgšanas pogas ļauj ritināt komandu vēsturi. Tālvadības pults kreisā un labā bultiņa ļauj pārvietot kursoru komandā."
            "sv" -> "Kanalbytarknapparna på fjärrkontrollen låter dig bläddra i kommandohistoriken. Vänster och höger pilar på fjärrkontrollen låter dig flytta markören i kommandot."
            else -> "The channel switching buttons on the remote control allow you to scroll through the command history. The left and right arrows on the remote control allow you to move the cursor in the command."
        }
    }

    fun getConsoleTipBody(lang: String): String {
        return when (lang) {
            "ru" -> "Проведите пальцем вдоль левого или правого края рамки, чтобы выбрать команду из истории команд. Проведите пальцем вдоль верхнего или нижнего края рамки, чтобы перемещать курсор в строке команды."
            "uk" -> "Проведіть пальцем вздовж лівого або правого краю рамки, щоб вибрати команду з історії команд. Проведіть пальцем вздовж верхнього або нижнього краю рамки, щоб переміщати курсор у рядку команди."
            "be" -> "Правядзіце пальцам уздоўж левага або правага краю рамкі, каб выбраць каманду з гісторыі каманд. Правядзіце пальцам уздоўж верхняга або ніжняга краю рамкі, каб перамяшчаць курсор у радку каманды."
            "de" -> "Wischen Sie am linken oder rechten Rand entlang, um einen Befehl aus dem Verlauf auszuwählen. Wischen Sie am oberen oder unteren Rand entlang, um den Cursor in der Befehlszeile zu bewegen."
            "es" -> "Desliza a lo largo del borde izquierdo o derecho para seleccionar un comando del historial. Desliza a lo largo del borde superior o inferior para mover el cursor en la línea de comando."
            "fr" -> "Balayez le long du bord gauche ou droit pour sélectionner une commande dans l'historique. Balayez le long du bord supérieur ou inférieur pour déplacer le curseur dans la ligne de commande."
            "it" -> "Scorri lungo il bordo sinistro o destro per selezionare un comando dalla cronologia. Scorri lungo il bordo superiore o inferiore per spostare il cursore nella riga di comando."
            "pt" -> "Deslize ao longo da borda esquerda ou direita para selecionar um comando do histórico. Deslize ao longo da borda superior ou inferior para mover o cursor na linha de comando."
            "da" -> "Stryg langs venstre eller højre kant for at vælge en kommando fra historikken. Stryg langs øverste eller nederste kant for at flytte markøren i kommandolinjen."
            "fi" -> "Pyyhkäise vasenta tai oikeaa reunaa pitkin valitaksesi komennon historiasta. Pyyhkäise ylä- tai alareunaa pitkin siirtääksesi kohdistinta komentorivillä."
            "kk" -> "Пәрмендер тарихынан пәрменді таңдау үшін жақтаудың сол немесе оң жақ шетімен сырғытыңыз. Пәрмен жолында курсорды жылжыту үшін жақтаудың жоғарғы немесе төменгі шетімен сырғытыңыз."
            "lt" -> "Braukite kairiuoju arba dešiniuoju kraštu, kad pasirinktumėte komandą iš istorijos. Braukite viršutiniu arba apatiniu kraštu, kad perkeltumėte žymeklį komandų eilutėje."
            "lv" -> "Pārvelciet gar kreiso vai labo malu, lai izvēlētos komandu no vēstures. Pārvelciet gar augšējo vai apakšējo malu, lai pārvietotu kursoru komandrindā."
            "sv" -> "Svep längs vänster eller höger kant för att välja ett kommando från historiken. Svep längs övre eller nedre kanten för att flytta markören på kommandoraden."
            else -> "Swipe along the left or right edge to select a command from the history. Swipe along the top or bottom edge to move the cursor in the command line."
        }
    }
}
