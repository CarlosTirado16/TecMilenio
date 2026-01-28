public class Card {
    String palo;
    String color;
    int numero;

    public Card(int numero, String palo) {
        this.numero = numero;
        this.palo = palo;
        if (palo.equals("Diamante") || palo.equals("Corazon"))
            this.color = "Rojo";
        else
            this.color = "Negro";
    }

    @Override
    public String toString() {
        return "Carta: " + palo + " " + color + " " + numero;
    }

    public String getPalo() {
        return palo;
    }

    public void setPalo(String palo) {
        this.palo = palo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
