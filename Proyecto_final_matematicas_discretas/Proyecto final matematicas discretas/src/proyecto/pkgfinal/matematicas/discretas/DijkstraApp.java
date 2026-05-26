package proyecto.pkgfinal.matematicas.discretas;

import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DijkstraApp extends JFrame {
    
    Map<String, List<Conexion>> grafo;
    JComboBox<String> origenCombo;
    JComboBox<String> destinoCombo;
    JTextArea textoRuta;
    JLabel labelDistancia;
    PanelGrafo panelDibujo;

    public DijkstraApp() {
        setTitle("Algoritmo de Dijkstra");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        grafo = new HashMap<>();
        cargarTxt();

        setLayout(new BorderLayout());

        JPanel panelArriba = new JPanel();
        origenCombo = new JComboBox<>();
        destinoCombo = new JComboBox<>();

        ArrayList<String> lista = new ArrayList<>();
        for (String n : grafo.keySet()) {
            lista.add(n);
        }
        Collections.sort(lista);
        
        for (int i = 0; i < lista.size(); i++) {
            origenCombo.addItem(lista.get(i));
            destinoCombo.addItem(lista.get(i));
        }

        JButton boton = new JButton("Calcular");
        boton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcularDijkstra();
            }
        });

        panelArriba.add(new JLabel("Origen:"));
        panelArriba.add(origenCombo);
        panelArriba.add(new JLabel("Destino:"));
        panelArriba.add(destinoCombo);
        panelArriba.add(boton);

        add(panelArriba, BorderLayout.NORTH);

        panelDibujo = new PanelGrafo();
        
        JScrollPane scrollGrafo = new JScrollPane(panelDibujo);
        scrollGrafo.getVerticalScrollBar().setUnitIncrement(16); 
        add(scrollGrafo, BorderLayout.CENTER);

        JPanel panelAbajo = new JPanel();
        panelAbajo.setLayout(new BorderLayout());
        labelDistancia = new JLabel("Distancia: ");
        textoRuta = new JTextArea(4, 50);
        textoRuta.setEditable(false);
        
        panelAbajo.add(labelDistancia, BorderLayout.NORTH);
        panelAbajo.add(new JScrollPane(textoRuta), BorderLayout.CENTER);

        add(panelAbajo, BorderLayout.SOUTH);
    }

    public void cargarTxt() {
        try {
            Scanner sc = new Scanner(new File("ciudades_grafo.txt"));
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if (linea.isEmpty()) continue;
                
                String[] partes = linea.split(" ");
                if (partes.length == 3) {
                    String n1 = partes[0];
                    String n2 = partes[1];
                    int peso = Integer.parseInt(partes[2]);

                    // Volvemos a la forma manual, menos "elegante" pero muy humana
                    if (grafo.get(n1) == null) grafo.put(n1, new ArrayList<>());
                    if (grafo.get(n2) == null) grafo.put(n2, new ArrayList<>());

                    grafo.get(n1).add(new Conexion(n2, peso));
                    grafo.get(n2).add(new Conexion(n1, peso));
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }

    public void calcularDijkstra() {
        String inicio = origenCombo.getSelectedItem().toString();
        String meta = destinoCombo.getSelectedItem().toString();

        if (inicio.equals(meta)) {
            JOptionPane.showMessageDialog(null, "Selecciona dos estaciones diferentes");
            return;
        }

        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> padres = new HashMap<>();
        ArrayList<String> visitados = new ArrayList<>();

        for (String nodo : grafo.keySet()) {
            distancias.put(nodo, 999999); 
        }

        distancias.put(inicio, 0);
        PriorityQueue<NodoRuta> cola = new PriorityQueue<>();
        cola.add(new NodoRuta(inicio, 0));

        while (!cola.isEmpty()) {
            NodoRuta actual = cola.poll();
            String u = actual.nombre;

            if (visitados.contains(u)) continue;
            visitados.add(u);

            if (u.equals(meta)) break;

            List<Conexion> vecinos = grafo.get(u);
            if (vecinos != null) {
                // For manual, como se ve más estudiantil
                for (int i = 0; i < vecinos.size(); i++) {
                    Conexion c = vecinos.get(i);
                    int suma = distancias.get(u) + c.peso;
                    if (suma < distancias.get(c.destino)) {
                        distancias.put(c.destino, suma);
                        padres.put(c.destino, u);
                        cola.add(new NodoRuta(c.destino, suma));
                    }
                }
            }
        }

        if (distancias.get(meta) != 999999) {
            labelDistancia.setText("Distancia: " + distancias.get(meta));

            // aqui armamos la ruta final
            ArrayList<String> temp = new ArrayList<>();
            String paso = meta;
            while (paso != null) {
                temp.add(paso);
                paso = padres.get(paso);
            }

            // reversa manual, nada de Collections.reverse
            ArrayList<String> camino = new ArrayList<>();
            for (int i = temp.size() - 1; i >= 0; i--) {
                camino.add(temp.get(i));
            }
            
            // concatenacion manual para armar el texto
            String rutaTxt = "";
            for (int i = 0; i < camino.size(); i++) {
                rutaTxt += camino.get(i) + (i == camino.size() - 1 ? "" : " -> ");
            }
            textoRuta.setText(rutaTxt);

            panelDibujo.ruta = camino;
            panelDibujo.repaint();
        } else {
            labelDistancia.setText("No hay conexión");
            textoRuta.setText("");
            panelDibujo.ruta = null;
            panelDibujo.repaint();
            JOptionPane.showMessageDialog(null, "No se encontró un camino");
        }
    }

    class PanelGrafo extends JPanel {
        Map<String, Point> coordenadas;
        ArrayList<String> ruta;

        public PanelGrafo() {
            setBackground(Color.WHITE);
            coordenadas = new HashMap<>();
            ruta = new ArrayList<>();

            int x = 50, y = 50;
            for (String nodo : grafo.keySet()) {
                if (nodo.equals("Puebla")) coordenadas.put(nodo, new Point(450, 300));
                else if (nodo.equals("CDMX")) coordenadas.put(nodo, new Point(350, 250));
                else if (nodo.equals("Veracruz")) coordenadas.put(nodo, new Point(600, 300));
                else if (nodo.equals("Oaxaca")) coordenadas.put(nodo, new Point(500, 450));
                else if (nodo.equals("Tlaxcala")) coordenadas.put(nodo, new Point(460, 230));
                else {
                    coordenadas.put(nodo, new Point(x, y));
                    x += 160;
                    if (x > 1200) { x = 50; y += 130; }
                }
            }
            setPreferredSize(new Dimension(1300, y + 200));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.LIGHT_GRAY);
            for (String nodo : grafo.keySet()) {
                Point p1 = coordenadas.get(nodo);
                for (Conexion c : grafo.get(nodo)) {
                    Point p2 = coordenadas.get(c.destino);
                    if (p1 != null && p2 != null) g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }

            g.setColor(Color.DARK_GRAY);
            for (String nodo : grafo.keySet()) {
                Point p = coordenadas.get(nodo);
                if (p != null) {
                    g.fillOval(p.x - 5, p.y - 5, 10, 10);
                    g.drawString(nodo, p.x + 10, p.y);
                }
            }

            if (ruta != null && !ruta.isEmpty()) {
                g.setColor(Color.RED);
                for (int i = 0; i < ruta.size() - 1; i++) {
                    Point p1 = coordenadas.get(ruta.get(i));
                    Point p2 = coordenadas.get(ruta.get(i + 1));
                    if (p1 != null && p2 != null) g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
                g.setColor(Color.BLUE);
                for (String nodo : ruta) {
                    Point p = coordenadas.get(nodo);
                    if (p != null) g.fillOval(p.x - 7, p.y - 7, 14, 14);
                }
            }
        }
    }
}

class Conexion {
    String destino;
    int peso;
    public Conexion(String dest, int p) { destino = dest; peso = p; }
}

class NodoRuta implements Comparable<NodoRuta> {
    String nombre;
    int dist;
    public NodoRuta(String n, int d) { nombre = n; dist = d; }
    
    // comparacion manual, mucho más estudiantil
    public int compareTo(NodoRuta otro) {
        if (this.dist < otro.dist) return -1;
        if (this.dist > otro.dist) return 1;
        return 0;
    }
}