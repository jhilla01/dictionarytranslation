import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Dictionary extends JFrame implements ActionListener {
    private final JComboBox<String> inputField;
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
            // Set JFrame properties
            setTitle("Dictionary");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // Create a JPanel to hold the input components
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

            // Create a JComboBox for language selection, a JComboBox for input, and a search JButton
            languageSelector = new JComboBox<>(new String[]{"Spanish", "German", "Norwegian"});
            inputField = new JComboBox<>(englishWords);
            inputField.setEditable(true);
            inputField.setSelectedIndex(-1);
            searchButton = new JButton("Search");

           // Add the components to the input panel
            inputPanel.add(languageSelector);
            inputPanel.add(inputField);
            inputPanel.add(searchButton);

            // Create JLabels for displaying translation and images
            translationLabel = new JLabel();
            imageLabel = new JLabel();

            // Add an ActionListener to the search button
            searchButton.addActionListener(this);

            // Create a JPanel for displaying the translation and image
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(translationLabel, BorderLayout.NORTH);
            centerPanel.add(imageLabel, BorderLayout.CENTER);

            // Create a JList for displaying the list of English words
            JList<String> wordList = new JList<>(englishWords);
            // Add a ListSelectionListener to the word list to update the input field when a word is selected
            wordList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Get the selected word from the list and set it as the input field's selected item
                    String selectedWord = wordList.getSelectedValue();
                    if (selectedWord != null) {
                        inputField.setSelectedItem(selectedWord);
                    }
                }
            }
            });
            // Create a JScrollPane for the word list
            JScrollPane wordListPane = new JScrollPane(wordList);

            add(inputPanel, BorderLayout.NORTH);
            add(centerPanel, BorderLayout.CENTER);
            add(wordListPane, BorderLayout.WEST);
        }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchWord();
        }
    }

    private void searchWord() {
        // Get the input value and selected language from their respective JComboBoxes
        String input = Objects.requireNonNull(inputField.getSelectedItem()).toString().toLowerCase();
        // Search for the input word in the English word array
        boolean wordFound = false;
        int languageIndex = languageSelector.getSelectedIndex();
        // Loop through the English words array and compare the input word to each word in the array
        for (int i = 0; i < englishWords.length; i++) {
            if (englishWords[i].equalsIgnoreCase(input)) {
                wordFound = true;
                translationLabel.setText(translations[languageIndex][i]);

                try {
                    URL imageUrl = getClass().getResource("/images/" + imageFileNames[i]);
                    assert imageUrl != null;
                    BufferedImage image = ImageIO.read(imageUrl);

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