package Locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
*<h1> Producator - Consumator </h1> 
* Clasa PCLock detine lacatul, conditiile si cele doua metode (append si take) prin intermediul carora 
* producatorii pot adauga elemente in buffer (in limita spatiului disponibil - nu se poate suprascrie 
* un element din buffer daca acesta nu a fost inca consumat) iar consumtorii pot extrage elemente
* ( in limita stocului din buffer - nu se poate consuma un element ce a fost consumat anterior).
* <p>
* Un consumator nu poate extrage elemente din buffer daca acesta e "gol" (are doar elemente ce au fost 
* consumate de firul curent sau alte fire de executie) deoarece va intra in bucla "while" si va trebui 
* sa astepte pana va primi semnalul dat de metoda signalAll().
* Un producator nu poate adauga elemente in buffer daca acesta este "plin" ( are doar elemente ce
* nu au fost consumate inca) fiindca va intra in bucla "while" si va trebui sa astepte pana va primi
*  semnalul dat de metoda signalAll().
* <p>
* La intrarea in cele 2 metode lacatul se va inchide si va fi deblocat doar la iesirea din metoda.
* <p>
* @author  Brinzan Florinel Razvan
* @since   2019-12-07
*/

public class PCLock {
	
	private final int CAPACITATE = 5;
	private volatile int oldest = 0;
	private volatile int newest = 0;
	private volatile int counter = 0;
	private int buffer[] = new int[CAPACITATE];
	
	ReentrantLock lock = new ReentrantLock();
	
	Condition conditie1 = lock.newCondition(); 
	Condition conditie2 = lock.newCondition();
	
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
	   * signalAll() ce trezeste toate firele de executie.
	   * <p>
	   * La apelarea acestei metode lacatul se va bloca si va fi deblocat doar
	   * la iesirea din metoda.
	   * @param element elementul ce va fi adaugat in buffer
	  */
	
	public void append(int element) {

		lock.lock();
		try {
			
			while (counter == CAPACITATE) {
				conditie1.await();
			}
			
			buffer[newest] = element;
			newest = (newest + 1) % CAPACITATE;
			counter++;
			
			conditie2.signalAll();
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	  /**
	   * Prin aceasta metoda Consumatorul extrage elemente din buffer.
	   * Cat timp counter-ul este mai mare decat 0 insemana ca exista elemente
	   * neconsumate in buffer, deci consumatorul le poate extrage ( incepand cu 
	   * cel mai vechi element introdus).Daca counter-ul va avea valoarea 0
	   * atunci firul de executie va astepta sa fie notificat.
	   * <p>
	   * In momentul in care se extrage un element din buffer se apeleaza metoda
	   * signalAll() ce trezeste toate firele de executie.
	   * La apelarea acestei metode lacatul se va bloca si va fi deblocat doar 
	   * la iesirea din metoda.
	   * @return elementul extras din buffer de catre consumator
	  */

	public int take() {
		
		int element = 0;
		
		lock.lock();
		try {
			while (counter == 0) {
				conditie2.await();
			}
			element = buffer[oldest];
			oldest = (oldest + 1) % CAPACITATE;
			counter--;
			
			conditie1.signalAll();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return element;
	}
}
