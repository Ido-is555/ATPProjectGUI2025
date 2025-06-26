//package com.example.atpprojectgui;
//
//import Model.MyModel;
//import View.SceneManager;
//import ViewModel.MyViewModel;
//import javafx.application.Application;
//import javafx.stage.Stage;
//
//public class HelloApplication extends Application {
//
//    @Override
//    public void start(Stage stage) {
//        // יצירת Model & ViewModel יחידים לאפליקציה
//        MyModel model = new MyModel();
//        MyViewModel viewModel = new MyViewModel(model);
//
//        // SceneManager מחזיק את ה-Stage וה-ViewModel
//        SceneManager.init(stage, viewModel);
//        SceneManager.switchTo("/fxml/StartScreen.fxml");
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}
