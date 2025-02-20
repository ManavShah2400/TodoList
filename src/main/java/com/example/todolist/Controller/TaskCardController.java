package com.example.todolist.Controller;

import com.example.todolist.dto.TaskDTO;
import com.example.todolist.managers.TaskList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskCardController {
    public Label taskName;
    public Label taskTimeStamp;
    public Label taskStatus;
    public String taskId;
    private final TaskList taskList = new TaskList();

    private TodoController mainController;

    public void handleViewTask(ActionEvent actionEvent) {
        System.out.println("Viewing task: " + taskName.getText());
        try{
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource(
                            "/com/example/todolist/task_view_dialog.fxml"));
            VBox dialogPane = loader.load();
            TaskDTO task = taskList.getTaskById(taskId);
            TaskViewDialogController dialogController = loader.getController();
            dialogController.setTaskDetails(task, this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(taskName.getText());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(dialogPane);
            String css = this.getClass().getResource("/com/example/todolist/viewTaskDialog.css").toExternalForm();
            scene.getStylesheets().add(css);

            dialogStage.setScene(scene);
            dialogStage.showAndWait();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setTaskDetails(String name,
                               LocalDateTime timeStamp,
                               String status, String id ,
                               TodoController controller){
        taskName.setText(name);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a, MM/dd/yyyy");
        taskTimeStamp.setText(timeStamp.format(formatter));
        taskStatus.setText(status);
        this.taskId = id;
        applyStatusColor(status);

        mainController = controller;
    }

    private void applyStatusColor(String status){
        switch (status){
            case "ToDo":
                taskStatus.setStyle("-fx-text-fill: grey;");
                break;
            case "InProgress":
                taskStatus.setStyle("-fx-text-fill: orange;");
                break;
            case "Done":
                taskStatus.setStyle("-fx-text-fill: green;");
                break;
            default:
                taskStatus.setStyle("-fx-text-fill: black;");
                break;
        }

    }

    public void updateTask(TaskDTO task){
        taskList.updateTask(task);
        taskName.setText(task.getTitle());
        taskStatus.setText(task.getStatus());
        applyStatusColor(task.getStatus());

        mainController.redrawTaskList();
    }

    public void deleteTask(TaskDTO task) {
        taskList.removeTask(task);
        mainController.redrawTaskList();
    }
}