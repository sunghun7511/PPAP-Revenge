import java.util.Scanner;

public class FlagReader {
	public void run() {
		System.out.println("Input your flag : ");
		
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		sc.close();
		
		new FlagChecker(input).run();
	}
}
