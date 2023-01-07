//Käytettävät kirjastot
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//Ohjelmassa olevien funktioiden esittelyt
void lueRoskat(void);
int lopetus();
void saldonTarkastus(double tilinsaldo);
double liittymanLataus(double tilisaldo);
void kuitintulostus(void);
int valikkovalinta();
double tililtaOtto(double tilisaldo);
void lueMerkkijono(char merkkijono[], int pituus);
int lueKokonaisluku(void);
void lopeta();

/*Pääfunktio/pääohjelma, jossa kaikki pankkiautomaatin toiminnot suoritetaan ja edellä esiteltyjä funktioita kutsutaan erilaisia parametreillä.
Pääohjelmassa käyttäjältä kysytään aluksi tilinumeroa, jota vastaava teksti/tilitiedosto avataan ja sieltä luetaan tilin salasana/pinkoodi.
Käyttäjällä on 3 yritystä saada salasana oikein. Jos käyttäjä syöttää  salasanan 3 kertaa väärin ohjelma ilmoittaa, että tili on jäädytetty ja ohjelma sulketuu.
Jos käyttäjä antaa oikean salasanan ohjelma ilmoittaa, että salasana on saatu oikein, toivottaa tervetulleeksi ja avaa toimintovalikon käyttäjälle.
Pääohjelman toiminta pyörii käyttäjän syötteiden ja funktioihin rakennettujen ohjausrakenteiden perusteella. */
int main()
{
//Esitellään pääohjelman muuttujat ja alustetaan tarvittavat muuttujat
    FILE *pFile;
    char tiliNumero[256];
    char pinKoodi[256];
    char checkPin[256];
    int yritykset;
    double saldo;
    int jatko;
    jatko = 1;
    saldo = 987.65;
    yritykset = 3;

/*
Aluksi kysytään käyttäjältä tilinumero. Tilinumeroa vastaava .acc tiedosto avataan, josta luetaan pinkoodi checkPin-muuttujaan
Sitten käyttäjän antamia pinkoodeja verrataan siihen strcmp-metodilla, ja sen palauttaman arvon perusteella kysyntää jatketaan tai käyttäjä
päästetään sisään pankkitililleen.
      */

        printf("\nAnna tilinumerosi:  > ");
        fgets(tiliNumero, 256, stdin);

        if (tiliNumero[strlen(tiliNumero)-1] == '\n')
            tiliNumero[strlen(tiliNumero)-1] = '\0';
        else
            lueRoskat();

        strcat(tiliNumero, ".acc");

        do {

            if ((pFile = fopen(tiliNumero, "r")) != NULL){
            printf("\nAnna pinkoodi > ");

            fgets(pinKoodi, 256, stdin);

            if (pinKoodi[strlen(pinKoodi) - 1] == '\n')
                pinKoodi[strlen(pinKoodi) - 1] = '\0';
            else
                lueRoskat();


         fgets(checkPin, 256, pFile);

            if (checkPin[strlen(checkPin) - 1] == '\n'){
               checkPin[strlen(checkPin) - 1] = '\0';
            }

            if (checkPin[strlen(checkPin) - 1] == '\r'){
               checkPin[strlen(checkPin) - 1] = '\0';
            }

            if (strcmp(pinKoodi, checkPin) == 0){
               printf("\nOikea salasana!");
               break;
            } else {
               printf("Vaara pinkoodi, yrita uudelleen! ");
               yritykset--;
            }

      } else {
        printf("\nTilia ei loytynyt. Tarkastakaa tilinumeronne ja yrittakaa sen jalkeen uudelleen.");
        exit(0);
      }
}while(yritykset!=0);

if(yritykset == 0){
    printf("\nTilisi on suljettu! Pinkoodi liian monta kertaa vaarin. ");
    exit(0);
} else{
    printf("\nTervetuloa!");
}

//While-silmukka toimintojen tekemiseen. Muuttuja jatko-muuttujan arvo muuttuu, silmukan toisto lopetetaan.
    while (jatko == 1){


    //Switch-case rakenne eri toiminnoille.
    switch (valikkovalinta())
    {
    case 1:
            saldo = liittymanLataus(saldo);
            break;
    case 2:
            saldo = tililtaOtto(saldo);
            break;
    case 3:
            saldonTarkastus(saldo);
            break;
    case 4:
            kuitintulostus();
            break;
    case 5:
        lopeta();
    default:
        printf("Tämä valinta ei kelpaa!");
    }
    //Tässä tarkastetaan. jos käyttäjä haluaa jatkaa automaatin käyttöä, eli muutetaan jatko-muuttujan arvoa tai sitten pidetään se samana.
    jatko = lopetus();

}
printf("Kiitos käynnistä \nTervetuloa uudelleen.");
return 0;
}

