package Semaphores;

import java.util.concurrent.Semaphore;

/**
*<h1> Producator - Consumator </h1> 
* Clasa PCSemafor detine cele doua semafoare prin intermediul carora producatorii pot adauga
* elemente in buffer (in limita spatiului disponibil - nu se poate suprascrie un element din buffer 
* daca acesta nu a fost inca consumat) iar consumtorii pot extrage elemente ( in limita stocului din
* buffer - nu se poate consuma un element ce a fost consumat anterior).
* <p>
* Un consumator nu poate extrage elemente din buffer daca acesta e "gol" (are doar elemente ce au fost 
* consumate de firul curent sau alte fire de executie) deoarece este blocat de "semafor_consumator",
* iar producatorul nu poate adauga elemente in buffer daca acesta este "plin" ( are doar elemente ce
* nu au fost consumate inca) fiindca este blocat de "semafor_producator".
* <p>
* @author  Brinzan Florinel Razvan
* @since   2019-12-07
*/

public class PCSemafor {
	
	//membri de tip data
	private final int CAPACITATE = 5;
	private volatile int oldest = 0;
	private volatile int newest = 0;
	private volatile int counter = 0;
	private int buffer[] = new int[CAPACITATE];
	private static Semaphore semafor_producator = new Semaphore(1); 
	private static Semaphore semafor_consumator = new Semaphore(0); 
	
	  /**
	   * Prin aceasta metoda Producatorul adauga elemente in buffer.
	   * Cat timp counter-ul este mai mic decat capacitatea buffer-ului
	   * se vor produce noi elemete (acestea vor suprascrie elemente deja
	   * consumate din buffer si counter-ul ce numara elementele neconsumate
	   * va creste). Daca counter-ul va avea valoarea capacitatii buffer-ului 
	   * atunci firul de executie va fi blocat pana se va apela metoda release().
	   * In momentul in care se adauga un nou element in buffer semaforul consumatorului
	   * primeste release().
	   * @param element elementul ce va fi adaugat in buffer
	  */
	
	public void append(int element) {
		while (counter == CAPACITATE) {
			try {
				semafor_producator.acquire();				
			} catch (InterruptedException e) { }
		}
		buffer[newest] = element;
		newest = (newest + 1) % CAPACITATE;
		counter++;
		semafor_consumator.release();
	}
	
	  /**
	   * Prin aceasta metoda Consumatorul extrage elemente din buffer.
	   * Cat timp counter-ul este mai mare decat 0 insemana ca exista elemente
	   * neconsumate in buffer, deci consumatorul le poate extrage ( incepand cu 
	   * cel mai vechi element introdus).Daca counter-ul va avea valoarea 0
	   * atunci firul de executie va fi blocat pana se va apela metoda release() 
	   * pentru semafor_consumator.
	   * In momentul in care se extrage un element din buffer semaforul producatorului
	   * primeste release() - elibereaza permis-ul, returnandu-l semaforului.
	   * @return elementul extras din buffer de catre consumator
	  */

	public int take() {
		int element;
		while (counter == 0) {
			try {
				semafor_consumator.acquire();	
			} catch (InterruptedException e) { }
		}
		element = buffer[oldest];
		oldest = (oldest + 1) % CAPACITATE;
		counter--;
		semafor_producator.release();
		return element;
	}

}
