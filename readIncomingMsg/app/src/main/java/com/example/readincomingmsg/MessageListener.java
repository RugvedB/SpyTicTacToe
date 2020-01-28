package com.example.readincomingmsg;


public interface MessageListener {

    void messageReceived(String senderPhoneNumber,String emailFrom,String emailBody,String msgBody,long timeStamp,String Message);
}
