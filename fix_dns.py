import re

with open('app/src/main/java/com/example/ui/RouterViewModel.kt', 'r') as f:
    content = f.read()

content = content.replace('val PublicDnsProviders = listOf(', 'fun getPublicDnsProviders(context: android.content.Context): List<DnsProvider> = listOf(')
content = content.replace('"Один из самых быстрых публичных DNS-провайдеров в мире с акцентом на конфиденциальность."', 'context.getString(com.example.R.string.dns_desc_cloudflare)')
content = content.replace('"Базовый (быстрый доступ и приватность)"', 'context.getString(com.example.R.string.dns_cloudflare_default)')
content = content.replace('"Защита от вредоносного ПО (Malware Block)"', 'context.getString(com.example.R.string.dns_cloudflare_malware)')
content = content.replace('"Семейный (блокировка вредоносного ПО + контента для взрослых + SafeSearch)"', 'context.getString(com.example.R.string.dns_cloudflare_family)')

content = content.replace('"Один из старейших и надежнейших сервисов, принадлежащий компании Cisco."', 'context.getString(com.example.R.string.dns_desc_opendns)')
content = content.replace('"Базовый (Home / без фильтрации)"', 'context.getString(com.example.R.string.dns_opendns_home)')
content = content.replace('"Семейный (Family Shield / предустановленная фильтрация)"', 'context.getString(com.example.R.string.dns_opendns_family)')
content = content.replace('"Подходит для роутеров, фильтрует контент для взрослых без необходимости создания учетной записи."', 'context.getString(com.example.R.string.dns_opendns_family_desc)')

content = content.replace('"Специализированный сервис, ориентированный исключительно на блокировку нежелательного контента."', 'context.getString(com.example.R.string.dns_desc_cleanbrowsing)')
content = content.replace('"Фильтр безопасности (Security Filter)"', 'context.getString(com.example.R.string.dns_cleanbrowsing_security)')
content = content.replace('"Взрослый фильтр (Adult Filter)"', 'context.getString(com.example.R.string.dns_cleanbrowsing_adult)')
content = content.replace('"Семейный фильтр (Family Filter)"', 'context.getString(com.example.R.string.dns_cleanbrowsing_family)')
content = content.replace('"Блокирует прокси, VPN-туннели, порнографию и жестко включает безопасный поиск в Google/YouTube."', 'context.getString(com.example.R.string.dns_cleanbrowsing_family_desc)')

content = content.replace('"Лучший выбор для тех, кто хочет избавиться от рекламы на уровне сетевых запросов."', 'context.getString(com.example.R.string.dns_desc_adguard)')
content = content.replace('"Базовый (блокировка рекламы, трекеров и фишинга)"', 'context.getString(com.example.R.string.dns_adguard_default)')
content = content.replace('"Семейный (базовый + контент для взрослых + SafeSearch)"', 'context.getString(com.example.R.string.dns_adguard_family)')
content = content.replace('"Нефильтрующий (просто быстрый и надежный сервис)"', 'context.getString(com.example.R.string.dns_adguard_unfiltered)')

content = content.replace('"Современный и очень гибкий DNS от создателей Windscribe."', 'context.getString(com.example.R.string.dns_desc_controld)')
content = content.replace('"Нефильтрующий (чистый интернет / Uncensored)"', 'context.getString(com.example.R.string.dns_controld_uncensored)')
content = content.replace('"Блокировка рекламы (Ads & Trackers)"', 'context.getString(com.example.R.string.dns_controld_ads)')
content = content.replace('"Семейный (все: реклама + соцсети + контент для взрослых)"', 'context.getString(com.example.R.string.dns_controld_family)')

content = content.replace('"Сервис с акцентом на кибербезопасность."', 'context.getString(com.example.R.string.dns_desc_quad9)')
content = content.replace('"Стандартный (по умолчанию / блокировка фишинга и малвари)"', 'context.getString(com.example.R.string.dns_quad9_default)')
content = content.replace('"Стандартный + ECS-поддержка (с поддержкой ECS)"', 'context.getString(com.example.R.string.dns_quad9_ecs)')
content = content.replace('"Передает малую часть вашего IP-адреса (EDNS Client Subnet) для более точной маршрутизации до CDN-серверов."', 'context.getString(com.example.R.string.dns_quad9_ecs_desc)')

content = content.replace('"Один из самых популярных публичных сервисов."', 'context.getString(com.example.R.string.dns_desc_google)')
content = content.replace('"Стандартный"', 'context.getString(com.example.R.string.dns_google_default)')

with open('app/src/main/java/com/example/ui/RouterViewModel.kt', 'w') as f:
    f.write(content)
