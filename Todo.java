
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;

public class Todo {

	public static void main(String args[]) throws IOException {
		List<String> todoList = new ArrayList<String>();
		if (args.length == 0) {
			help();
		} else {
			switch (args[0]) {
			case "add":
				if (args.length == 1) {
					System.out.print("Error: Missing todo string. Nothing added!");
				} else {
					todoList.add(args[1]);
					writeTodoList(todoList);
					System.out.println("Added todo: \"" + args[1] + "\"");
				}
				break;
			case "help":
				help();
				break;
			case "done":
				if (args.length == 1) {
					System.out.print("Error: Missing NUMBER for marking todo as done.");
				} else {
					todoList = getTodoList();
					new FileWriter("todo.txt", false).close();
					int pos = Integer.parseInt(args[1]) - 1;
					if (pos < 0 || pos >= todoList.size()) {
						System.out.print("Error: todo #0 does not exist.");
					} else {
						while(todoList.get(pos).equals("done")) {
							pos++;
						}
						todoList.set(pos, "done");
						writeTodoList(todoList);
						System.out.print("Marked todo #" + Integer.parseInt(args[1]) + " as done.");
					}
				}
				break;
			case "del":
				if (args.length == 1) {
					System.out.print("Error: Missing NUMBER for deleting todo.");
				} else {
					todoList = getTodoList();
					new FileWriter("todo.txt", false).close();
					int index = Integer.parseInt(args[1]) - 1;
					if (index < 0 || index >= todoList.size()) {
						System.out.print("Error: todo #" + args[1] + " does not exist. Nothing deleted.");
					} else {
						while(todoList.get(index).equals("done")) {
							index++;
						}
						todoList.remove(index);
						writeTodoList(todoList);
						System.out.println("Deleted todo #" + Integer.parseInt(args[1]));
					}
				}
				break;
			case "report":
				int pending = reportPending();
				int done = reportDone();
				if (pending == 0) {
					System.out.print("There are no pending todos!");
				} else {
					System.out.print(java.time.LocalDate.now() + " Pending : " + pending + " Completed : " + done);
				}
				break;
			case "ls":
				try {
					todoList = getTodoList();
					if (todoList.size() == 0) {
						System.out.print("There are no pending todos!");
					} else {
						int i = reportPending();
						Collections.reverse(todoList);
						for (String s : todoList) {
							if (!s.equals("done")) {
								System.out.print("[" + (i--) + "] " + s + "\n");
							}
						}
					}
				} catch (IOException e) {
					System.out.print("There are no pending todos!");
				}
				break;
			default:
				help();
				break;
			}
		}
	}

	private static List<String> getTodoList() throws IOException {
		FileReader fin = new FileReader("todo.txt");
		BufferedReader bin = new BufferedReader(fin);
		List<String> todoList = bin.lines().collect(Collectors.toList());
		bin.close();
		fin.close();
		return todoList;
	}

	private static void writeTodoList(List<String> todoList) throws IOException {

		FileWriter fout = new FileWriter("todo.txt", true);
		BufferedWriter bout = new BufferedWriter(fout);
		for (String str : todoList) {
			bout.write(str);
			bout.write("\n");
		}
		bout.close();
		fout.close();
	}

	private static int reportDone() throws IOException {
		List<String> todoList = getTodoList();
		int done = 0;
		for (String s : todoList) {
			if (s.equals("done")) {
				done++;
			}
		}
		return done;
	}

	private static int reportPending() throws IOException {
		List<String> todoList = getTodoList();
		int pending = 0;
		for (String s : todoList) {
			if (!s.equals("done")) {
				pending++;
			}
		}
		return pending;
	}

	private static void help() {
		System.out.print("StringContaining \"Usage :-\n");
		System.out.print("$ ./todo add \"todo item\"  # Add a new todo\n");
		System.out.print("$ ./todo ls               # Show remaining todos\n");
		System.out.print("$ ./todo del NUMBER       # Delete a todo\n");
		System.out.print("$ ./todo done NUMBER      # Complete a todo\n");
		System.out.print("$ ./todo help             # Show usage\n");
		System.out.print("$ ./todo report           # Statistics\"");
	}
}
