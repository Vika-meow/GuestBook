package Code.Controller;

import Code.Domain.Message;
import Code.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Map;

@Controller
public class MessageController {
    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/")
    public String helloPage(Map<String, Object> model){
        return "helloPage";
    }

    @PostMapping("/")
    public String add(@RequestParam Map<String,String> allParams, Map<String, Object> model){
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        Message message = new Message(allParams.get("name"), allParams.get("email"),
                allParams.get("text"), sqlDate, "123.11.11.11", "1293");
        if(allParams.containsKey("homepage"))
            message.setHomepage(allParams.get("homepage"));

        messageRepository.save(message);

        return "helloPage";
    }
}
