import java.util.Scanner;

public class JuegoPoker {
    public static void main(String[] args) {
        var deck =  new Deck();
        var consola = new Scanner(System.in);
        var salir = false;
        System.out.println("*** Baraja de poker ***\n");
        deck.mezclarDeck();
        deck.despliegaDeck();
        while (!salir) {
            try {
                var opcion = mostrarMenu(consola);
                salir = ejecutarOpciones(opcion, deck);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println();
        }
    }

    public static int mostrarMenu(Scanner consola) {
        System.out.println("""
                *** Menu ***
                1. Mostrar baraja
                2. Mezclar baraja
                3. Remover primera carta
                4. Remover carta aleatoria
                5. Remover 5 cartas aleatorias
                6. Salir
                """);
        return Integer.parseInt(consola.nextLine());
    }

    public static boolean ejecutarOpciones(int opcion, Deck deck){
        var salir = false;
        switch (opcion) {
            case 1 -> deck.despliegaDeck();
            case 2 -> deck.mezclarDeck();
            case 3 -> deck.head();
            case 4 -> deck.cartaRandom();
            case 5 -> deck.cincoCartas();
            case 6 -> {
                System.out.println("Gracias por jugar!");
                salir = true;
            }
            default -> System.out.println("Opcion no valida");
        }
        return salir;
    }


}
