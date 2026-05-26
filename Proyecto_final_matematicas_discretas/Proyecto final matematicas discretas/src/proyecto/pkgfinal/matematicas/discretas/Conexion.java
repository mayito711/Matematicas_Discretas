package proyecto.pkgfinal.matematicas.discretas;

public class Conexion {
    String destino;
    int peso;

    // Guardamos a dónde va la conexión y cuánto cuesta llegar (distancia o tiempo)
    public Conexion(String destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }
}