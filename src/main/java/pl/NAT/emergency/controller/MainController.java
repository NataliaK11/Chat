package pl.NAT.emergency.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.NAT.emergency.model.ChatSocket;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {

        try {
            ChatSocket.removeUser();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "index";
    }
}
