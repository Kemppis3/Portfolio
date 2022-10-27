"""
@Author: Joni Kemppainen, Oulun yliopisto
Miinantallaaja lopputyö Oulun yliopiston Ohjelmoinnin alkeet -kurssille.

Haravasto -kirjaston on tehnyt Mika Oja Oulun yliopistosta. Haravasto -library made by Mika Oja from University of Oulu

Itse miinaharavaa opettelin täällä: https://miinaharava.com/
Pelin säännöt tulvatäyttön tekoa varten: https://fi.wikipedia.org/wiki/Miinaharava_(peli) (8 naapuriruudun tarkastus)
"""

import haravasto as har
import time as t
import random as r

"""
Luodaaan globaalit sanakirjat, jotka sisältävät tietoja kustakin pelatusta pelistä.
Numerolista-lista on luotu hiiri_käsittelijä -funktiota varten.
"""

pelikentantiedot = {
    "aluskentta": [],
    "peitekentta":[],
    "miinoituskentta":[],
    "kentan_leveys": 0,
    "kentan_korkeus": 0,
    "miinojenmaara": 0
}

pelitiedot = {
    "peliaika": 0,
    "pelintulos": " ",
    "siirrot": 0
}

numerolista = ["0","1","2","3","4","5","6","7","8"]

def pyyda_syotteet():
    """
    Pyydetään käyttäjältä syötteet kentän luomiseen ja miinojen määrän asetukseen.
    Huomasin, että suuremmilla kentillä pelistä tulee melko takkuinen, joten
    rajoitin kentän koon omasta mielestäni sopivaan kokoväliin, jotta pelin pelaaminen on mieluista.
    """
    
    while True:
        try:
            pelikentantiedot["kentan_korkeus"] = int(input("Anna pelikentän korkeus: "))
            if pelikentantiedot["kentan_korkeus"] <= 15 and pelikentantiedot["kentan_korkeus"]>= 5:
                break
            else:
                print("Kentän maksimikooksi on asetettu 15x15 ja minimikooksi 5x5 pelin mielekkyyden takia. Pysytään näissä rajoissa.")
        except ValueError:
            print("Annoit vääränlaisen syötteen pelikentän korkeudeksi!")
    
    while True:
        try:
            pelikentantiedot["kentan_leveys"] = int(input("Anna pelikentän leveys: "))
            if pelikentantiedot["kentan_leveys"] <= 15 and pelikentantiedot["kentan_leveys"] >= 5:
                break
            else:
                print("Kentän maksimikooksi on asetettu 15x15 ja minimikooksi 5x5 pelin mielekkyyden takia. Pysytään näissä rajoissa.")
        except ValueError:
            print("Annoit vääränlaisen syötteen pelikentän leveydeksi!")
    
    while True:
        try:
            pelikentantiedot["miinojenmaara"] = int(input("Anna asetettavien miinojen määrä. Miinojen minimääräksi on asetettu 5: "))
            if pelikentantiedot["miinojenmaara"] < 5:
                print("Tällä määrällä miinoja ei saada kunnollista peliä aikaan!")
            elif pelikentantiedot["kentan_korkeus"] * pelikentantiedot["kentan_leveys"] < pelikentantiedot["miinojenmaara"]:
                print("Kentälle ei mahdu noin montaa miinaa.")
            elif pelikentantiedot["kentan_korkeus"] * pelikentantiedot["kentan_leveys"] == pelikentantiedot["miinojenmaara"]:
                print("Yritätkö tahallaan hävitä?")
            elif pelikentantiedot["kentan_korkeus"] * pelikentantiedot["kentan_leveys"] > pelikentantiedot["miinojenmaara"]:
                break    
        except ValueError:
            print("Annoit vääränlaisen syötteen miinojen määräksi!")

