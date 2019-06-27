package pl.NAT.emergency.model;

import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
public class User {
    private String nickname;
    private WebSocketSession webSocketSession;
    private LocalDateTime lastDataTime;

    public User(WebSocketSession webSocketSession) {

        this.webSocketSession = webSocketSession;

    }

    public void sendMessage(TextMessage test) throws Exception{
        webSocketSession.sendMessage(test);
    }
}
