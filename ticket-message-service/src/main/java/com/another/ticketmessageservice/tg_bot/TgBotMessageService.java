package com.another.ticketmessageservice.tg_bot;

import org.springframework.stereotype.Service;

@Service
public class TgBotMessageService {

    public void startCommand(String charId, String username) {
        String text = "Welcome to the bot \n" + username +
                "Here you can confirm the acceptance of the application for work \n" +
                "Commands to control the bot";
    }
}
