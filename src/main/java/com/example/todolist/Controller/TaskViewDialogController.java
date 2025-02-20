package com.example.todolist.Controller;

import com.example.todolist.dto.TaskDTO;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TaskViewDialogController {
    public MFXTextField taskTitleField;
    public TextArea taskDescriptionField;
    public MFXComboBox statusComboBox;
    public MFXTextField commentField;
    public VBox commentList;

    private TaskDTO task;
    private TaskCardController mainController;

    public void setTaskDetails(TaskDTO task, TaskCardController mainController){
        this.task = task;
        this.mainController = mainController;

        taskTitleField.setText(task.getTitle());
        taskDescriptionField.setText(task.getDescription());
        statusComboBox.getItems().clear();
        statusComboBox.getItems().addAll("ToDo", "InProgress", "Done");

        Platform.runLater(() ->{
            statusComboBox.setValue(task.getStatus());
        });

        task.getComments().forEach(comment -> displayComment(comment) );

    }

    private void displayComment(String comment){
        Text commentLabel = new Text(comment);
        commentLabel.setStyle("-fx-padding: 3px;");
        commentList.getChildren().addFirst(commentLabel);
    }


    public void handleAddComment(ActionEvent actionEvent) {
        String comment = commentField.getText();
        if(!comment.isEmpty()){
            task.addComment(comment);
            displayComment(comment);
            commentField.clear();
        }

    }

    public void handleCancel(ActionEvent actionEvent) {
        closeDialog();
    }

    public void handleUpdate(ActionEvent actionEvent) {
        task.setTitle(taskTitleField.getText());
        task.setDescription(taskDescriptionField.getText());
        task.setStatus((String) statusComboBox.getValue());

        mainController.updateTask(task);
        closeDialog();
    }

    private void closeDialog(){
        Stage stage = (Stage) taskTitleField.getScene().getWindow();
        stage.close();
    }

    public void handleDelete(ActionEvent actionEvent) {
        mainController.deleteTask(task);
        closeDialog();
    }
}