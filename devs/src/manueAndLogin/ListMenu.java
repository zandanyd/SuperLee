package manueAndLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ListMenu<E extends Object> extends JList<E> {

    private final DefaultListModel model;
    private int selectedIndex = -1;

    private EventMenuButton eventMenuButton;

    public void setEventMenu(EventMenuButton eventMenuButton){
        this.eventMenuButton = eventMenuButton;
    }

    public ListMenu() {
        model = new DefaultListModel();
        setModel(model);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) { // add action of any menu button!
                if (SwingUtilities.isLeftMouseButton(me)) {
                    int index = locationToIndex(me.getPoint());
                    Object o = model.getElementAt(index);
                    if (o instanceof Model_Menu) {
                        Model_Menu menu = (Model_Menu) o;
                        if (menu.getType() == Model_Menu.MenuType.MENU) {
                            selectedIndex = index;
                        }
//                        if(menu.getName().equals("New Transport Document")){
//                            System.out.println("YESSSS");
//                        }
                        if(eventMenuButton != null){
                            eventMenuButton.press(index);
                        }
                    } else {
                        selectedIndex = index;
                    }

                    repaint();
                }
            }
        });

    }

    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int index, boolean selected, boolean focus) {
                Model_Menu data;
                if (o instanceof Model_Menu) {
                    data = (Model_Menu) o;
                } else {
                    data = new Model_Menu("", o + "", Model_Menu.MenuType.EMPTY);
                }
                ManulItem item = new ManulItem(data);
                item.setSelected(selectedIndex == index);
                return item;
            }

        };
    }

    public void addItem(Model_Menu data) {
        model.addElement(data);
    }
}