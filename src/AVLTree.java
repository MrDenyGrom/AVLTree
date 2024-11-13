import java.util.*;

class AVLTree {

    static class Node {
        int key;
        Node left, right;
        int height;

        Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

    private Node root;

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    public void insert(int key) {
        root = insertRecursive(root, key);
    }

    private Node insertRecursive(Node node, int key) {
        if (node == null)
            return new Node(key);

        if (key < node.key) {
            node.left = insertRecursive(node.left, key);
        } else if (key > node.key) {
            node.right = insertRecursive(node.right, key);
        } else {
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && key < node.left.key) {
            return rightRotate(node);
        }

        if (balance < -1 && key > node.right.key) {
            return leftRotate(node);
        }

        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public void delete(int key) {
        root = deleteRecursive(root, key);
    }

    private Node deleteRecursive(Node root, int key) {
        if (root == null) {
            return root;
        }

        if (key < root.key) {
            root.left = deleteRecursive(root.left, key);
        } else if (key > root.key) {
            root.right = deleteRecursive(root.right, key);
        } else {
            if (root.left == null || root.right == null) {
                Node temp = root.left != null ? root.left : root.right;
                root = temp;
            } else {
                Node temp = minNode(root.right);
                root.key = temp.key;
                root.right = deleteRecursive(root.right, temp.key);
            }
        }

        if (root == null) {
            return root;
        }

        root.height = Math.max(height(root.left), height(root.right)) + 1;

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }

        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }

        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private Node minNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public void printTree() {
        printTree(root, "", true);
    }

    private void printTree(Node node, String indent, boolean isRight) {
        if (node != null) {
            System.out.println(indent + (isRight ? "└── " : "├── ") + node.key);
            printTree(node.left, indent + (isRight ? "    " : "│   "), false);
            printTree(node.right, indent + (isRight ? "    " : "│   "), true);
        }
    }

    public boolean search(int key) {
        return searchRecursive(root, key, 0) != -1;
    }

    private int searchRecursive(Node node, int key, int steps) {
        if (node == null) {
            return -1;
        }

        if (node.key == key) {
            System.out.println("Количество шагов: " + steps);
            return steps;
        }

        if (key < node.key) {
            return searchRecursive(node.left, key, steps + 1);
        } else {
            return searchRecursive(node.right, key, steps + 1);
        }
    }

    public void generateRandomTree(int count) {
        Random random = new Random();
        Set<Integer> generatedNumbers = new HashSet<>();

        while (generatedNumbers.size() < count) {
            generatedNumbers.add(random.nextInt(100));
        }

        for (int num : generatedNumbers) {
            insert(num);
        }

        System.out.println(count + " уникальных случайных чисел добавлено в дерево.");
    }

    public void cyclicDeleteOddNodes() {
        while (root != null && root.left != null) {
            List<Node> queue = new LinkedList<>();
            Queue<Node> q = new LinkedList<>();
            q.add(root);

            while (!q.isEmpty()) {
                Node current = q.poll();
                queue.add(current);

                if (current.left != null) q.add(current.left);
                if (current.right != null) q.add(current.right);
            }

            List<Node> toDelete = new ArrayList<>();
            for (int i = 0; i < queue.size(); i++) {
                if (i % 2 == 0) {
                    toDelete.add(queue.get(i));
                }
            }

            for (Node node : toDelete) {
                delete(node.key);
            }

            System.out.println("Дерево после удаления нечётных узлов:");
            printTree();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AVLTree tree = new AVLTree();

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Вставить элемент вручную");
            System.out.println("2. Сгенерировать уникальные случайные числа и добавить в дерево");
            System.out.println("3. Вывести дерево");
            System.out.println("4. Удалить элемент вручную");
            System.out.println("5. Поиск элемента с подсчётом шагов (обход в ширину)");
            System.out.println("6. Циклическое удаление нечётных узлов");
            System.out.println("7. Выход");
            System.out.print("Ваш выбор: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Введите число для вставки: ");
                    int value = scanner.nextInt();
                    tree.insert(value);
                    System.out.println("Число " + value + " добавлено в дерево.");
                    break;
                case 2:
                    System.out.print("Введите количество уникальных случайных чисел: ");
                    int count = scanner.nextInt();
                    tree.generateRandomTree(count);
                    break;
                case 3:
                    System.out.println("Дерево:");
                    tree.printTree();
                    break;
                case 4:
                    System.out.print("Введите число для удаления: ");
                    int deleteValue = scanner.nextInt();
                    tree.delete(deleteValue);
                    System.out.println("Число " + deleteValue + " удалено из дерева.");
                    break;
                case 5:
                    System.out.print("Введите число для поиска: ");
                    int searchValue = scanner.nextInt();
                    if (!tree.search(searchValue)) {
                        System.out.println("Число " + searchValue + " не найдено в дереве.");
                    }
                    break;
                case 6:
                    System.out.println("Циклическое удаление нечётных узлов...");
                    tree.cyclicDeleteOddNodes();
                    break;
                case 7:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Некорректный выбор.");
            }
        }
    }
}
