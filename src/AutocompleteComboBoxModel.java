import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class AutocompleteComboBoxModel extends DefaultComboBoxModel<String> {
    private final List<String> data;
    private boolean showingDropdown;

    public AutocompleteComboBoxModel(String[] words) {
        data = new ArrayList<>(Arrays.asList(words));
        showingDropdown = false;
    }

    public void updateSuggestions(String prefix) {
        removeAllElements();

        for (String word : data) {
            if (word.toLowerCase().startsWith(prefix.toLowerCase())) {
                addElement(word);
            }
        }
    }

    @Override
    public int getSize() {
        if (showingDropdown) {
            return data.size();
        } else {
            return super.getSize();
        }
    }

    public void setShowingDropdown(boolean showingDropdown) {
        this.showingDropdown = showingDropdown;
    }
}