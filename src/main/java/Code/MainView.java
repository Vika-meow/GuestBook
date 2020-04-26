package Code;

import Code.Domain.Message;
import Code.Repository.MessageRepository;
import Code.components.MessageEditor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;


@Route
public class MainView extends VerticalLayout {
    private MessageRepository messageRepository;
    private Grid<Message> messagesGrid = new Grid<>(Message.class);

    private final MessageEditor editor;


    public MainView(MessageRepository messageRepository, MessageEditor editor) {
        this.messageRepository = messageRepository;
        this.editor = editor;

        messagesGrid.setColumns("userName", "email", "homepage", "text", "date");
        messagesGrid.setHeight("5");

        add(editor);
        //Test();
        MyGrid myGrid = new MyGrid();
        add(myGrid);

        this.editor.setChangeHandler(() -> {
            myGrid.refresh();
        });

        showMessages();

    }

    private void showMessages(){
        messagesGrid.setItems(messageRepository.findAll());

    }

    private class MyGrid extends VerticalLayout{
        private Button first;
        private Button prev;
        private Button next;
        private Label currentPageLabel;
        private HorizontalLayout buttons;

        private int currentPage = 1;
        private int numberOfLists = 1;
        private int pageSize = 25;

        MyGrid(){
            numberOfLists = messageRepository.findAll().size() / pageSize;

            if(messageRepository.findAll().size() % pageSize != 0){
                numberOfLists++;
            }

            buttons = new HorizontalLayout();
            first = new Button("1");

            first.addClickListener(e -> {
                if(currentPage != 1) {
                    currentPage = 1;
                    refresh();
                }
            });

            prev = new Button("<-");

            prev.addClickListener(e -> {
               if (currentPage > 1) {
                   currentPage--;
                   refresh();
               }
            });

            next = new Button("->");

            next.addClickListener(e ->{
               if(currentPage < numberOfLists) {
                   currentPage++;
                   refresh();
               }
            });

            currentPageLabel = new Label("Current Page: " + currentPage);

            buttons.add(first, prev, next, currentPageLabel);
            currentPage = 1;
            add(buttons);
            paintTable();

        }

        private void reCounting(){
            numberOfLists = messageRepository.findAll().size() / pageSize;
            if(messageRepository.findAll().size() % pageSize != 0){
                numberOfLists++;
            }
        }

        private void paintTable(){
            reCounting();

            List<Message> currentList = new ArrayList<>();

            Grid<Message> grid = new Grid(Message.class);
            grid.setColumns("userName", "email", "homepage", "text", "date");

            for (Grid.Column column : grid.getColumns()) {
                column.setSortable(false);
            }

            int max;
            if (currentPage*pageSize > messageRepository.findAll().size()){
                max = messageRepository.findAll().size();
            } else{
                max = currentPage*pageSize;
            }

            for (int i = (currentPage-1)*pageSize; i < max; i++) {
                Message it = messageRepository.findAll().get(i);
                currentList.add(it);
            }
            grid.setItems(currentList);
            add(grid);
        }

        public void refresh(){
            reCounting();
            removeAll();

            buttons.removeAll();
            currentPageLabel = new Label("Current page: " + currentPage);
            buttons.add(first, prev, next,currentPageLabel);
            add(buttons);

            paintTable();
        }
    }
}
