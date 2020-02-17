
import java.io.*;
import java.util.*;

/**
 *
 * Eemil Laine eemil.laine@tuni.fi
 * 
 * Ohjelma, joka tuottaa kahdesta numeorilla täytetyistä tiedostoista.
 * 3 tiedostoa, joihin on ajettu joukko-operaatiot: UNION, LEIKKAUS JA XOR.
 * Ohjelma lukee tiedostot nimeltään: setA.txt ja setB.txt.
 * Tiedostoista luetaan rivin ensimmäinen numero ja jos tuon numeron arvo on alle 999999999.
 * Jos tiedostolle syöttää kirjaimia hyväksyy se kaikki ennen kirjainta syötetyt numerot.
 *
 */
public class Test {  
    
    // Luodaan scanner luokka.
    private final static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args){
        
        // Tulostetaan tarvittavat ohjeet ohjelman käyttämiseen.
        System.out.println("Ohjelma suorittaa kahden tiedoston numeroarvoista " +
            "union, leikkaus ja erotus joukko-operaatiot.");
        System.out.println("Ohjelma lukee tiedostoista jokaiselta riviltä ensimmäisen numeroarvon.");
        System.out.println("Suurin ohjelmalle syötettävä numero voi olla 999999999.");
        System.out.println("Ohjelma lukee tiedostot nimeltään: setA.txt ja setB.txt");
        System.out.println("Tiedostojen tulee olla muodossa:");
        System.out.println("1");
        System.out.println("55");
        System.out.println("3");
        
        // Luodaan olio HashTable luokkaan
        HashTable table = new HashTable(2);
        
        // Tarvittavat attribuutit tiedostojen lukemiseen.
        int arvo;
        int rivi = 0;
        int tiedosto = 1;
        
        /**
         * Luetaan tiedostot.
         * Tiedostojen niminä käytetään setA.txt ja setB.txt.
         * Ensin luetaan setA.txt tiedosto, sitten setB.txt tiedosto.
         */
        // Luetaan setA.txt tiedosto.
		try {
            File myObj1 = new File("setA.txt");
            Scanner myReader1 = new Scanner(myObj1);
            while (myReader1.hasNextLine()) {
                arvo = myReader1.nextInt();
                table.put(arvo, rivi, tiedosto);
                rivi++;
            }
            myReader1.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
        } catch (Exception e) {
            System.out.println("An error occurred.");
        }

        // Nollataan rivilaskuri ja lisätään tiedostolaskuriin yksi.
        rivi = 0;
        tiedosto++;

        // Luetaan setB.txt tiedosto.
        try {
            File myObj2 = new File("setB.txt");
            Scanner myReader2 = new Scanner(myObj2);
            while (myReader2.hasNextLine()) {
                arvo = myReader2.nextInt();
                table.put(arvo, rivi, tiedosto);
                rivi++;
            }
            myReader2.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
        
        // Tulostetaan taulukoko.
        System.out.println("Taulun koko  -> " + table.size());
        
        /**
         * Annetaan mahdollisuus poistaa arvoja HashTablesta.
         * Poistettava arvo on aina ensimmäinen löydetty arvo.
         * Myöhemmin HashTableen lisätyt arvot poistuvat viimeisinä.
         */
        System.out.println("Nyt on mahdollisuus poistaa arvoja.");
        String viesti = "Haluatko poistaa arvon? y | n";
        Boolean jatketaan = true;
        while (jatketaan != false) {
            System.out.println(viesti);
            char k = sc.next().charAt(0);
            if (k == 'y') {
                System.out.println("Syötä poistettava arvo: ");
                while (sc.hasNext()) {
                    if (sc.hasNextInt()) {
                        int p = sc.nextInt();
                        boolean poistettu = table.remove(p);
                        if (poistettu) {
                            System.out.println("Poisto onnistui");
                        } else {
                            System.out.println("Arvoa ei löytynt listasta");
                        }
                        viesti = "Haluatko poistaa uuden arvon? y | n";
                        break;
                    } else {
                        System.out.println("Virhe! Väärän muotoinen arvo. Syötä numeroarvo.");
                        sc.next();
                    }
                }
            } else if (k == 'n') {
                System.out.println("Selvä, edetään tiedostojen kirjoittamiseen.");
                break;
            } else {
                jatketaan = true;
            }
        }
        
       /**
         * Ajetaan tarvittavat joukko-operaatiot.
         */
        LinkedList<Integer> union = table.union();
        LinkedList<Integer> leikkaus = table.leikkaus();
        LinkedList<Integer> XOR = table.XOR();
        
       /**
         * Tulostetaan joukko-operaatioiden avulla saadut listat käyttäjälle.
         * Listoja ajetaan HashTable luokan läpi, jotta rivi ja tiedoston määrä voidaan tulostaa,
         * toiselle sarakkeelle.
         */
        try {
            BufferedWriter or = new BufferedWriter(new FileWriter("or.txt"));
            BufferedWriter and = new BufferedWriter(new FileWriter("and.txt"));
            BufferedWriter xor = new BufferedWriter(new FileWriter("xor.txt"));
            
            // Tulostetaan or.txt tiedosto union joukko-operaation perusteella.
            or.write("Arvo:      Määrä tiedostoissa:");
            or.newLine();
            for (int i = 0; i < union.size(); i++) {
                or.write(table.getMaara(union.get(i)));
                or.newLine();
            }
            or.close();
            
            // Tulostetaan and.txt tiedosto leikkaus joukko-operaation perusteella.
            and.write("Arvo:      Ensimmäisessä syötetiedostossa rivillä:");
            and.newLine();
            for (int i = 0; i < leikkaus.size(); i++) {
                and.write(table.getR(leikkaus.get(i)));
                and.newLine();
            }
            and.close();
            
            // Tulostetaan xor.txt tiedosto erotus joukko-operaation perusteella.
            xor.write("Arvo:      Tiedostossa:");
            xor.newLine();
            for (int i = 0; i < XOR.size(); i++) {
                xor.write(table.getT(XOR.get(i)));
                xor.newLine();
            }
            xor.close();
            
            // Kerrotaan, että ohjelma on onnistuneesti kirjoittanut tiedsotot joukko-operaatioiden mukaan.
            System.out.println("Tiedostot or.txt, and.txt ja xor.txt kirjoitettu.");
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e); // Tarkistetaan tuliko ongelmia kirjoituksen yhteydessä.
        }
    }
}
