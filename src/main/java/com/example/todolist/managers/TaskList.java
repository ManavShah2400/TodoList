package com.example.todolist.managers;

import com.example.todolist.dto.TaskDTO;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "src/task.bin";
    private static List<TaskDTO> tasks = new ArrayList<>();

    public TaskList(){
        loadTasks();
    }


    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void addTask(TaskDTO task){
        tasks.add(task);
        sortTasksByStatus();
        saveTasks();
    }

    public void removeTask(TaskDTO task){
        tasks.remove(task);
        saveTasks();
    }

    public TaskDTO getTaskById(String id){
        for(TaskDTO currentTask: tasks){
            if(currentTask.getId().equals(id)){
                return currentTask;
            }
        }
        return null;
    }

    public void updateTask(TaskDTO updatedTask){
        for(int i = 0; i< tasks.size(); i++){
            TaskDTO currentTask = tasks.get(i);
            if(currentTask.getId().equals(updatedTask.getId())){
                currentTask.setTitle(updatedTask.getTitle());
                currentTask.setDescription(updatedTask.getDescription());
                currentTask.setStatus(updatedTask.getStatus());

                currentTask.setComments(updatedTask.getComments());

                break;
            }
        }

        sortTasksByStatus();
        saveTasks();
    }

    private void sortTasksByStatus(){
        tasks.sort(Comparator.comparingInt(
                task -> {
                    switch (task.getStatus()){
                        case "ToDo": return 1;
                        case "InProgress": return 2;
                        case "Done": return 3;
                        default: return 4;
                    }
                }

        ));
    }

    private void saveTasks(){
        try(ObjectOutputStream oos
                    = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))){
            oos.writeObject(tasks);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    private void loadTasks(){
        File file = new File(FILE_PATH);
        if(file.exists()){
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                tasks = (List<TaskDTO>) ois.readObject();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            saveTasks();
        }
    }

}