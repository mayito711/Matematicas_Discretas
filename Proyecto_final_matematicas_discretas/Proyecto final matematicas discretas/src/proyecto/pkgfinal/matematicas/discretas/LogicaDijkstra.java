package proyecto.pkgfinal.matematicas.discretas;

import java.io.File;
import java.util.*;

class NodoD implements Comparable<NodoD> {
    String nombre;
    int dist;

    public NodoD(String n, int d) { nombre = n; dist = d; }

    public int compareTo(NodoD otro) { return Integer.compare(this.dist, otro.dist); }
}

public class LogicaDijkstra {

    // Método para cargar el grafo desde un archivo de texto
    public Map<String, List<Conexion>> leerArchivo(String nombreArchivo) {
        Map<String, List<Conexion>> mapa = new HashMap<>();
        try {
            Scanner sc = new Scanner(new File(nombreArchivo));
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if (linea.isEmpty()) continue;
                
                String[] partes = linea.split(" ");
                if (partes.length == 3) {
                    String n1 = partes[0];
                    String n2 = partes[1];
                    int costo = Integer.parseInt(partes[2]);

                    mapa.putIfAbsent(n1, new ArrayList<>());
                    mapa.putIfAbsent(n2, new ArrayList<>());

                    mapa.get(n1).add(new Conexion(n2, costo));
                    mapa.get(n2).add(new Conexion(n1, costo));
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
        return mapa;
    }

    // Ejecuta el algoritmo de Dijkstra para encontrar la ruta mínima
    public void ejecutar(Map<String, List<Conexion>> grafo, String inicio, String meta, List<String> camino, int[] distancia) {
        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> padres = new HashMap<>();
        ArrayList<String> visitados = new ArrayList<>();

        for (String v : grafo.keySet()) {
            distancias.put(v, 999999);
        }

        distancias.put(inicio, 0);
        PriorityQueue<NodoD> pq = new PriorityQueue<>();
        pq.add(new NodoD(inicio, 0));

        while (!pq.isEmpty()) {
            NodoD actual = pq.poll();
            String u = actual.nombre;

            if (visitados.contains(u)) continue;
            visitados.add(u);

            if (u.equals(meta)) break;

            List<Conexion> vecinos = grafo.get(u);
            if (vecinos == null) continue;

            for (Conexion c : vecinos) {
                int suma = distancias.get(u) + c.peso;
                if (suma < distancias.get(c.destino)) {
                    distancias.put(c.destino, suma);
                    padres.put(c.destino, u);
                    pq.add(new NodoD(c.destino, suma));
                }
            }
        }

        // Si se alcanzó la meta, construimos la ruta resultante
        if (distancias.get(meta) != 999999) {
            distancia[0] = distancias.get(meta);
            
            ArrayList<String> temp = new ArrayList<>();
            String aux = meta;
            while (aux != null) {
                temp.add(aux);
                aux = padres.get(aux);
            }

            Collections.reverse(temp);
            camino.addAll(temp);
        } else {
            distancia[0] = -1;
        }
    }
}