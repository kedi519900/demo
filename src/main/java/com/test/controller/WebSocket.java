package com.test.controller;


import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket")
@Component
public class WebSocket {
    //用来存放每个客户端对应的MyWebSocket对象
    private static CopyOnWriteArraySet<WebSocket> user = new CopyOnWriteArraySet<WebSocket>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    @OnMessage
    public void onMessage(String message,Session session) throws IOException {
        //群发消息
        for (WebSocket myWebSocket : user) {
            System.out.println(session);
            myWebSocket.session.getBasicRemote().sendText(message);
            //myWebSocket.session.getBasicRemote().sendText("<img src=''/>");
        }
    }
    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println(session.getId()+" open...");
        this.session = session;
        user.add(this);
        for (WebSocket myWebSocket : user) {
            System.out.println(session);
            myWebSocket.session.getBasicRemote().sendText("当前登录人数"+user.size()+"人");
            //myWebSocket.session.getBasicRemote().sendText("<img src=''/>");
        }
    }
    @OnClose
    public void onClose(){
        System.out.println(this.session.getId()+" close...");
        user.remove(this);
    }

    @OnError
    public void onError(Session session,Throwable error){
        System.out.println(this.session.getId()+" error...");
        error.printStackTrace();
    }


}