//Tyhjentää lukupuskurin
void lueRoskat(void){
   while( fgetc(stdin) != '\n');
}

/*Funktiossa käyttäjältä kysytään haluaako hän jatkaa automaatin käyttöä ja käyttäjän syötteen perusteella palautetaan arvo,
joka sijoitetaan jatko-muuttujaan. Palautetun arvon perusteella automaatin käyttö jatkuu tai loppuu.
Jos käyttäjä valitsee arvon 1, while-silmukan toistoehto täyttyy eli, automaatin käyttöä jatketaan tai
jos käyttäjä valitsee arvon 2 while-silmukan toistoehto ei täyty ja automaatti sulkeutuu. */
int lopetus(){
    int valinta;
    printf("\nHaluatko jatkaa automaatin käyttöä? Valitse 1 = Kyllä ja 2 = Ei \n >");
    valinta = lueKokonaisluku();
    if(valinta == 1){
        return valinta;
    }
    else if(valinta == 2){
        return valinta;
    }
    else{
        while(valinta != 1 || valinta != 2){
            printf("\nVäärä syöte!");
            printf("\nHaluatko jatkaa automaatin käyttöä? Valitse 1 = Kyllä ja 2 = Ei \n >");
            valinta = lueKokonaisluku();
            if(valinta == 1){
                return valinta;
            }
           else if(valinta == 2){
                return valinta;
        }
            else{
                continue;
            }
        }
}
return 1;
}

//Funktio ottaa argumenttinaan pääohjelman tilinsaldo muuttujan ja tulostaa käyttäjälle tilin saldon. Funktio ei palauta mitään, vaan muuttaa parametrinaan saatua arvoa suoraan.
void saldonTarkastus(double tilinsaldo){
    printf("\nTilin saldo: %.2f", tilinsaldo);
}

