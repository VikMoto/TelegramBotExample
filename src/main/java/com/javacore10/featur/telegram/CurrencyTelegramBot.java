package com.javacore10.featur.telegram;

import com.javacore10.featur.currency.CurrencyService;
import com.javacore10.featur.currency.PrivatBankCurrencyService;
import com.javacore10.featur.currency.dto.Currency;
import com.javacore10.featur.telegram.command.HelpCommand;
import com.javacore10.featur.telegram.command.StartCommand;
import com.javacore10.featur.ui.PrettyPrintCurrencyServise;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

    private CurrencyService currencyService;
    private PrettyPrintCurrencyServise prettyPrintCurrencyServise;
    public CurrencyTelegramBot() {
        currencyService = new PrivatBankCurrencyService();
        prettyPrintCurrencyServise = new PrettyPrintCurrencyServise();

        register(new StartCommand());
        register(new HelpCommand());
    }

    @Override
    public String getBotUsername() {
        return BotConstants.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConstants.BOT_TOKEN;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }



    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @SneakyThrows
    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            String callbackQuery = update.getCallbackQuery().getData();
            Currency currencyQuery = Currency.valueOf(callbackQuery);


            String convertText = prettyPrintCurrencyServise.convert(
                    currencyService.getRate(currencyQuery), currencyQuery);

            SendMessage responseMessage = new SendMessage();
            responseMessage.setText(convertText);
            //
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            responseMessage.setChatId(Long.toString(chatId));
            execute(responseMessage); //@SneakyThrows instead try/Catch block
            System.out.println("callbackQuery = " + callbackQuery);
        }
        if (update.hasMessage()){
            String message = update.getMessage().getText();
            String responseText = "You wrote - " + message;

            SendMessage messageText = new SendMessage();
            messageText.setText(responseText);
            messageText.setChatId(Long.toString(update.getMessage().getChatId()));
            execute(messageText);//@SneakyThrows instead try/Catch block

        }

//        System.out.println("Non-command here!");
    }

    @Override
    public void processInvalidCommandUpdate(Update update) {
        super.processInvalidCommandUpdate(update);
    }

    @Override
    public boolean filter(Message message) {
        return super.filter(message);
    }
}
