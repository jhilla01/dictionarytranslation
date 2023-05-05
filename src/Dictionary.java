import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dictionary extends JFrame implements ActionListener {
    private final JComboBox<String> inputField;
    private final AutocompleteComboBoxModel inputModel;
    private final JButton searchButton;
    private final JLabel translationLabel;
    private final JLabel imageLabel;
    private final JComboBox<String> languageSelector;


    private final String[] englishWords =
            {
                    "apple", "bridge", "carrot", "drawer", "energy", "flower", "guitar", "helmet", "island", "jungle",
                    "kitten", "ladder", "mirror", "needle", "orange", "pillow", "quiver", "rocket", "shadow", "thread",
                    "umbrella", "violin", "window", "xylophone", "yogurt"
            };
    private final String[][] translations = {
            // Spanish
            {
                    "manzana", "puente", "zanahoria", "cajón", "energía", "flor", "guitarra", "casco", "isla", "jungla",
                    "gatito", "escalera", "espejo", "aguja", "naranja", "almohada", "carcaj", "cohete", "sombra", "hilo",
                    "paraguas", "violín", "ventana", "xilófono", "yogur"
            },
            // German
            {
                    "Apfel", "Brücke", "Karotte", "Schublade", "Energie", "Blume", "Gitarre", "Helm", "Insel", "Dschungel",
                    "Kätzchen", "Leiter", "Spiegel", "Nadel", "Orange", "Kissen", "Köcher", "Rakete", "Schatten", "Faden",
                    "Regenschirm", "Violine", "Fenster", "Xylophon", "Joghurt"
            },
            // Norwegian
            {
                    "eple", "bro", "gulrot", "skuff", "energi", "blomst", "gitar", "hjelm", "øy", "jungel",
                    "kattunge", "stige", "speil", "nål", "appelsin", "pute", "piler", "rakett", "skygge", "tråd",
                    "paraply", "fiolin", "vindu", "xylofon", "yoghurt"
            }
    };
    private final String[] imageFileNames = {
            "apple.jpg", "bridge.png", "carrot.jpg", "drawer.png", "energy.png", "flower.png", "guitar.jpg", "helmet.png", "island.png", "jungle.png",
            "kitten.jpg", "ladder.png", "mirror.png", "needle.png", "orange.jpg", "pillow.jpg", "quiver.png", "rocket.png", "shadow.jpg", "thread.jpg",
            "umbrella.png", "violin.png", "window.jpg", "xylophone.png", "yogurt.png"
    };

    public Dictionary() {
        setTitle("Dictionary");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        inputModel = new AutocompleteComboBoxModel(englishWords);
        inputField = new JComboBox<>(inputModel);
        inputField.setEditable(true);
        inputField.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                    String prefix = inputField.getEditor().getItem().toString();
                    inputModel.updateSuggestions(prefix);
                    inputField.showPopup();
                }
            }
        });
        inputField.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                inputModel.setShowingDropdown(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                inputModel.setShowingDropdown(false);
            }
        });

        searchButton = new JButton("Search");
        languageSelector = new JComboBox<>(new String[]{"Spanish", "German", "Norwegian"});

        inputPanel.add(languageSelector);
        inputPanel.add(inputField);
        inputPanel.add(searchButton);

        translationLabel = new JLabel();
        imageLabel = new JLabel();

        JList<String> wordList = new JList<>(englishWords);
        wordList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    inputField.setSelectedItem(wordList.getSelectedValue());
                    searchWord();
                }
            }
        });
        JScrollPane wordListPane = new JScrollPane(wordList);

        searchButton.addActionListener(this);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(translationLabel, BorderLayout.NORTH);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);
        add(wordListPane, BorderLayout.WEST);
    }






    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchWord();
        }
    }

    private void searchWord() {
        String input = inputField.getEditor().getItem().toString().toLowerCase();
        boolean wordFound = false;
        int languageIndex = languageSelector.getSelectedIndex();
        for (int i = 0; i < englishWords.length; i++) {
            if (englishWords[i].equalsIgnoreCase(input)) {
                wordFound = true;
                translationLabel.setText(translations[languageIndex][i]);

                try {
                    URL imageUrl = getClass().getResource("/images/" + imageFileNames[i]);
                    Image image = ImageIO.read(imageUrl);

                    // Scale the image to a smaller size
                    int newWidth = 250;
                    int newHeight = 250;
                    Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                    ImageIcon icon = new ImageIcon(scaledImage);
                    imageLabel.setIcon(icon);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
        if (!wordFound) {
            translationLabel.setText("Word not found");
            imageLabel.setIcon(null);
        }
    }

    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();
        dictionary.setVisible(true);
    }
}