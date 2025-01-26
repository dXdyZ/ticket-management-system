package com.example.telegrambot.telegram_bot;

import com.example.telegrambot.entity.Task;
import com.example.telegrambot.entity.dto.CommentDTO;
import com.example.telegrambot.entity.dto.UserProfileDTO;

public class StringConverter {
    public static String convertTask(Task task) {
        return "Задача ожидает подтверждения\n" +
                "id: " + task.getId() + "\n" +
                "topic: " + task.getTopic() + "\n" +
                "description: " + task.getDescription() + "\n" +
                "priority: " + task.getPriority().toString() + "\n" +
                "create date: " + task.getCreateDate() + "\n" +
                "create user: " + task.getUsers().getUsername() + "\n" +
                "user willing to take on a task: " + task.getWorkUser().getUsername();
    }

    public static String convertProfileUser(UserProfileDTO userProfileDTO) {
        return "Пользователь\n" +
                "user name: " + userProfileDTO.getUsername() + "\n" +
                "email: " + userProfileDTO.getEmail() + "\n" +
                "create profile date: " + userProfileDTO.getCreateData();
    }

    public static String convertComment(CommentDTO commentDTO) {
        return "Коментарий к задаче\n" +
                "user name: " + commentDTO.getUsername() + "\n" +
                "comment: " + commentDTO.getCommentText() + "\n" +
                "create date: " + commentDTO.getCreateDate();
    }
}
