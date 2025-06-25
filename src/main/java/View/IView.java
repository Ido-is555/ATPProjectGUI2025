package View;

import ViewModel.MyViewModel;

public interface IView {
    // --- bind the ViewModel to the View ---
    void bindViewModel(MyViewModel viewModel);

    // --- display the maze on screen ---
    void displayMaze();

    // --- display the solution path on the maze ---
    void displaySolution();

    // --- display the victory screen when the player reaches the goal ---
    void displayVictory();

    // --- show an error dialog or message to the user ---
    void showError(String message);
}
