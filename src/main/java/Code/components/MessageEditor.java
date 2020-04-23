package Code.components;

import Code.Domain.Message;
import Code.Repository.MessageRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.awt.*;

@SpringComponent
@UIScope
public class MessageEditor extends VerticalLayout implements KeyNotifier {
    private final MessageRepository messageRepository;

    private Message message;

    private TextField userName = new TextField("name");
    private TextField email = new TextField("email");
    private TextField homepage = new TextField("homepage");
    private TextField text = new TextField("text");

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<Message> binder = new Binder<>(Message.class);

    private ChangeHandler changeHandler;


    public interface ChangeHandler{
        void onChange();
    }

    public MessageEditor(MessageRepository messageRepository){
        this.messageRepository = messageRepository;

        add(userName, email, homepage, text, actions);
        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editMessage(message));
        setVisible(false);
    }

    public void delete() {
        changeHandler.onChange();
    }

    public void save() {
        messageRepository.save(message);
        changeHandler.onChange();
    }

    public void editMessage(Message message){
        if (message == null){
            setVisible(false);
            return;
        }
    }
}
