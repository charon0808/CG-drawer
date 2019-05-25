package draw;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Frame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int xx, yy;
    private Color[] colors;
    private JPanel canvasPanel;
    private JSlider sliderR, sliderG, sliderB;
    private BufferedImage image;
    private CG cg;

    public void setCG(CG c) {
        cg = c;
        colors = cg.colors;
    }

    private void setColor(int r, int g, int b) {
        // System.err.printf("r=%d, g=%d, b=%d\n", r, g, b);
        cg.setColor(r, g, b);
    }

    public void setSlider(int r, int g, int b) {
        sliderR.setValue(r);
        sliderB.setValue(b);
        sliderG.setValue(g);
    }

    public void InitFrame() {
        Listener listener = new Listener(cg, this);

        canvasPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        canvasPanel.addMouseListener(listener);
        canvasPanel.addMouseMotionListener(listener);
        this.add(canvasPanel, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        JButton b1, b2, b3;
        b1 = new JButton("open");
        b2 = new JButton("save");
        b3 = new JButton("clear");

        b1.setPreferredSize(new Dimension(80, 30));
        b2.setPreferredSize(new Dimension(80, 30));
        b3.setPreferredSize(new Dimension(80, 30));

        b1.addActionListener(listener);
        b2.addActionListener(listener);
        b3.addActionListener(listener);

        toolBar.add(b1);
        toolBar.add(b2);
        toolBar.add(b3);

        this.add(toolBar, BorderLayout.PAGE_START);

        JPanel slidesPanel = new JPanel();
        sliderB = new JSlider(0, 255, 100);
        sliderB.setOrientation(SwingConstants.VERTICAL);
        sliderB.setMajorTickSpacing(10);
        sliderB.setMinorTickSpacing(5);

        sliderG = new JSlider(0, 255, 100);
        sliderG.setOrientation(SwingConstants.VERTICAL);
        sliderG.setMajorTickSpacing(10);
        sliderG.setMinorTickSpacing(5);

        sliderR = new JSlider(0, 255, 100);
        sliderR.setOrientation(SwingConstants.VERTICAL);
        sliderR.setMajorTickSpacing(10);
        sliderR.setMinorTickSpacing(5);

        slidesPanel.add(sliderR);
        slidesPanel.add(new JLabel("R"));
        slidesPanel.add(sliderG);
        slidesPanel.add(new JLabel("G"));
        slidesPanel.add(sliderB);
        slidesPanel.add(new JLabel("B"));

        JButton colorButton = new JButton();
        colorButton.setPreferredSize(new Dimension(80, 80));
        colorButton.setBackground(new Color(cg.getColor()));
        JPanel hahPanel = new JPanel();
        hahPanel.setLayout(new BorderLayout());
        hahPanel.add(slidesPanel, BorderLayout.PAGE_START);
        hahPanel.add(colorButton, BorderLayout.CENTER);

        sliderR.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!sliderR.getValueIsAdjusting()) {
                    setColor(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
                    colorButton.setBackground(new Color(cg.getColor()));
                }
            }
        });
        sliderG.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!sliderG.getValueIsAdjusting()) {
                    setColor(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
                    colorButton.setBackground(new Color(cg.getColor()));
                }
            }
        });
        sliderB.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!sliderB.getValueIsAdjusting()) {
                    setColor(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
                    colorButton.setBackground(new Color(cg.getColor()));
                }
            }
        });

        this.add(hahPanel, BorderLayout.LINE_START);

        JToolBar shapePanel = new JToolBar();
        shapePanel.setLayout(new FlowLayout());
        String[] shapes = {"line", "rectangle", "polygon", "ellipse", "clip", "drag", "rotate", "scale"};
        for (String i : shapes) {
            JButton button = new JButton(i);
            button.addActionListener(listener);
            shapePanel.add(button);
        }
        this.add(shapePanel, BorderLayout.PAGE_END);

        JPanel colorPanel = new JPanel(new GridLayout(6, colors.length, 4, 8));
        for (int i = 0; i < colors.length; i++) {
            Color color = colors[i];
            JButton cb = new JButton(String.valueOf(i));

            cb.setPreferredSize(new Dimension(60, 30));
            cb.setBackground(color);
            cb.addActionListener(listener);
            colorPanel.add(cb, BorderLayout.CENTER);
        }

        this.add(colorPanel, BorderLayout.LINE_END);
        this.setBackground(Color.WHITE);
        this.setSize(1050, 725);
        this.setTitle("CG-drawer");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setVisible(true);

    }

    public void updateImage(BufferedImage i) {
        image = i;
        canvasPanel.repaint();
    }

}
