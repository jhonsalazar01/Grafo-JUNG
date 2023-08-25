import edu.uci.ics.jung.algorithms.layout.StaticLayout; // Import the StaticLayout class
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TransportGraph extends JFrame {

    public static void main(String[] args) {
        Graph<String, String> graph = new UndirectedSparseGraph<String, String>();

        graph.addVertex("Sogamoso");
        graph.addVertex("Tunja");
        graph.addVertex("Paipa");
        graph.addVertex("Duitama");
        graph.addVertex("Nobsa");
        graph.addVertex("Tibasosa");
        graph.addVertex("Bogota");
        graph.addVertex("Firavitoba");
        graph.addVertex("Aguazul");
        graph.addVertex("Yopal");
        graph.addVertex("Villavicencio");

        graph.addEdge("138", "Bogota", "Tunja");
        graph.addEdge("17", "Duitama", "Nobsa");
        graph.addEdge("41", "Paipa", "Tunja");
        graph.addEdge("13", "Duitama", "Paipa");
        graph.addEdge("12", "Duitama", "Tibasosa");
        graph.addEdge("9", "Tibasosa", "Sogamoso");
        graph.addEdge("8", "Nobsa", "Sogamoso");
        graph.addEdge("10", "Firavitoba", "Sogamoso");
        graph.addEdge("137", "Sogamoso", "Aguazul");
        graph.addEdge("27", "Aguazul", "Yopal");
        graph.addEdge("251", "Villavicencio", "Yopal");
        graph.addEdge("125", "Villavicencio", "Bogota");


        StaticLayout<String, String> layout = new StaticLayout<>(graph);
        layout.setLocation("Bogota", 20, 290);
        layout.setLocation("Tunja", 50, 190);
        layout.setLocation("Paipa", 100, 90);
        layout.setLocation("Duitama", 150, 50);
        layout.setLocation("Sogamoso", 330, 80);
        layout.setLocation("Tibasosa", 210, 150);
        layout.setLocation("Firavitoba", 250, 190);
        layout.setLocation("Nobsa", 230, 50);
        layout.setLocation("Aguazul", 400, 130);
        layout.setLocation("Yopal", 450, 180);
        layout.setLocation("Villavicencio", 150, 350);


        VisualizationViewer<String, String> viewer = new VisualizationViewer<>(layout, new Dimension(500, 480));

        viewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
        viewer.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        JPanel panel = new JPanel();
        panel.add(viewer);

        JComboBox<String> sourceComboBox = new JComboBox<>(graph.getVertices().toArray(new String[0]));
        JComboBox<String> targetComboBox = new JComboBox<>(graph.getVertices().toArray(new String[0]));
        JButton calculateButton = new JButton("Calcular");

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Origen: "));
        controlPanel.add(sourceComboBox);
        controlPanel.add(new JLabel("Destino: "));
        controlPanel.add(targetComboBox);
        controlPanel.add(calculateButton);

        panel.add(controlPanel);
        JLabel selectedPathLabel = new JLabel("Ruta: ");
        panel.add(selectedPathLabel);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double distanceT = 0;
                String source = (String) sourceComboBox.getSelectedItem();
                String target = (String) targetComboBox.getSelectedItem();

                DijkstraShortestPath<String, String> dijkstra = new DijkstraShortestPath<>(graph);
                List<String> shortestPath = dijkstra.getPath(source, target);

                if (shortestPath != null) {
                    double distance = (double) dijkstra.getDistance(source, target);

                    String pathString = String.join(" -> ", shortestPath);
                    for (String c : shortestPath) {
                        distanceT += Double.parseDouble(c);

                    }



                    viewer.getRenderContext().setEdgeDrawPaintTransformer(edge -> {
                        if (shortestPath.contains(edge)) {
                            return Color.RED;
                        }
                        return Color.BLACK;
                    });

                    viewer.getRenderContext().setEdgeStrokeTransformer(edge -> {
                        if (shortestPath.contains(edge)) {
                            return new BasicStroke(5);
                        }
                        return new BasicStroke(1);
                    });

                    viewer.repaint();

                    selectedPathLabel.setText("Ruta: " + pathString + "     " + "Distancia " + distanceT+" km");
                }
            }


        });
        JComboBox<String> disableComboBox = new JComboBox<>(graph.getVertices().toArray(new String[0]));
        JButton disableButton = new JButton("Restringir");
        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedNode = (String) disableComboBox.getSelectedItem();

                if (selectedNode != null) {
                    graph.removeVertex(selectedNode);
                    viewer.repaint();
                }
            }
        });
        controlPanel.add(new JLabel("Inhabilitar Nodo: "));
        controlPanel.add(disableComboBox);
        controlPanel.add(disableButton);



        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setSize(750, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}