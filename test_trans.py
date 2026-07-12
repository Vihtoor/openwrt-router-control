import urllib.request
import urllib.parse
import json

def translate(text, target_lang):
    url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=ru&tl=" + target_lang + "&dt=t&q=" + urllib.parse.quote(text)
    try:
        req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
        with urllib.request.urlopen(req) as response:
            data = json.loads(response.read().decode())
            return "".join([x[0] for x in data[0]])
    except Exception as e:
        return str(e)

print(translate("Привет мир", "en"))
