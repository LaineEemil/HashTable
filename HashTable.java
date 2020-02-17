
import java.util.*;

/**
 *
 * Eemil Laine eemil.laine@tuni.fi
 * HashTable olioluokka.
 *
 */
public class HashTable {

    /**
     * Luodaan linkedlist, joka tallentaa arvot hash tableen arvon perusteella.
     * Jokaiselle nodelle annetaan metadatana rivin ja tiedoston arvo.
     */
    private static class ListNode {
        int rivi;
        int tiedosto;
        int arvo;

        ListNode next; // Osoittaa mahdollisen seuraavan noden.
        // Null kertoo listan lopun.
    }

    private ListNode[] table; // Hash table, joka on taulukko linked listoja.

    private int count; // Arvojen määrä taulussa.


    /**
     * Luodaan hash table. Vakiona annetaan kooksi 64, jos kokoa ei ole määrätty.
     */
    public HashTable() {
        table = new ListNode[64];
    }


    /**
     * Luodaan hash table, koko määrätty main luokassa.
     * Koon oltava > 0.
     * @param initialSize
     */
    public HashTable(int initialSize) {
        if (initialSize <= 0)
            throw new IllegalArgumentException("Illegal table size");
        table = new ListNode[initialSize];
    }


    /**
     * Tämä luokka antaa käyttäjälle mahdollisuuden nähdä visuaalisena HashTablen toiminta ja 
     * poistaa sieltä arvoja.
     */
    void dump() {
        System.out.println();
        for (int i = 0; i < table.length; i++) {
            // Print out the location number and the list of
            // key/value pairs in this location.
            System.out.print(i + ":");
            ListNode list = table[i]; // For traversing linked list number i.
            while (list != null) {
                System.out.print("  (" + list.arvo + "," + list.rivi + "," +
                    list.tiedosto + ")");
                list = list.next;
            }
            System.out.println();
        }
    }


    /**
     * Tämä luokka lisää arvoja HashTableen.
     * Jokaisen arvon mukana tallennetaan metadatana arvon sijainti rivillä ja tiedostossa.
     * @param arvo
     * @param r
     * @param t
     */
    public void put(int arvo, int r, int t) {

        // Tarkistetaan, että arvo on olemassa.
        assert arvo != (int) arvo: "Arvo ei saa olla tyhjä.";

        int bucket = hash(arvo); // Millä index arvolla arvo sijaitsee.

        ListNode list = table[bucket]; // Valitaan HashTablesta index, joka vastaa arvon hashia.
        while (list != null) {
            // Selataan hashin arvoja ja tarkistetaan onko indexillä jo arvoa.
            if (list == null) {
                list.arvo = arvo;
                break;
            }
            list = list.next;
        }


        // Tarkistetaan onko valittu index null.
        if (list != null) {
            // Annetaan indexin arvoksi arvo.
            list.arvo = arvo;
        } else {
            //Lisätään node listaan, jotta tyhjään indexiin saadaan arvo
            if (count >= 0.75 * table.length) {
                // Jos taulu alkaa täyttyä suurennetaan sitä.
                // Ennen uduen arvon lisäämistä.
                resize();
                bucket = hash(arvo); // LAsketaan hash uudelleen, 
                // koska se ei enää täsmää.
            }
            // Asetetaan arvot uuteen nodeen.
            ListNode newNode = new ListNode();
            newNode.arvo = arvo;
            newNode.rivi = r;
            newNode.tiedosto = t;
            newNode.next = table[bucket];
            table[bucket] = newNode;
            count++; // Lisätään laskuriin yksi.
        }
    }


    /**
     * Palautetaan haetun arvon arvo, jos se löytyy taulusta.
     * Jos haettua arvo ei ole palautetaan null.
     * @param a
     * @return arvo / null
     */
    public String get(int a) {

        int bucket = hash(a); // Millä index arvolla arvo sijaitsee.
        ListNode list = table[bucket]; // Valitaan HashTablesta index, joka vastaa arvon hashia.
        while (list != null) {
            // Tarkistetaan onko arvo valitun indexin takana.
            if (list.arvo == a) {
                return " Arvo: " + list.arvo;
            } else {
                list = list.next; // Siirrytään seuraavaan arvoon listassa.
                System.out.println(list.arvo);
            }
        }

        // Jos haluttua arvoa ei löydy palautetaan null
        return null;
    }
    /**
     * Palautetaan haetun arvon arvo ja sijainti rivillä, jos se löytyy taulusta.
     * Jos haettua arvo ei ole palautetaan null.
     * @param a
     * @return arvo + rivi / null
     */
    public String getR(int a) {

        int bucket = hash(a); // Millä index arvolla arvo sijaitsee.
        ListNode list = table[bucket]; // Valitaan HashTablesta index, joka vastaa arvon hashia.
        while (list != null) {
            // Tarkistetaan onko arvo valitun indexin takana.
            if (list.arvo == a) {
                return a + " Rivillä -> " + list.rivi;
            } else {
                list = list.next; // Siirrytään seuraavaan arvoon listassa.
                System.out.println(list.arvo);
            }
        }
        return null;
    }

