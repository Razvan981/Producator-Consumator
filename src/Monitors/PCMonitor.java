package Monitors;

/**
*<h1> Producator - Consumator </h1> 
* Clasa PCMonitor detine cele doua metode sincronizate (append si take) prin intermediul carora 
* producatorii pot adauga elemente in buffer (in limita spatiului disponibil - nu se poate suprascrie 
* un element din buffer daca acesta nu a fost inca consumat) iar consumtorii pot extrage elemente
* ( in limita stocului din buffer - nu se poate consuma un element ce a fost consumat anterior).
* <p>
* Un consumator nu poate extrage elemente din buffer daca acesta e "gol" (are doar elemente ce au fost 
* consumate de firul curent sau alte fire de executie) deoarece va intra in bucla "while" si va fi adormit,
* iar producatorul nu poate adauga elemente in buffer daca acesta este "plin" ( are doar elemente ce
* nu au fost consumate inca) fiindca va intra in bucla "while" si va fi adormit.
* <p>
* Asteptarea firelor se va incheia in momentul in care va fi apelata metoda notifyAll() ce va trezi
* toate firele care asteapta pe monitorul obiectului.
* <p>
* @author  Brinzan Florinel Razvan
* @since   2019-12-07
*/

public class PCMonitor {
	
	private final int CAPACITATE = 5;
	private volatile int oldest = 0;
	private volatile int newest = 0;
	private volatile int counter = 0;
	private int buffer[] = new int[CAPACITATE];
	
	  /**
	   * Prin aceasta metoda Producatorul adauga elemente in buffer.
	   * Cat timp counter-ul este mai mic decat capacitatea buffer-ului
	   * se vor produce noi elemete (acestea vor suprascrie elemente deja
	   * consumate din buffer si counter-ul ce numara elementele neconsumate
	   * va creste).
	   * <p>
	   * Daca counter-ul va avea valoarea capacitatii buffer-ului atunci firul
	   * de executie va intra in bucla while si va astepta sa fie notificat.
	   * In momentul in care se adauga un element in buffer se apeleaza metoda
	   * notifyAll() ce trezeste toate firele care asteapta pe monitorul obiectului.
	   * @param element elementul ce va fi adaugat in buffer
	  */
	
	public synchronized void append(int element) {
		while (counter == CAPACITATE) {
			try {
				wait();
			} catch (InterruptedException e){}
		}
		buffer[newest] = element;
		newest = (newest + 1) % CAPACITATE;
		counter++;
		notifyAll(); // signal not zero
	}
	
	  /**
	   * Prin aceasta metoda Consumatorul extrage elemente din buffer.
	   * Cat timp counter-ul este mai mare decat 0 insemana ca exista elemente
	   * neconsumate in buffer, deci consumatorul le poate extrage ( incepand cu 
	   * cel mai vechi element introdus).Daca counter-ul va avea valoarea 0
	   * atunci firul de executie va astepta sa fie notificat.
	   * <p>
	   * In momentul in care se extrage un element din buffer se apeleaza metoda
	   * notifyAll() ce trezeste toate firele care asteapta pe monitorul obiectului.
	   * @return elementul extras din buffer de catre consumator
	  */

	public synchronized int take() {
		int element;
		while (counter == 0) {
			try {
				wait();
			} catch (InterruptedException e){}
		}
		element = buffer[oldest];
		oldest = (oldest + 1) % CAPACITATE;
		counter--;
		notifyAll(); // signal not full
		return element;
	}

}