/*Funktiossa käyttäjä pystyy lataamaan puhelinliittymäänsä saldoa.
Käyttäjältä kysytään puhelinoperaattoria, puhelinnumeroa ja käyttäjä valitsee liittymään ladattavan summan.
Lopuksi käyttäjän antaman tiedot vielä tulostetaan näytölle ja ladattava saldo miinustetaan käyttäjän pankkitilin saldosta.
*/
double liittymanLataus(double tilisaldo){
int liittymalataus;
int liittymavalinta;
char puhelinnumero[11];
liittymavalinta = 0;
liittymalataus = 0;
printf("Liittymän lataus");
printf("\nValitse palvelu:\n1. Saunalahti \n2. DNA\n3. Go Mobile\n >");
liittymavalinta = lueKokonaisluku();
if(liittymavalinta == 1){
    printf("Syötä Saunalahti puhelinnumero (esim. 041987...)\n ja paina Enter \n >");
    lueMerkkijono(puhelinnumero, 11);
    printf("\nLiittymän lataus \nValitse haluamasi summa.");
    printf("\n 10 euroa \n 15 euroa \n 20 euroa \n 25 euroa \n 30 euroa \n 50 euroa \n 100 euroa \n >");
    liittymalataus = lueKokonaisluku();
    if(liittymalataus % 5 != 0){
        printf("Väärä valinta!");
        return tilisaldo;
        }
    else if(liittymalataus > tilisaldo){
        printf("Tililläsi ei ole tarpeeksi katetta!");
        return tilisaldo;

}
    else if(liittymalataus < 10 || liittymalataus > 100){
                printf("Talletuksessa ilmeni virhe!");
                return tilisaldo;
            }
    else{
        printf("Maksun tiedot: \nPuhelinnumero %s \nLadattava summa %d + \nOtto tililtä %d -", puhelinnumero, liittymalataus, liittymalataus);
        tilisaldo = tilisaldo - liittymalataus;
        return tilisaldo;
}
}
else if(liittymavalinta == 2){
        printf("Syötä DNA puhelinnumero (esim. 041987...)\n ja paina Enter \n >");
        lueMerkkijono(puhelinnumero, 11);
        printf("\nLiittymän lataus \nValitse haluamasi summa.");
        printf("\n 10 euroa \n 15 euroa \n 20 euroa \n 25 euroa \n 30 euroa \n 50 euroa \n 100 euroa \n >");
        liittymalataus = lueKokonaisluku();
        if(liittymalataus % 5 != 0){
            printf("Väärä valinta!");
            return tilisaldo;
            }
        else if(liittymalataus > tilisaldo){
                printf("Tililläsi ei ole tarpeeksi katetta!");
                return tilisaldo;

            }
        else if(liittymalataus < 10 || liittymalataus > 100){
                printf("Talletuksessa ilmeni virhe!");
                return tilisaldo;

            }
        else{
            printf("Maksun tiedot: \nPuhelinnumero %s \nLadattava summa %d + \nOtto tililtä %d -", puhelinnumero, liittymalataus, liittymalataus);
            tilisaldo = tilisaldo - liittymalataus;
            return tilisaldo;

        }
        }
        else if(liittymavalinta == 3){
            printf("Syötä Go Mobile puhelinnumero (esim. 041987...)\n ja paina Enter \n >");
            lueMerkkijono(puhelinnumero, 11);
            printf("\nLiittymän lataus \nValitse haluamasi summa.");
            printf("\n 10 euroa \n 15 euroa \n 20 euroa \n 25 euroa \n 30 euroa \n 50 euroa \n 100 euroa \n >");
            liittymalataus = lueKokonaisluku();
            if(liittymalataus % 5 != 0){
                printf("\nEt voi ladata liittymaa tällä määrällä!");
                return tilisaldo;

            }
            else if(liittymalataus > tilisaldo){
                printf("Tililläsi ei ole tarpeeksi katetta!");
                return tilisaldo;

            }
            else if(liittymalataus < 10 || liittymalataus > 100){
                printf("Talletuksessa ilmeni virhe!");
                return tilisaldo;

            }
            else{
            printf("Maksun tiedot: \nPuhelinnumero %s \nLadattava summa %d + \nOtto tililtä %d -", puhelinnumero, liittymalataus, liittymalataus);
            tilisaldo = tilisaldo - liittymalataus;
            return tilisaldo;
        }
    }
else{
    printf("Virheellinen valinta!");
    return tilisaldo;
    }
return 0;
}

/*Funktio simuloi pankkiautomaatin kuitintulostus-ominaisuutta, jossa käyttäjä pystyisi tulostamaan tilinsä tiedot (saldon, tapahtumat) kuitille tai automaatin näytölle.
Käyttäjä voi valita tässäkin tapauksessa haluaako tiedot näytölle vai kuitille, vaikka tietoja ei oikeasti tulosteta. */
void kuitintulostus(void){
int tapahtumavalinta;
tapahtumavalinta = 0;
printf("Haluatko tiedot: \n 1. Näytölle\n 2. Kuitille \n >");
tapahtumavalinta = lueKokonaisluku();
if(tapahtumavalinta == 1){
    printf("\nTietoja ladataan...");
    printf("\nTilin tapahtumat: ");
}
else if (tapahtumavalinta == 2){
    printf("Kuitti tulostuu.");
}
else{
    printf("Väärä valinta!");
}
}

//Funktiossa käyttäjä valitsee toiminnon minkä haluaa automaatilla suorittaa. Käyttäjän valinnan perusteella palautetaan arvo, jonka pääohjelman switch-case rakenne saa lauseen arvokseen
int valikkovalinta(){
int toimintovalinta;
toimintovalinta = 0;
printf("\n\nValitse toiminto:\n 1. Liittymän lataus \n 2. Otto\n 3. Saldon tarkastus\n 4. Tapahtumat\n 5. STOP \n > ");
toimintovalinta = lueKokonaisluku();
return toimintovalinta;
}