    /**
     * Palautetaan haetun arvon arvo ja tiedosto, jos se löytyy taulusta.
     * Jos haettua arvo ei ole palautetaan null.
     * @param a
     * @return arvo + tiedosto / null
     */
    public String getT(int a) {

        int bucket = hash(a); // Millä index arvolla arvo sijaitsee.
        ListNode list = table[bucket]; // Valitaan HashTablesta index, joka vastaa arvon hashia.
        while (list != null) {
            // Tarkistetaan onko arvo valitun indexin takana.
            if (list.arvo == a) {
                return a + " Tiedostossa ->" + list.tiedosto;
            } else {
                list = list.next; // Siirrytään seuraavaan arvoon listassa.
            }
        }
        return null;
    }

    /**
     * Palautetaan haetun arvon arvo ja määrä tiedostoissa, jos se löytyy taulusta.
     * Jos haettua arvo ei ole palautetaan null.
     * @param a
     * @return 
     */
    public String getMaara(int a) {
        int bucket = hash(a); // Millä index arvolla arvo sijaitsee.
        int maara = 0; // Laskuri määrälle
        int t = 1; // Tiedoston numero
        String palautus; // Attribuutti palautettavalle arvolle.
        
        // Lasketaan haluttujen arvojen määrä setA.txt tiedostossa.
        ListNode list1 = table[bucket]; // Valitaan HashTablesta index, joka vastaa arvon hashia.
        while (list1 != null) {
            if (list1.arvo == a && list1.tiedosto == t) {
                maara++;
            }
            list1 = list1.next;
        }
        palautus = " Määrä tiedostossa setA -> " + maara;

        // Nollataan määrä ja lisätään tiedosto laskuriin yksi.
        maara = 0;
        t++;
        
        // Lasketaan haluttujen arvojen määrä setB.txt tiedostossa.
        ListNode list2 = table[bucket]; // Valitaan HashTablesta index, joka vastaa arvon hashia.
        while (list2 != null) {
            if (list2.arvo == a && list2.tiedosto == t) {
                maara++;
            }
            list2 = list2.next;
        }
        // Yhdistetään kaksi stringiä ja palautetaan arvo + määrät tiedostoissa.
        return a + palautus + " Määrä tiedostossa setB -> " + maara;
    }

    /**
     * POistetaan arvo ja sen metadata Hash-taulusta.
     * Jos arvoa ei löydy taulusta ei tehdä mitään.
     * @param a
     * @return 
     */
    public boolean remove(int a) {

        int bucket = hash(a); // Millä index arvolla arvo sijaitsee.

        if (table[bucket] == null) {
            // Jos index on tyhjä poistutaan operaatiosta.
            return false;
        }

        if (table[bucket].arvo == a) {
            // Jos ensimmäinen arvo indexillä on haluttu arvo poistetaan se.
            table[bucket] = table[bucket].next;
            count--; // Poistetaan yksi Hash-Taulun koosta.
            // Palautetaan true, koska poisto onnistui.
            return true;
        }

        // Jos haettu arvo ei ole ensimmäisenä listalla haetaan haluttua arvoa listasta.
        // Kun arvo löytyy poistetaan ensimmäinen löydetty arvo.
        ListNode prev = table[bucket]; // Edellisen noden lippu.
        ListNode curr = prev.next; // Liikutaan listalla.
        // Aloitetaan toisesta nodesta.
        while (curr != null && curr.arvo != a) {
            curr = curr.next;
            prev = curr;
        }

        // Poistetaan löydetty arvo listalta.
        if (curr != null) {
            prev.next = curr.next;
            count--; // Poistetaan yksi HashTablen koosta.
        }
        // Palautetaan true, koska poisto onnistui.
        return true;
    }


    /**
     * Tarkistetaan onko indexillä jo arvoa.
     * @param a
     * @return Palautetaan true, jos on ja false, jos ei ole.
     */
    public boolean containsKey(int a) {

        int bucket = hash(a); // Millä index arvolla arvo sijaitsee.

        ListNode list = table[bucket]; // Liikutaan listalla haluttuun HashIndexiin.
        while (list != null) {
            // Jos indexillä on arvo palautetaan true.
            if (list.arvo == a) {
                return true;
            }
            list = list.next;
        }

        // Indexsillä ei ollut arvoa palautetaan false.
        return false;
    }


    /**
     * Palautetaan taulun koko.
     * @return 
     */
    public int size() {
        return count;
    }


    /**
     * Lasketaan arvolle hasharvo.
     * Hasharvon tulos riippuu taulun koosta.
     */
    private int hash(Object key) {
        return (Math.abs(key.hashCode())) % table.length;
    }


