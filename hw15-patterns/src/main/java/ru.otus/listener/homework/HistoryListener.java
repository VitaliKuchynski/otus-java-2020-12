package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.ArrayList;
import java.util.List;

public class HistoryListener implements Listener {

    private List<String> messageList = new ArrayList<>();

    public List<String> getMessageList() {
        return messageList;
    }

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {

        messageList.add(oldMsg.toString() + " - " + newMsg.toString());
    }
}
