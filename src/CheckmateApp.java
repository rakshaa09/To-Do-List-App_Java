import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

class Task extends JPanel {
    JLabel index;
    JTextField taskName;
    JCheckBox done;

    Color lightPink = new Color(255, 204, 204);
    Color green = new Color(188, 226, 158);
    Color taskBorder = new Color(233, 119, 119);

    private boolean checked;

    Task() {
        this.setPreferredSize(new Dimension(350, 50));
        this.setBackground(lightPink);
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        checked = false;

        index = new JLabel("");
        index.setPreferredSize(new Dimension(30, 30));
        index.setHorizontalAlignment(JLabel.CENTER);
        this.add(index, BorderLayout.WEST);

        taskName = new JTextField();
        taskName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        taskName.setBackground(lightPink);

        this.add(taskName, BorderLayout.CENTER);

        done = new JCheckBox("Done");
        done.setPreferredSize(new Dimension(100, 40));
        done.setFont(new Font("Sans-serif", Font.BOLD, 14));
        done.setBackground(lightPink);
        done.setFocusPainted(false);

        this.add(done, BorderLayout.EAST);
    }

    public void changeIndex(int num) {
        this.index.setText(num + "");
        this.revalidate();
    }

    public JCheckBox getDone() {
        return done;
    }

    public boolean getState() {
        return checked;
    }

    public void changeState() {
        this.setBackground(green);
        checked = true;
        revalidate();
    }
}

class TaskList extends JPanel {
    Color listBackground = new Color(255, 229, 204);

    TaskList() {
        GridLayout layout = new GridLayout(8, 1);
        layout.setVgap(10);
        this.setLayout(layout);
        this.setPreferredSize(new Dimension(380, 580));
        this.setBackground(listBackground);
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public void updateNumbers() {
        Component[] listItems = this.getComponents();
        for (int i = 0; i < listItems.length; i++) {
            if (listItems[i] instanceof Task) {
                ((Task) listItems[i]).changeIndex(i + 1);
            }
        }
    }

    public void removeCompletedTasks() {
        for (Component c : getComponents()) {
            if (c instanceof Task) {
                if (((Task) c).getState()) {
                    remove(c);
                    updateNumbers();
                }
            }
        }
    }

    public void clearTasks() {
        removeAll();
        updateNumbers();
    }
}

class Footer extends JPanel {
    JButton addTask;
    JButton clearTasks;

    Color buttonColor = new Color(255, 153, 153);
    Color footerBackground = new Color(255, 229, 204);

    Footer() {
        this.setPreferredSize(new Dimension(400, 60));
        this.setBackground(footerBackground);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        addTask = new JButton("Add Task");
        addTask.setPreferredSize(new Dimension(140, 40));
        addTask.setFont(new Font("Sans-serif", Font.BOLD, 16));
        addTask.setBackground(buttonColor);
        this.add(addTask);

        // Changed button label from "Clear Completed" to "Clear Tasks"
        clearTasks = new JButton("Clear Tasks");
        clearTasks.setPreferredSize(new Dimension(160, 40));
        clearTasks.setFont(new Font("Sans-serif", Font.BOLD, 16));
        clearTasks.setBackground(buttonColor);
        this.add(clearTasks);
    }

    public JButton getNewTask() {
        return addTask;
    }

    public JButton getClearTasks() {
        return clearTasks;
    }
}

class TitleBar extends JPanel {
    Color titleBackground = new Color(255, 229, 204);

    TitleBar() {
        this.setPreferredSize(new Dimension(400, 80));
        this.setBackground(titleBackground);
        JLabel titleText = new JLabel("CHECKMATE");
        titleText.setPreferredSize(new Dimension(300, 60));
        titleText.setFont(new Font("Sans-serif", Font.BOLD, 28));
        titleText.setHorizontalAlignment(JLabel.CENTER);
        this.add(titleText);
    }
}

class AppFrame extends JFrame {
    private TitleBar titleBar;
    private Footer footer;
    private TaskList taskList;
    private JComboBox<String> listSelector;
    private DefaultComboBoxModel<String> listModel;

    AppFrame() {
        this.setSize(600, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLayout(new BorderLayout(20, 20));

        titleBar = new TitleBar();
        footer = new Footer();
        taskList = new TaskList();
        listModel = new DefaultComboBoxModel<>();
        listSelector = new JComboBox<>(listModel);
        listSelector.setPreferredSize(new Dimension(200, 30));
        listSelector.setEditable(true);

        JPanel listPanel = new JPanel();
        listPanel.add(new JLabel("Select List:"));
        listPanel.add(listSelector);

        // Create button panel for adding and deleting lists
        JPanel buttonPanel = new JPanel();
        JButton addListButton = new JButton("Add List");
        JButton deleteListButton = new JButton("Delete List");

        buttonPanel.add(addListButton);
        buttonPanel.add(deleteListButton);

        this.add(titleBar, BorderLayout.NORTH);
        this.add(footer, BorderLayout.SOUTH);
        this.add(taskList, BorderLayout.CENTER);
        this.add(listPanel, BorderLayout.WEST);
        this.add(buttonPanel, BorderLayout.EAST);

        addListeners(addListButton, deleteListButton);
    }

    public void addListeners(JButton addListButton, JButton deleteListButton) {
        footer.getNewTask().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String selectedList = (String) listSelector.getSelectedItem();
                if (selectedList != null && !selectedList.isEmpty()) {
                    Task task = new Task();
                    taskList.add(task);
                    taskList.updateNumbers();

                    task.getDone().addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            task.changeState();
                            taskList.updateNumbers();
                            revalidate();
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a list.");
                }
            }
        });

        footer.getClearTasks().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                taskList.removeCompletedTasks();
                repaint();
            }
        });

        // Add list functionality
        addListButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String newList = JOptionPane.showInputDialog("Enter List Name:");
                if (newList != null && !newList.isEmpty()) {
                    listModel.addElement(newList);
                    listSelector.setSelectedItem(newList); // Set the new list as selected
                }
            }
        });

        // Delete list functionality
        deleteListButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String selectedList = (String) listSelector.getSelectedItem();
                if (selectedList != null && !selectedList.isEmpty()) {
                    listModel.removeElement(selectedList);
                    taskList.clearTasks(); // Clear the tasks when the list is deleted
                    listSelector.setSelectedItem(null); // Deselect the list
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a list to delete.");
                }
            }
        });
    }
}

public class CheckmateApp {
    public static void main(String args[]) {
        new AppFrame(); // Start the application
    }
}
