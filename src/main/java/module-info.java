module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires MaterialFX;


    opens com.example.todolist.Controller to javafx.fxml;
    exports com.example.todolist;
}