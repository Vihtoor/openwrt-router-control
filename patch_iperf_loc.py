import re

file_path = "app/src/main/java/com/example/IperfLocalizations.kt"

with open(file_path, "r") as f:
    content = f.read()

new_functions = """    fun getConsoleTipTitle(lang: String): String {
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
}"""

content = content.replace("}", new_functions)

with open(file_path, "w") as f:
    f.write(content)
print("Done")