def luo_pelikentat(): 
    """""
    Luodaan kaksi erillistä tyhjää kenttää, eli kaksi 2-ulotteista listaa. Aluskenttä, jossa miinat ovat ja toinen kenttä peittämään se ruuduilla.
    2-ulotteisen kentän luominen onnistuu kahdella for-silmukalla, jossa ensin luodaan listan sisään lista (rivi), jolle lisätään haluttu määrä sarakkeita. Tätä käydään läpi niin kauan,
    kunnens haluttu määrä rivejä on luotu. Samalla myös miinoitetaan aluskenttä.
    2-ulotteisen listan luomiseen ja niiden käsittelyyn katsoin apua täältä https://www.geeksforgeeks.org/python-using-2d-arrays-lists-the-right-way/.
    """
    for rivi in range(pelikentantiedot["kentan_korkeus"]):
        pelikentantiedot["aluskentta"].append([])
        for sarake in range(pelikentantiedot["kentan_leveys"]):
            pelikentantiedot["aluskentta"][rivi].append(" ")

    for rivi in range(pelikentantiedot["kentan_korkeus"]):
        pelikentantiedot["peitekentta"].append([])
        for sarake in range(pelikentantiedot["kentan_leveys"]):
            pelikentantiedot["peitekentta"][rivi].append(" ")

    miinoita(pelikentantiedot["miinojenmaara"])

def laske_ymparoivat_miinat(miinakentta, x, y): 
    """
    Luodaan paikallinen muuttuja ymparoivatmiinat, jonka jälkeen tutkitaan ruudun ympäriltä miinojen määrä.
    2-ulotteisten listojen läpikäyntiin löysin täältä apua: https://stackoverflow.com/questions/16548668/iterating-over-a-2-dimensional-python-list.
    """
    ymparoivatmiinat = 0
    tutkittavaruutu = [(x,y)]
    kentanleveys = len(miinakentta[0]) - 1
    kentankorkeus = len(miinakentta) - 1
    i, j = tutkittavaruutu.pop()
    for rivi in range(j-1, j+2):
        for sarake in range(i-1, i+2):
            try:
                if rivi >= 0 and rivi <= kentankorkeus and sarake >= 0 and sarake <= kentanleveys:
                    if pelikentantiedot["aluskentta"][rivi][sarake] == "x":
                        ymparoivatmiinat += 1
            except IndexError:
                pass
    return ymparoivatmiinat 

def numeroi_ymparoivat_miinat():
    """
Käydään kentän jokainen ruutu läpi, siten, että jos ruudussa ei ole miinaa sen ympärillä olevat miinat lasketaan.
Laske_ymparoivat_miinat-funktion palautus muutetaan string muotoon, jotta tämä funktio sijoittaa numeron string-muodossa kenttään ja tällöin
hiiri_käsittelijä-funktio osaa tulkita sitä.
    """
    for rivinnumero, rivi in enumerate(pelikentantiedot["aluskentta"]):
        for sarakkeennumero, tarkastettavaruutu in enumerate(pelikentantiedot["aluskentta"][rivinnumero]):
            if tarkastettavaruutu != "x":
                pelikentantiedot["aluskentta"][rivinnumero][sarakkeennumero] = str(laske_ymparoivat_miinat(pelikentantiedot["aluskentta"], sarakkeennumero, rivinnumero)) 
            elif tarkastettavaruutu == "x":
                pass

def miinoita(miinat): 
    """
    Luodaan aluksi miinoituskenttä, joka on samankokoinen kuin pelikenttä ja tälle kentälle luodaan koordinaattipisteitä (x,y).
    Sitten näitä koordinaattipisteitä valitaan pythonhin random.choice-funktiolla asetettujen miinojen verran ja asetetaan miinat ("x":t) aluskentälle samoihin kohtiin.
    Jotta miinoja ei tulisi vahingossakaan samoihin kohtiin kentässä, poistetaan äskettäin asetetun miinan koordinaattipiste miinoituskentästä, ennen kuin uutta
    pistettä lähdetään arpomaan.
    """
    for x in range(len(pelikentantiedot["aluskentta"][0])):
        for y in range(len(pelikentantiedot["aluskentta"])):
            pelikentantiedot["miinoituskentta"].append((x,y))
    
    for m in range(miinat):
        try:
            i, j = r.choice(pelikentantiedot["miinoituskentta"])
            pelikentantiedot["aluskentta"][j][i] = "x"
            pelikentantiedot["miinoituskentta"].remove((i,j))
        except IndexError:
            pass

