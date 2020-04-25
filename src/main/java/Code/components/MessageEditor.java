package Code.components;

import Code.Domain.Message;
import Code.Repository.MessageRepository;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.WebBrowser;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringComponent
@UIScope
public class MessageEditor extends VerticalLayout implements KeyNotifier {

    private final MessageRepository messageRepository;
    private Message message;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    Matcher matcher;

    WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();

    private TextField userName = new TextField("Enter Name");
    private TextField email = new TextField("email");
    private TextField homepage = new TextField("Enter Homepage");
    private TextArea text = new TextArea("Enter your message");

    private Button save = new Button("Send", VaadinIcon.CHECK.create());
    private HorizontalLayout actions = new HorizontalLayout(save);

    HorizontalLayout input = new HorizontalLayout();

    private Binder<Message> binder = new Binder<>(Message.class);

    private ChangeHandler changeHandler;

    private Captcha captcha = new Captcha();

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }


    public interface ChangeHandler{
        void onChange();
    }

    public MessageEditor(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
        buttonSettings();

        input.add(userName, email, homepage);

        add(input, new HorizontalLayout(text, captcha), actions);


        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");

        save.addClickListener(e -> {
            if (checkValid()) {
                save();
            }
        });

        setVisible(true);
    }

    private boolean checkValid(){
        matcher = emailPattern.matcher(email.getValue());
        if(!matcher.matches()){
            email.setInvalid(true);
            return false;
        } else {
            email.setInvalid(false);
        }

        if((userName.isInvalid()) ||
                (text.isInvalid()) || (!captcha.isValid())) {
            return false;
        }

        return true;
    }

    public void save() {
        message = new Message();
        message.setDate(new Date(System.currentTimeMillis()));
        message.setIp(webBrowser.getAddress());
        message.setBrowserVesrion(webBrowser.getBrowserApplication());
        message.setUserName(userName.getValue());
        message.setEmail(email.getValue());
        message.setText(text.getValue());
        message.setHomepage(homepage.getValue());

        messageRepository.save(message);
        userName.focus();
        //changeHandler.onChange();
        refresh();
    }

    private void refresh(){
        userName.setValue("");
        email.setValue("");
        homepage.setValue("");
        text.setValue("");
        captcha.refresh();
        changeHandler.onChange();

    }

    private void buttonSettings(){
        userName.setRequired(true);
        userName.setAutofocus(true);
        userName.setErrorMessage("Please Enter your name");


        email.setRequired(true);
        email.setPattern(EMAIL_PATTERN);
        email.setErrorMessage("Please enter your email");

        text.setRequired(true);
        text.setPlaceholder("Write here");
        text.getStyle().set("maxlength", "1024");
        text.setErrorMessage("Please write your message here");

    }
}
