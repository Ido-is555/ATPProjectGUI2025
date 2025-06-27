package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;

public interface IView {
    void bindViewModel(MyViewModel viewModel);

    void displayMaze();
    void displaySolution();
    void displayVictory();
    void showError(String message);
}