def tulvataytto(peitekentta, x, y): 
    """
    Tulvatutkimus aloitetaan käyttäjän hiiren vasemmalla näppäimellä klikkaamasta kohdasta, jossa tarkistetaan ruutu ja ruudun ympäröimä alue ja jos 
    algoritmin ehdot toteutuvat lisätään ympärillä olevien ruutujen koordinaatit listaan, joten nekin tarkastetaan, jolloin aiheutuu ketjureaktio ruutujen poistossa, eli tulvatäyttö.
    Tulvatäyttö loppuu kun kohdataan numeroitu ruutu.
    Tulvatäyttöalgoritmin tekoon sain hieman apua opiskelukavereilta, koska tämän tekemisessä oli vähän ongelmia.
    """
    tulvaruudut = [(x,y)]
    leveys = len(peitekentta[0]) - 1
    korkeus = len(peitekentta) - 1
    while tulvaruudut != []:
        i, j = tulvaruudut.pop()
        leveyssuunta = range(i - 1,  i + 2)
        korkeussuunta = range(j - 1, j + 2)
        for k in korkeussuunta:
            for l in leveyssuunta:
                try:
                    if l >= 0 and l <= leveys and k >= 0 and k <= korkeus and pelikentantiedot["peitekentta"][k][l] == " " and pelikentantiedot["aluskentta"][k][l] != "x":
                        pelikentantiedot["peitekentta"][k][l] = pelikentantiedot["aluskentta"][k][l]
                        if pelikentantiedot["peitekentta"][k][l] == " " or pelikentantiedot["peitekentta"][k][l] == "0":
                            tulvaruudut.append((l, k))
                except IndexError:
                    pass
                
def piirra_pelikentta(): 
    """
    Haravaston aseta_piirto_kasittelija -funktiolle annettava käsittelijäfunktio. Piirtää pelikentän ja kentälle ruudut.
    Pelikentän piirtäminen onnistuu samalla tavalla kuin kentän luominen, mutta käytetään Pythonin enumerate -funktiota apuna, jotta saadaan käytyä
    jokainen kentän rivi läpi ja saadaan samalla tarvittavat avaimet haravaston lisaa_piirrettava_ruutu -funktiolle.
    Koska hiiri_kasittelija -funktiossa peitekentän ja aluskentän välille luodaan suhde, tarvitsee tässä kohdassa käydä läpi vain peitekenttä.
    Haravaston dokumentaatiossa sanotaan, että yhden ruudun oletuskoko on 40x40 pikseliä, joten skaalataan ruudut siihen kokoon.
    """
    har.tyhjaa_ikkuna()
    har.piirra_tausta()
    har.aloita_ruutujen_piirto()
    for y, rivi in enumerate(pelikentantiedot["peitekentta"]):
        for x, avain in enumerate(rivi):
            har.lisaa_piirrettava_ruutu(avain, x*40, y*40)
    har.piirra_ruudut()

