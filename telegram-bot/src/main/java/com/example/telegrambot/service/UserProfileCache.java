package com.example.telegrambot.service;

import com.example.telegrambot.entity.dto.UserProfileDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class UserProfileCache {
    private final Map<Long, UserProfileDTO> userProfile = new ConcurrentHashMap<>();


    public void saveUserProfile(Long chatId, UserProfileDTO userProfileDTO) {
        userProfile.put(chatId, userProfileDTO);
    }

    public UserProfileDTO getUserProfileByChatId(Long chatId) {
        return userProfile.get(chatId);
    }

    public void removeUserProfileByChatId(Long chatId) {
        userProfile.remove(chatId);
    }
}
