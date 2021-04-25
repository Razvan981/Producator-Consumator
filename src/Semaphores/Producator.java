package Semaphores;

/**
*<h1> Producator - Consumator </h1> 
* Clasa Producator extinde clasa Thread si suprascrie metoda "run()".
* <P>
* Aceasta clasa are ca membrii de tip data elementele ce vor fi 
* initializate cu valorile date ca parametri in constructor.
* <P>
* Clasa contine un constructor cu parametri, o metoda prin care se
* returneaza numarul de elemente produse si metoda run() ce apeleaza
* metoda append(int) din clasa PCSemafor pentru a adauga elemente in buffer.
* <p>
* @author  Brinzan Florinel Razvan
* @since   2019-12-07
*/

public class Producator extends Thread {
	
	//membri de tip data
	private int nr_fireP;
	private int nr_fir;
	private int max; 
	private int min;
	private int nr_elemente_produse = 0;
	private PCSemafor buffer;
	
	  /**
	   * Constructorul clasei Producator. Initializeaza membrii de tip data
	   * ai clasei cu valorile date ca parametri.
	   * @param nr_fireP numarul de producatori
	   * @param nr_fir numarul producatorului
	   * @param min valoarea minima a intervalului in care se genereaza numere
	   * @param max valoarea maxima a intervalului in care se genereaza numere
	   * @param buffer buffer-ul de numere (produse)
	  */

	public Producator(int nr_fireP, int nr_fir, int min, int max, PCSemafor buffer) {
		
		this.nr_fireP = nr_fireP;
		this.nr_fir = nr_fir;
		this.min = min;
		this.max = max;
		this.buffer = buffer;
	}
	
	/**
	 * Metoda getCounter() returneaza numarul de elemente produse de firul curent.
	 * @return numarul de elemente produse
	 */

	public int getCounter() {
		return nr_elemente_produse;
	}
	
    /**
	   * Aceasta metoda suprascrie metoda "run()" din clasa Thread. Metoda "run()" din clasa
	   * Producator are rolul de a genera elementul (un nr intreg din intervalul [min, max) ) si
	   * a-l introduce in buffer prin intermediul metodei append(int) din clasa PCSemafor.
	   * <p>
	   * La final se incrementeaza numarul de elemente produse de firul curent, se afiseaza 
	   * numarul producatorului si numarul elementului adaugat in buffer.
	   */

	public void run() {
		int element = min;
		while ((element % nr_fireP) != nr_fir) {
			element++;
		} // determina primul numar produs
		while (element < max) {
			buffer.append(element);
			nr_elemente_produse++;
			System.out.println("Producatorul " + (nr_fir+1) + " a produs elementul " + element);
			element = element + nr_fireP;
		}
	}
}
