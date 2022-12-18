import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.*;

public class Viewer {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        // slider to control horizontal rotation
        JSlider headingSlider = new JSlider(0, 360, 180);
        pane.add(headingSlider, BorderLayout.SOUTH);

        // slider to control vertical rotation
        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -180, 180, 0);
        pane.add(pitchSlider, BorderLayout.EAST);

        final Vertex[] camera = {new Vertex(0, 0, -500)};

        final double[] distance = {200};

        // panel to display render results
        JPanel renderPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // 1 - (100, 100, 100)
                // 2 - (-100, 100, 100)
                // 3 - (-100, -100, 100)
                // 4 - (100, -100, 100)
                // 5 - (100, 100, -100)
                // 6 - (-100, 100, -100)
                // 7 - (-100, -100, -100)
                // 8 - (100, -100, -100)
                // ^
                // |
                // Грани - 1234, 2673, 6587, 5148, 5621, 4378

                List<Square> cube = new ArrayList<>();
                cube.add(new Square(new Vertex(100, 100, 100), new Vertex(-100, 100, 100), new Vertex(-100, -100, 100), new Vertex(100, -100, 100)));
                cube.add(new Square(new Vertex(-100, 100, 100), new Vertex(-100, 100, -100), new Vertex(-100, -100, -100), new Vertex(-100, -100, 100)));
                cube.add(new Square(new Vertex(-100, 100, -100), new Vertex(100, 100, -100), new Vertex(100, -100, -100), new Vertex(-100, -100, -100)));
                cube.add(new Square(new Vertex(100, 100, -100), new Vertex(100, 100, 100), new Vertex(100, -100, 100), new Vertex(100, -100, -100)));
                cube.add(new Square(new Vertex(100, 100, -100), new Vertex(-100, 100, -100), new Vertex(-100, 100, 100), new Vertex(100, 100, 100)));
                cube.add(new Square(new Vertex(100, -100, 100), new Vertex(-100, -100, 100), new Vertex(-100, -100, -100), new Vertex(100, -100, -100)));

                double heading = -Math.toRadians(headingSlider.getValue());
                double pitch = -Math.toRadians(pitchSlider.getValue());
                Matrix3 pitchTransform = new Matrix3(new double[] {
                        Math.cos(pitch), 0, -Math.sin(pitch),
                        0, 1, 0,
                        Math.sin(pitch), 0, Math.cos(pitch)
                });
                Matrix3 headingTransform = new Matrix3(new double[] {
                        1, 0, 0,
                        0, Math.cos(heading), Math.sin(heading),
                        0, -Math.sin(heading), Math.cos(heading)
                });

                g2.translate(getWidth()/2, getHeight()/2);
                g2.setColor(Color.WHITE);

                Matrix3 transform = headingTransform.multiply(pitchTransform);

                for (Square s : cube) {
                    Vertex v1 = transform.transform(s.v1);
                    Vertex v2 = transform.transform(s.v2);
                    Vertex v3 = transform.transform(s.v3);
                    Vertex v4 = transform.transform(s.v4);

                    Square sTemp = new Square(v1, v2, v3, v4);

                    v1 = v1.viewCoordinateTrans(camera[0]);
                    v2 = v2.viewCoordinateTrans(camera[0]);
                    v3 = v3.viewCoordinateTrans(camera[0]);
                    v4 = v4.viewCoordinateTrans(camera[0]);

                    Point2D vs1 = v1.screenTransition(distance[0]);
                    Point2D vs2 = v2.screenTransition(distance[0]);
                    Point2D vs3 = v3.screenTransition(distance[0]);
                    Point2D vs4 = v4.screenTransition(distance[0]);

                    if (sTemp.isVisible(camera[0])){

                        Path2D path = new Path2D.Double();
                        path.moveTo(vs1.getX(), vs1.getY());
                        path.lineTo(vs2.getX(), vs2.getY());
                        path.lineTo(vs3.getX(), vs3.getY());
                        path.lineTo(vs4.getX(), vs4.getY());
                        path.closePath();

                        g2.draw(path);

                    }

                }
            }
        };

        renderPanel.setFocusable(true);

        pane.add(renderPanel, BorderLayout.CENTER);

        renderPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double multiplier = 1;
                if(e.getWheelRotation()<0)
                    for(int i = 0; i<-e.getWheelRotation();i++)
                        multiplier*=1.1;
                else
                    for(int i = 0; i<e.getWheelRotation();i++)
                        multiplier/=1.1;

                distance[0] *= multiplier;
                renderPanel.repaint();
            }

        });

        final int[] previousX = {-1000};
        final int[] currentX = {0};
        final int[] previousY = {-1000};
        final int[] currentY = {0};

        renderPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentX[0] = e.getX();
                if (previousX[0] == -1000){
                    previousX[0] = currentX[0];
                }
                int deltaX = currentX[0] - previousX[0];
                previousX[0] = currentX[0];

                currentY[0] = e.getY();
                if (previousY[0] == -1000){
                    previousY[0] = currentY[0];
                }
                int deltaY = currentY[0] - previousY[0];
                previousY[0] = currentY[0];

                VertexSpherical spherical = new VertexSpherical(0, 0, 0);
                spherical = spherical.convert(camera[0]);

                spherical.phi -= Math.toRadians(deltaX);
                spherical.zeta += Math.toRadians(deltaY);

                if (spherical.phi >= Math.PI) {
                    spherical.phi -= Math.PI;
                }

                if (spherical.phi <= 0) {
                    spherical.phi += Math.PI;
                }

                if (spherical.zeta >= Math.PI) {
                    spherical.zeta -= Math.PI;

                }

                if (spherical.zeta <= 0) {
                    spherical.zeta += Math.PI;
                }

                camera[0].x = spherical.r * Math.sin(spherical.zeta) * Math.cos(spherical.phi);
                camera[0].y = spherical.r * Math.sin(spherical.zeta) * Math.sin(spherical.phi);
                camera[0].z = spherical.r * Math.cos(spherical.zeta);

                renderPanel.repaint();

            }

            @Override
            public void mouseMoved(MouseEvent e) {

                previousX[0] = -1000;
                previousY[0] = -1000;

            }
        });

        renderPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int degOnX = 0;
                int degOnZ = 0;

                if(e.getKeyCode() == 68) {
                    degOnX += 2;
                }
                if(e.getKeyCode() == 65) {
                    degOnX += -2;
                }
                if(e.getKeyCode() == 87) {
                    degOnZ += 2;
                }
                if(e.getKeyCode() == 83) {
                    degOnZ += -2;
                }




            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        headingSlider.addChangeListener(e -> renderPanel.repaint());
        pitchSlider.addChangeListener(e -> renderPanel.repaint());

        frame.setSize(800, 800);
        frame.setVisible(true);


    }
}