/*Funktio toimii automaatin ottotoimintona, jossa käyttäjä voi nostaa 20 ja 50 euron seteleitä.
Funktio antaa nostettavan summan mahdollisimman isoina seteleinä. Maksiminosto on 1000 euroa.
Funktio saa argumenttina pääohjelman tilisaldon, josta juuri tehty nosto miinustetaan.
Ota huomioon, että voit nostaa vain sallittuja summia automaatista. Ohjelma ilmoittaa sinulle,
jos olet tekemässä sellaista nostoa, joka ei ole sallittu.
*/
double tililtaOtto(double tilisaldo){
int viiskymppiset;
int tempViiskymppiset;
int kakskymppiset;
int nosto;
int jaannos;
nosto = 0;
viiskymppiset = 0;
kakskymppiset = 0;
tempViiskymppiset = 0;
jaannos = 0;

printf("\nAutomaatista saa vain 20 ja 50 euron seteleita. Miniminosto on 20 euroa ja maksiminosto 1000 euroa.");
printf("\nVoit nostaa 20 euroa, 40 euroa ja tasta kymmenen euron valein 1000 euroon saakka.");
printf("\nNostettava summa > ");
nosto = lueKokonaisluku();

if((nosto < 20) || (nosto > 20 && nosto < 40)){
    printf("Et voi nostaa tallaista summaa tasta automaatista. ");
    return tilisaldo;
}
else if((nosto % 2 != 0) &&(nosto % 10 != 0)){
    printf("Et voi nostaa tallaista summaa tasta automaatista. ");
    return tilisaldo;
}
else if(nosto > 1000){
    printf("Et voi nostaa tallaista summaa. Maksiminosto on 1000 euroa.");
    return tilisaldo;
}
else if(nosto > tilisaldo){
    printf("Tilisi saldo ei riita tahan nostoon.");
    return tilisaldo;
}
else{
tilisaldo = tilisaldo - nosto;
jaannos = nosto % 50;
if(jaannos == 0){
    viiskymppiset = nosto/50;
    kakskymppiset = 0;
}
else if(jaannos != 0){
    tempViiskymppiset = nosto - jaannos;
    viiskymppiset = tempViiskymppiset/50;
    if(jaannos % 20 == 0){
        kakskymppiset = jaannos/20;
}
    else if(jaannos % 20 != 0){
        viiskymppiset--;
        kakskymppiset = (jaannos+50)/20;
    }
}
printf("\nSetelit tulossa, odota hetki...");
printf("\nViidenkymmenen euron setelit: %d", viiskymppiset);
printf("\nKahdenkymmenen euron setelit: %d", kakskymppiset);
saldonTarkastus(tilisaldo);
return tilisaldo;
}
}

//Kurssilla annettujen apufunktioiden mukana saatu funktio, joka lukee käyttäjältä merkkijonon. Argumentteina funktio saa char-arrayn ja sen pituuden, joiden puitteissa merkkijono luetaan.
void lueMerkkijono(char merkkijono[], int pituus){

   fgets( merkkijono, pituus, stdin );

   if( merkkijono[ strlen(merkkijono)-1 ] == '\n')
      merkkijono[ strlen(merkkijono)-1 ] = '\0';
   else
      lueRoskat();

}

//Kurssilla annettujen apufunktioiden mukana saatu funktio, joka lukee käyttäjältä kokonaislukuja standard inputilla ja palauttaa luetun arvon.
int lueKokonaisluku(void){

   int luku;
   char mki;
   int status;

   while (((status = scanf("%d%c", &luku, &mki)) == 0)  || (2 == status && mki != '\n')){
      lueRoskat();
      printf("Et syottanyt kokonaislukua > ");
   }

   return luku;
}

//Lopetusfunktio, joka kutsuttaessa lopettaa ohjelman suorituksen välittömästi.
void lopeta(){
    exit(0);
}