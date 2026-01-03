package MazeProgram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Graphical Component and model of the 'Settings' pop-up window
 */
public class SettingsWindow extends JDialog {

    /**
     * A reference to the main cell grid.
     */
    private final GridCell cellGrid;

    /**
     * A reference to the panel to be repainted.
     */
    private final JPanel mainPanel;

    private final MazeGenerator mazeGenerator;

    /**
     * Constructs a new 'SettingsWindow' object.
     */
    public SettingsWindow(GridCell cellGrid, JPanel mainPanel, MazeGenerator mazeGenerator) {
        this.cellGrid = cellGrid;
        this.mainPanel = mainPanel;
        this.mazeGenerator = mazeGenerator;

        setTitle("Settings");
        setResizable(false);
        setLocationRelativeTo(null);
        setPreferredSize(new Dimension(300, 200));
        add(createColorOptions());
        add(showAnimationOption(), BorderLayout.SOUTH);

        JLabel colorText = new JLabel("Select Color");
        colorText.setHorizontalAlignment(SwingConstants.CENTER);
        add(colorText, BorderLayout.NORTH);
    }

    /**
     * Creates and returns a JComboBox allowing the user to select a color for the maze from a list
     * of colors.
     */
    private JComboBox<String> createColorOptions() {
        JComboBox<String> colorChoices = new JComboBox<>();
        colorChoices.addItem("Cyan");
        colorChoices.addItem("Green");
        colorChoices.addItem("Blue");
        colorChoices.addItem("Red");
        colorChoices.addItem("Yellow");
        colorChoices.addItem("Orange");
        colorChoices.addItem("Pink");
        colorChoices.addItem("aMAZEme");

        /*
        Listener that updates the maze color upon selection
         */
        colorChoices.addActionListener(e -> {
            switch ((String) colorChoices.getSelectedItem()) {
                case ("Cyan"):
                    cellGrid.updateColor(Color.CYAN);
                    cellGrid.repaint();
                    mainPanel.repaint();
                    break;
                case ("Green"):
                    cellGrid.updateColor(Color.GREEN);
                    cellGrid.repaint();
                    mainPanel.repaint();
                    break;
                case ("Blue"):
                    cellGrid.updateColor(Color.BLUE);
                    cellGrid.repaint();
                    mainPanel.repaint();
                    break;
                case ("Red"):
                    cellGrid.updateColor(Color.RED);
                    cellGrid.repaint();
                    mainPanel.repaint();
                    break;
                case ("Yellow"):
                    cellGrid.updateColor(Color.YELLOW);
                    cellGrid.repaint();
                    mainPanel.repaint();
                    break;
                case ("Orange"):
                    cellGrid.updateColor(Color.ORANGE);
                    cellGrid.repaint();
                    mainPanel.repaint();
                    break;
                case ("Pink"):
                    cellGrid.updateColor(Color.PINK);
                    cellGrid.repaint();
                    mainPanel.repaint();
                    break;
                case ("aMAZEme"): // option for a random color
                    Random rand = new Random();
                    int r = rand.nextInt(256);
                    int g = rand.nextInt(256);
                    int b = rand.nextInt(256);

                    Color randColor = new Color(r, g, b);
                    cellGrid.updateColor(randColor);
                    cellGrid.repaint();
                    mainPanel.repaint();
                    break;
            }
        });
        return colorChoices;

    }

    private JCheckBox showAnimationOption() {
        JCheckBox animationSelect = new JCheckBox("Show Animation", true);
        animationSelect.addActionListener(e -> {
            mazeGenerator.setAnimated();
        });

        return animationSelect;


    }


}
