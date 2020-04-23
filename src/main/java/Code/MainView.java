package Code;

import Code.Domain.Message;
import Code.Repository.MessageRepository;
import Code.components.MessageEditor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;

@Route
public class MainView extends VerticalLayout {
    private final MessageRepository messageRepository;

    private Grid<Message> messagesGrid = new Grid<>(Message.class);

    final TextField filter= new TextField("", "Type to filter");
    private final Button addNewBtn = new Button("Add new");
    private final HorizontalLayout toolBar = new HorizontalLayout(filter, addNewBtn);

    private final MessageEditor editor;

    public MainView(MessageRepository messageRepository, MessageEditor editor) {
        this.messageRepository = messageRepository;
        this.editor = editor;

        add(toolBar, messagesGrid, editor);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> fillList(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        messagesGrid.asSingleSelect().addValueChangeListener(e -> {
            editor.editMessage(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editMessage(new Message()));

        // Listen changes made by the editor, refresh data from backend
        /*editor.setChangeHandler(() -> {
            editor.setVisible(false);
            fillList(filter.getValue());
        });*/

       // add(new Text("Welcome to MainView."));
    }

    private void fillList(String name) {
        if (name.isEmpty()) {
            messagesGrid.setItems(this.messageRepository.findAll());
        } else {
           // messagesList.setItems(this.employeeRepo.findByName(name));
        }
    }

}
