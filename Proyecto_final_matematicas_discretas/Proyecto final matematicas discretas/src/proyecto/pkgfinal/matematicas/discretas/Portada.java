package proyecto.pkgfinal.matematicas.discretas;

import javax.swing.*;
import java.awt.event.*;

public class Portada extends JFrame {
    
    public Portada() {
        setTitle("Proyecto Final");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); 

        // Etiquetas con la información institucional
        JLabel l1 = new JLabel("BENEMERITA UNIVERSIDAD AUTONOMA DE PUEBLA");
        l1.setBounds(100, 30, 400, 30);
        add(l1);

        JLabel l2 = new JLabel("Facultad de Ciencias de la Computacion");
        l2.setBounds(120, 60, 300, 30);
        add(l2);

        JLabel l3 = new JLabel("Profesor: Luis Ernesto Valencia Segura");
        l3.setBounds(120, 100, 300, 30);
        add(l3);

        JLabel l4 = new JLabel("Integrantes:");
        l4.setBounds(120, 150, 100, 30);
        add(l4);

        JLabel i1 = new JLabel("Alonso Sanchez Perez  202459160");
        i1.setBounds(120, 190, 220, 20);
        add(i1);
        JLabel i2 = new JLabel("Juan Carlos Lopez Jimenez 202540390");
        i2.setBounds(120, 210, 250, 20);
        add(i2);
        JLabel i3 = new JLabel("Mario Emilio Sanchez Dorantes 202458674");
        i3.setBounds(120, 230, 250, 20);
        add(i3);
        JLabel i4 = new JLabel("Uriel Alexis Urbano Leon 202462272 ");
        i4.setBounds(120, 250, 220, 20);
        add(i4);
        
        JLabel l5 = new JLabel("Primavera 2026");
        l5.setBounds(120, 280, 200, 20);
        add(l5);

        JButton btn = new JButton("Entrar al sistema");
        btn.setBounds(160, 310, 150, 40);
        add(btn);

        // Al presionar, abrimos la aplicación principal y cerramos esta pantalla
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DijkstraApp principal = new DijkstraApp();
                principal.setVisible(true);
                dispose(); 
            }
        });
    }

    public static void main(String[] args) {
        new Portada().setVisible(true);
    }
}