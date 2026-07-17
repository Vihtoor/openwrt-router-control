import re

file_path = "app/src/main/java/com/example/IperfLocalizations.kt"

with open(file_path, "r") as f:
    content = f.read()

target = """    fun getConsoleTipBody(lang: String): String {"""

replacement = """    fun getConsoleTipBodyTv(lang: String): String {
        return when (lang) {
            "ru" -> "Кнопки переключения каналов на пульте управления позволяют листать историю команд. Стрелки влево и вправо на клавиатуре позволяют перемещать курсор по команде."
            "uk" -> "Кнопки перемикання каналів на пульті управління дозволяють гортати історію команд. Стрілки вліво і вправо на клавіатурі дозволяють переміщати курсор по команді."
            "be" -> "Кнопкі пераключэння каналаў на пульце кіравання дазваляюць гартаць гісторыю каманд. Стрэлкі налева і направа на клавіятуры дазваляюць перамяшчаць курсор па камандзе."
            "de" -> "Mit den Kanalwechseltasten auf der Fernbedienung können Sie durch den Befehlsverlauf blättern. Mit den Links- und Rechtspfeilen auf der Tastatur können Sie den Cursor im Befehl bewegen."
            "es" -> "Los botones de cambio de canal en el mando a distancia permiten desplazarse por el historial de comandos. Las flechas izquierda y derecha del teclado permiten mover el cursor en el comando."
            "fr" -> "Les boutons de changement de chaîne de la télécommande vous permettent de faire défiler l'historique des commandes. Les flèches gauche et droite du clavier vous permettent de déplacer le curseur dans la commande."
            "it" -> "I pulsanti di cambio canale sul telecomando consentono di scorrere la cronologia dei comandi. Le frecce sinistra e destra della tastiera consentono di spostare il cursore nel comando."
            "pt" -> "Os botões de mudança de canal no controle remoto permitem que você percorra o histórico de comandos. As setas esquerda e direita no teclado permitem mover o cursor no comando."
            "da" -> "Kanalvælgerknapperne på fjernbetjeningen giver dig mulighed for at rulle gennem kommandohistorikken. Venstre og højre pil på tastaturet giver dig mulighed for at flytte markøren i kommandoen."
            "fi" -> "Kaukosäätimen kanavanvaihtopainikkeilla voit selata komentohistoriaa. Näppäimistön vasen ja oikea nuoli mahdollistavat kohdistimen siirtämisen komennossa."
            "kk" -> "Қашықтан басқару пультіндегі арналарды ауыстыру түймелері пәрмендер тарихын айналдыруға мүмкіндік береді. Пернетақтадағы сол және оң жақ көрсеткілер курсорды пәрмен бойынша жылжытуға мүмкіндік береді."
            "lt" -> "Nuotolinio valdymo pulto kanalų perjungimo mygtukai leidžia slinkti komandų istoriją. Klaviatūros rodyklės kairėn ir dešinėn leidžia perkelti žymeklį komandoje."
            "lv" -> "Tālvadības pults kanālu pārslēgšanas pogas ļauj ritināt komandu vēsturi. Tastatūras kreisā un labā bultiņa ļauj pārvietot kursoru komandā."
            "sv" -> "Kanalbytarknapparna på fjärrkontrollen låter dig bläddra i kommandohistoriken. Vänster och höger pilar på tangentbordet låter dig flytta markören i kommandot."
            else -> "The channel switching buttons on the remote control allow you to scroll through the command history. The left and right arrows on the keyboard allow you to move the cursor in the command."
        }
    }

    fun getConsoleTipBody(lang: String): String {"""

content = content.replace(target, replacement)

with open(file_path, "w") as f:
    f.write(content)
print("Done")
