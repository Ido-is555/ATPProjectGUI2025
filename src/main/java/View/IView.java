package View;

import ViewModel.MyViewModel;

public interface IView {
    void bindViewModel(MyViewModel viewModel);

    void displayMaze();
    void displaySolution();
    void displayVictory();
    void showError(String message);
}