    /**
     * Tuplataan taulun koko, jos sen koko käy liian pieneksi lisättäville arvoille.
     * Vaihdetaan arvojen paikkoja ja hasheja vastaamaan uutta suurempaa taulua.
     */
    private void resize() {
        ListNode[] newtable = new ListNode[table.length * 2];
        for (ListNode list: table) {
            // Liikutaan listaa pitkin.
            while (list != null) {
                // Liikutaan seuraavaan nodeen listalla,
                ListNode next = list.next;
                // Tallennetaan listan arvo.
                Object k = list.arvo;
                int hash = (Math.abs(k.hashCode())) % newtable.length;
                // Lasketaan uusi hasharvo ja sijoitetaan arvo uudelle hasharvolleen.
                list.next = newtable[hash];
                newtable[hash] = list;
                list = next; // Liikutaan seuraavaan hashArvoon/nodeen vanhassa taulussa.
            }
        }
        table = newtable; // Vaihdetaan vanhataulu uuteen.
    }

    /**
     * Union joukko-operaatio.
     * Kaikki arvot mukaan mutta vain kerran.
     * @return union
     */
    public LinkedList < Integer > union() {
        
        // Luodaan listat jotka täytetään taulusta vastaamaan tiedostoa.
        LinkedList < Integer > t1 = new LinkedList < > ();
        LinkedList < Integer > t2 = new LinkedList < > ();
        
        // Täytetään listat tiedoston mukaan.
        for (ListNode list: table) {
            while (list != null) {
                if (list.tiedosto == 1) {
                    t1.add(list.arvo);
                } else if (list.tiedosto == 2) {
                    t2.add(list.arvo);
                }
                list = list.next;
            }
        }

        // Luodaan tyhjä lista union operaation suorittamista varten.
        LinkedList < Integer > union = new LinkedList < > ();

        // Lisätään valittu arvo unon listaan, jos se ei ole jo union listassa.
        for (int i = 0; i < t1.size(); i++) {
            if (!union.contains(t1.get(i))) {
                union.add(t1.get(i));
            }
        }
        
        // Lisätään valittu arvo unon listaan, jos se ei ole jo union listassa.
        for (int i = 0; i < t2.size(); i++) {
            if (!union.contains(t2.get(i))) {
                union.add(t2.get(i));
            }
        }
        // Palautetaan union lista.
        return union;
    }

    /**
     * Valitaan kahdesta tiedostosta ne arvot, jotka löytyvät molemmista tiedostoista.
     * @return leikkaus
     */
    public LinkedList < Integer > leikkaus() {
        
        // Luodaan listat jotka täytetään taulusta vastaamaan tiedostoa.
        LinkedList < Integer > t1 = new LinkedList < > ();
        LinkedList < Integer > t2 = new LinkedList < > ();
        
        // Täytetään listat tiedoston mukaan.
        for (ListNode list: table) {
            while (list != null) {
                if (list.tiedosto == 1) {
                    t1.add(list.arvo);
                } else if (list.tiedosto == 2) {
                    t2.add(list.arvo);
                }
                list = list.next;
            }
        }

        // Luodaan tyhjä lista leikkaus operaation suorittamista varten.
        LinkedList < Integer > leikkaus = new LinkedList < > ();
        
        // Tarkistetaan kumpi lista on suurempi. 
        // Jos suurempi lista ei sisällä valittua arvoa, eikä se ole jo leikkaus listassa lisätään se listaan.
        if (t1.size() < t2.size()) {
            for (int i = 0; i < t1.size(); i++) {
                if (t2.contains(t1.get(i)) && !leikkaus.contains(t1.get(i))) {
                    leikkaus.add(t1.get(i));
                }
            }
        } else {
            for (int i = 0; i < t2.size(); i++) {
                if (t1.contains(t2.get(i)) && !leikkaus.contains(t2.get(i))) {
                    leikkaus.add(t2.get(i));
                }
            }
        }

        // Palautetaan leikattulista.
        return leikkaus;
    }

    /**
     * VAlitaan arvot, jotka eivät ole kummassakin tiedsotossa mutta ovat toisessa.
     * @return 
     */
    public LinkedList <Integer> XOR() {
        
        // Luodaan listat jotka täytetään taulusta vastaamaan tiedostoa.
        LinkedList < Integer > t1 = new LinkedList < > ();
        LinkedList < Integer > t2 = new LinkedList < > ();
        
        // Täytetään listat tiedoston mukaan.
        for (ListNode list: table) {
            while (list != null) {
                if (list.tiedosto == 1) {
                    t1.add(list.arvo);
                } else if (list.tiedosto == 2) {
                    t2.add(list.arvo);
                }
                list = list.next;
            }
        }

        // Luodaan tyhjä lista XOR operaation suorittamista varten.
        LinkedList < Integer > XOR = new LinkedList < > ();

        // Jos arvoa ei löydy toisesta tiedostosta eikä sitä ole lisätty vielä niin lisätään arvo listaan.
        for (int i = 0; i < t1.size(); i++) {
            if (!XOR.contains(t1.get(i)) && !t2.contains(t1.get(i))) {
                XOR.add(t1.get(i));
            }
        }
        
        // Jos arvoa ei löydy toisesta tiedostosta eikä sitä ole lisätty vielä niin lisätään arvo listaan.
        for (int i = 0; i < t2.size(); i++) {
            if (!XOR.contains(t2.get(i)) && !t1.contains(t2.get(i))) {
                XOR.add(t2.get(i));
            }
        }
        
        // Palautetaan XOR-joukkooperaatiolla tehty lista.
        return XOR;
    }

}