def hiiri_kasittelija(x, y, painike, muokkausnapit): 
    """ Asetetaan haravaston hiiri_vasen ja hiiri_oikea näppäimet toimintoihiin ja samalla tehdään yhteys alus- ja peitekentän välille.
    Tässä myös katsotaan onko pelaaja astunut miinaan ja jos näin käy niin kutsutaan astuit_miinaan (häviö) -funktiota
    """
    
    if painike == har.HIIRI_VASEN:
        pelitiedot["siirrot"] += 1
        if pelikentantiedot["aluskentta"][y//40][x//40] in numerolista and pelikentantiedot["peitekentta"][y//40][x//40] != "f" and pelikentantiedot["peitekentta"][y//40][x//40] == " ":
            tulvataytto(pelikentantiedot["peitekentta"], x//40, y//40)
        elif pelikentantiedot["aluskentta"][y//40][x//40] == "x" and pelikentantiedot["peitekentta"][y//40][x//40] == "f":
            pass
        elif pelikentantiedot["aluskentta"][y//40][x//40] == "x":
            pelikentantiedot["peitekentta"][y//40][x//40] == "x"
            astuit_miinaan()
        else:
            pelikentantiedot["peitekentta"][y//40][x//40] == pelikentantiedot["aluskentta"][y//40][x//40]
    elif painike == har.HIIRI_OIKEA:
        pelitiedot["siirrot"] += 1
        if pelikentantiedot["peitekentta"][y//40][x//40] == " ":
            pelikentantiedot["peitekentta"][y//40][x//40] = "f"
        elif pelikentantiedot["peitekentta"][y//40][x//40] == "f":
            pelikentantiedot["peitekentta"][y//40][x//40] = " "
        else:
            pass

def laske_avaamattomat_ruudut(): 
    """
    Voitontarkastuksen kannalta tärkeä funktio. Funktiossa tarkastetaan ruutujen laatu, eli onko ruutu tyhjä tai onko se merkattu lipulla.
    Voitit_pelin -funktiossa tämän funktion palauttamaa arvoa verrataan asetettujen miinojen määrään ja jos arvot täsmäävät, on pelaaja voittanut pelin.
    """
    avaamattomat_ruudut = 0
    for y in range(len(pelikentantiedot["peitekentta"])):
        for x in range(len(pelikentantiedot["peitekentta"][0])):
            try:
                if pelikentantiedot["peitekentta"][y][x] == " " or pelikentantiedot["peitekentta"][y][x] == "f":
                    avaamattomat_ruudut += 1
            except IndexError:
                pass
    return avaamattomat_ruudut

def ruudun_paivitys(peliaika): #
    """Ruudunpäivitys -funktio, annetaan haravaston toistu_käsittelijä -funktiolla käsittelijäfunktioksi. Funktio päivittää peliaikaa ja tarkastaa jatkuvasti
    onko pelaaja voittanut pelin.
    """
    pelitiedot["peliaika"] += peliaika
    voitit_pelin()

def pelin_aloitus(): 
    """ Pelin aloitus -funktio, jossa kutsutaan kaikkia tarvittavia funktioita, jotta peli voidaan käynnistää, kun pelaaja päättää aloittaa uuden pelin.
    Skaalataan luotavan ikkunan koko siten, että pelikentälle piirrettävät ruudut mahtuvat siihen.
    """
    pyyda_syotteet()
    har.lataa_kuvat("spritet")
    har.luo_ikkuna(pelikentantiedot["kentan_leveys"] * 40, pelikentantiedot["kentan_korkeus"] * 40)
    luo_pelikentat()
    numeroi_ymparoivat_miinat()
    har.aseta_piirto_kasittelija(piirra_pelikentta)
    har.aseta_hiiri_kasittelija(hiiri_kasittelija)
    har.aseta_toistuva_kasittelija(ruudun_paivitys, 1/60)
    har.aloita()

def alusta_tiedot(): #
    """
    Tässä funktiossa alustetaan kaikki pelitiedot, joita ollaan käytetty edellisessä pelissä. Funktiota kutsutaan aina kun käyttäjä häviää tai voittaa pelin.
    """
    pelikentantiedot["aluskentta"] = []
    pelikentantiedot["peitekentta"] = []
    pelikentantiedot["miinoituskentta"] = []
    pelikentantiedot["kentan_korkeus"] = 0
    pelikentantiedot["kentan_leveys"] = 0
    pelikentantiedot["miinojenmaara"] = 0
    pelitiedot["peliaika"] = 0
    pelitiedot["siirrot"] = 0
    pelitiedot["pelintulos"] = " "

def voitit_pelin():  
    """
    Voitontarkastus -funktio. Voitettaessa kirjataan pelin tulos voitoksi, kutsutaan pelin_tietojen_tallennus -funktiota, haravaston lopeta -funktiota ja 
    alustetaan pelitiedot seuraavaa pelikertaa varten.
    """
    if laske_avaamattomat_ruudut() == pelikentantiedot["miinojenmaara"]:
        pelitiedot["pelintulos"] = "Voitto"
        pelin_tietojen_tallennus()
        print("\nOnneksi olkoon, voitit pelin!")
        har.lopeta()
        alusta_tiedot()

def astuit_miinaan(): 
    """
    Häviöntarkastus -funktio. Sama kuin voittofunktiossa, mutta kirjataan pelin tulokseksi vain häviö.
    Funktion kutsumisehto on määritetlty käsittele_hiiri -funktiossa tapauksessa, jossa funktiota kutsutaan silloin, kun 
    painetaan hiiren vasenta nappia sellaisen ruudun kohdalla, joka sisältää miinan.
    """
    pelitiedot["pelintulos"] = "Häviö"
    pelin_tietojen_tallennus()
    print("\nAstuit miinaan! Peli päättyi.")
    har.lopeta()
    alusta_tiedot()

def pelin_tietojen_tallennus(): 
    """
    Funktiossa tallennetaan kaikki pelin tiedot tekstitiedostoon, josta pelaaja voi sitten myöhemmin tarkastella edellisten pelien tuloksia.
    Valitaan with open() -funktiolle toiseksi argumentiksi a+, jotta uusien pelien tiedot kirjataan tiedoston loppuun ja jos tiedostoa ei ole vielä olemassa, 
    se luodaan. Peliaikatiedot on määritelty tallennettavaksi muodossa: Viikonpäivä, päivä.kuukausi.vuosi tunnit:minuutit:sekunnit.
    Pythonin time -kirjaston käyttöön katsoin apua täältä: https://www.geeksforgeeks.org/time-strftime-function-in-python/.
    """
    try:
        with open("pelientiedot.txt", "a+") as pelientiedot:
            paivamaara = (t.strftime("%A, %d.%m.%Y %H:%M:%S", t.localtime()))
            aika = round((pelitiedot["peliaika"])/60,2)
            pelintulos = pelitiedot["pelintulos"]
            siirrot = pelitiedot["siirrot"]
            korkeus = pelikentantiedot["kentan_korkeus"]
            leveys = pelikentantiedot["kentan_leveys"]
            miinat = pelikentantiedot["miinojenmaara"]
            pelientiedot.write(f"\n Peli pelattiin: {paivamaara} \n Pelin kesto oli: {aika} minuuttia \n Pelin tulos oli: {pelintulos} {siirrot} siirrolla \n Kentän koko oli {korkeus} x {leveys} \n Ja miinoja oli kentällä {miinat} \n")
    except IOError:
        print("\nPelitietojen tallennus epäonnistui!")

def avaa_pelitiedot(): 
    """
    Funktiolla avataan pelin_tietojen_tallenus -funktiossa määritetty tekstitiedosto, joka sisältää edellisten pelien pelitiedot.
    """
    try:
        with open("pelientiedot.txt", "r") as pelitiedot:
            print(pelitiedot.read())
    except FileNotFoundError:
        print("\nPelitietoja ei löytynyt! Pelitietotiedosto luodaan sen jälkeen, kun olet pelannut ensimmäisen pelisi.")

def paavalikko(): 
    """
    Alkuvalikko-funktio, joka pyörii toistuvasti loopissa, kunnes pelaaja päättää poistua pelistä.
    """
    while True:
        pelaajanvalinta = input("\nTervetuloa pelaamaan miinantallaajaa! \nUusi peli = u \nKatsele edellisten pelien tietoja = t \nLopeta peli = l \n>").lower()
        if pelaajanvalinta == "u":
            pelin_aloitus()
        elif pelaajanvalinta == "t":
            avaa_pelitiedot()
        elif pelaajanvalinta == "l":
            break
        else:
            print("\n Väärä valinta!")

if __name__ == "__main__":
    paavalikko()