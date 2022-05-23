import model.abilities.AreaOfEffect;

public class test {
	public static void main(String[] args) {
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(i != 0 || j != 0) {
					System.out.print('.');
				}else {
					System.out.print(' ');
				}
			}
			System.out.println();
		}
	}
}
