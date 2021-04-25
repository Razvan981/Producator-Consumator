package Semaphores;

/**
*<h1> Producator - Consumator </h1> 
* Clasa Consumator extinde clasa Thread si suprascrie metoda "run()".
* <P>
* Aceasta clasa are ca membrii de tip data elementele ce vor fi 
* initializate cu valorile date ca parametri in constructor.
* <P>
* Clasa contine un constructor cu parametri, o metoda prin care se
* returneaza numarul de elemente consumate si metoda run() ce apeleaza
* metoda take() din clasa PCSemafor pentru a extrage elemente din buffer.
* <p>
* @author  Brinzan Florinel Razvan
* @since   2019-12-07
*/

public class Consumator extends Thread {
	
	//membri de tip data
	private int nr_fireC;
	private int nr_fir;
	private int max; 
	private int min;
	private int nr_elemente_consumate = 0;
	private PCSemafor buffer;

	  /**
	   * Constructorul clasei Consumator. Initializeaza membrii de tip data
	   * ai clasei cu valorile date ca parametri.
	   * @param nr_fireC numarul de consumatori
	   * @param nr_fir numarul consumatorului
	   * @param min valoarea minima a intervalului in care se genereaza/extrag numere
	   * @param max valoarea maxima a intervalului in care se genereaza/extrag  numere
	   * @param buffer buffer-ul de numere (produse)
	  */
	
	public Consumator(int nr_fireC, int nr_fir, int min, int max, PCSemafor buffer) {
		
		this.nr_fireC = nr_fireC;
		this.nr_fir = nr_fir;
		this.min = min;
		this.max = max;
		this.buffer = buffer;
	}
	
	/**
	 * Metoda getCounter() returneaza numarul de elemente consumate de firul curent.
	 * @return numarul de elemente consumate
	 */

	public int getCounter() {
		return nr_elemente_consumate;
	}
	
    /**
	   * Aceasta metoda suprascrie metoda "run()" din clasa Thread. Metoda "run()" din clasa
	   * Consumator are rolul de a extrage elemente din buffer prin intermediul metodei take()
	   * din clasa PCSemafor.
	   * <p>
	   * La final se incrementeaza numarul de elemente consumate de firul curent, se afiseaza 
	   * numarul consumatorului si numarul elementului consumat din buffer.
	   */

	public void run() {
		
		int iterator = min;
		int element;
		while ((iterator % nr_fireC) != nr_fir) {
			iterator++;
		}
		while (iterator < max) {
			
			element = buffer.take();
			nr_elemente_consumate++;
			System.out.println("Consumatorul " + (nr_fir+1) + " a consumat elementul " + element);
			iterator = iterator + nr_fireC;
		}
	}
}
