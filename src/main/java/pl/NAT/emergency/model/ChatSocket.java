package pl.NAT.emergency.model;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@EnableWebSocket
@Component
public class ChatSocket extends TextWebSocketHandler implements WebSocketConfigurer {

    private static List<User> sessions = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(this, "/chat").setAllowedOrigins("*");  // pozwala wszystkim łączyć się
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        sessions.add(new User(session));

        sendWelcomeToAllWithoutMe(session);

    }

    private void sendWelcomeToAllWithoutMe(WebSocketSession session) throws Exception {
        for (User user : sessions) {
            if (!(user.getWebSocketSession().getId().equals(session.getId()))) {
                TextMessage infoMessages = new TextMessage("<server>Mamy nowego ziomeczka");
                user.sendMessage(infoMessages);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        User userWriter = findUserBySession(session);
        if (userWriter.getNickname() == null) {
            userWriter.setNickname(message.getPayload());

        }


        for (User user : sessions) {

            TextMessage newMessage = new TextMessage(userWriter.getNickname() + " ( "
                    + formatter.format(LocalDateTime.now())
                    + " ) " + message.getPayload());
            user.sendMessage(newMessage);
            user.setLastDataTime(LocalDateTime.now());

        }

        //removeUser();

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(findUserBySession(session));
        sayGoodByeToFriends(session);
    }

    private static void sayGoodByeToFriends(WebSocketSession session) throws Exception {
        for (User session1 : sessions) {
            if (!(session1.getWebSocketSession().getId().equals(session.getId()))) {
                TextMessage infoMessages = new TextMessage("<server>Ktoś się wyłączył");
                session1.sendMessage(infoMessages);
            }
        }
    }

    private User findUserBySession(WebSocketSession session) {
        User user = sessions.stream()
                .filter(s -> s.getWebSocketSession().getId().equals(session.getId()))
                .findAny()
                .orElseThrow(IllegalStateException::new);

        return user;

    }

    @Scheduled(fixedRate = 120000)
    public static void removeUser() throws Exception {
        for (User user : sessions) {
            Duration duration = Duration.between(user.getLastDataTime(), LocalDateTime.now());
            long diff = Math.abs(duration.toMinutes());
            if (diff > 1) {
                sessions.remove(user);
                sayGoodByeToFriends(user.getWebSocketSession());
            }
        }
    }
}
