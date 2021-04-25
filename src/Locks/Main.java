package Locks;

/**
*<h1> Producator - Consumator </h1> 
* Clasa Main contine metoda "main" in care sunt instantiati producatorii si consumatorii. 
* <p>
* Aceasta clasa contine 4 variabile statice - finale ce reprezinta numarul
* de producatori (NR_PRODUCATORI), numarul de consumatori (NR_CONSUMATORI),
* limita inferioara si superioara a intervalului de numere/produse (MINN respectiv MAXN).
* <p>
* Clasa Main contine un obiect de tip PCLock ce reprezinta buffer-ul de elemente care
* va fi dat ca parametru tuturor firelor de executie (producatori si consumatori).
* @author  Brinzan Florinel Razvan
* @since   2019-12-07
*/

public class Main {

	static final int NR_PRODUCATORI = 10;
	static final int NR_CONSUMATORI = 5;
	static final int MINN = 0;
	static final int MAXN = 50;

	static Producator producatori[] = new Producator[NR_PRODUCATORI];
	static Consumator consumatori[] = new Consumator[NR_CONSUMATORI];
	static PCLock buffer = new PCLock();
	
	   /**
	   * In aceasta metoda sunt create thread-urile (producatorii si consumatorii) si este apelata metoda
	   *  "start()" ce pune in executie thread-urile. Fiecare producator/consumator primeste ca parametrii
	   *  numarul de producatori/consumatori, numarul sau de identificare, limita inferioara si superioara
	   *  a intervalului de numere(produse) si buffer-ul de numere(produse).
	   *  <p>
	   *  La final se afiseaza numarul de elemente produse de fiecare producator si numarul de elemente
	   *  consumate de fiecare consumator.
	   * @param args neutilizat.
	   */

	public static void main(String[] args) {

		int iterator;
		System.out.println("START --> Producator - Consumator");

		for (iterator = 0; iterator < NR_PRODUCATORI; iterator++) {
			producatori[iterator] = new Producator(NR_PRODUCATORI, iterator, MINN, MAXN, buffer);
			producatori[iterator].start();
		}

		for (iterator = 0; iterator < NR_CONSUMATORI;iterator++) {
			consumatori[iterator] = new Consumator(NR_CONSUMATORI, iterator, MINN, MAXN, buffer);
			consumatori[iterator].start();
		}
		for (iterator = 0; iterator < NR_PRODUCATORI; iterator++) {
			try {
				producatori[iterator].join();
			} catch (InterruptedException e) {
			}
		}
		for (iterator = 0; iterator < NR_CONSUMATORI; iterator++) {
			try {
				consumatori[iterator].join();
			} catch (InterruptedException e) {
			}
		}
		for (iterator = 0; iterator < NR_PRODUCATORI; iterator++) {
			System.out.println("Producatorul " + (iterator+1) + " a produs " + producatori[iterator].getCounter() + " elemente.");
		}
		for (iterator = 0; iterator < NR_CONSUMATORI; iterator++) {
			System.out.println("Consumatorul " + (iterator+1) + " a consumat " + consumatori[iterator].getCounter() + " elemente.");
		}
		System.out.println("STOP --> Producator - Consumator");

	}
}