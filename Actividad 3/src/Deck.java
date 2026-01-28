import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    ArrayList <Card> deck = new ArrayList<>();

    public Deck() {
        crearDeck();
    }

    public void crearDeck() {
        System.out.println("Creando baraja");
        for (int i = 0; i <= 12; i++) {
            deck.add(new Card(i + 1, "Trebol"));
        }
        for (int i = 0; i <= 12; i++) {
            deck.add(new Card(i + 1, "Pica"));
        }
        for (int i = 0; i <= 12; i++) {
            deck.add(new Card(i + 1, "Corazon"));
        }
        for (int i = 0; i <= 12; i++) {
            deck.add(new Card(i + 1, "Diamante"));
        }

    }

    public void despliegaDeck() {
        for (Card carta: deck) {
            System.out.println(carta.toString());
        }
        System.out.println();
    }

    public void mezclarDeck() {
        System.out.println("Mezclando Deck");
        Collections.shuffle(deck);
        System.out.println();
    }

    public void head() {
        System.out.println("Carta de hasta arriba");
        System.out.println(deck.get(0));
        deck.remove(0);
        System.out.println("La carta se ha removido, quedan "
                + deck.size() + " cartas");
        System.out.println();
    }

    public void cartaRandom() {
        int min = 0;
        int max = deck.size() - 1;
        int aleatorio = (int)(Math.random() * (max - min + 1)) + min;
        System.out.println("Su carta aleatoria fue...");
        System.out.println(deck.get(aleatorio));
        deck.remove(aleatorio);
        System.out.println("Su carta ha sido removida, quedan "
                            + deck.size() + " cartas");
        System.out.println();
    }

    public void cincoCartas() {
        System.out.println("Eligiendo 5 cartas");
        for (int i = 0; i < 5; i++){
            int min = 0;
            int max = deck.size() - 1;
            int aleatorio = (int)(Math.random() * (max - min + 1)) + min;
            System.out.println(deck.get(aleatorio));
            deck.remove(aleatorio);
        }
        System.out.println("Sus cartas han sido removidas, quedan "
                + deck.size() + " cartas");
        System.out.println();
    }

}
