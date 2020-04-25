package Code;

import Code.Domain.Message;
import Code.Repository.MessageRepository;
import Code.components.MessageEditor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.Route;


@Route
public class MainView extends VerticalLayout {
    private MessageRepository messageRepository;
    private Grid<Message> messagesGrid = new Grid<>(Message.class);
    private final MessageEditor editor;

    public MainView(MessageRepository messageRepository, MessageEditor editor) {
        this.messageRepository = messageRepository;
        this.editor = editor;

        add(editor, messagesGrid);

        this.editor.setChangeHandler(() -> {
            showMessages();
        });

        showMessages();

    }

    private void showMessages(){
        messagesGrid.setItems(messageRepository.findAll());
        messagesGrid.setColumns("userName", "email", "homepage", "text", "date");
        messagesGrid.setPageSize(25);
    }


}
