import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author Wodzu
 */
public class Kodowanie extends JFrame {

	double[] fx_i = new double[256];
	double entropia = 0.0;
	Wezel[] tab = new Wezel[256];
	Wezel[] tablica = new Wezel[400];
	JLabel label1, label2, label3, label4, label5;

	int rozmiar = 400;
	long total = 0;
	long total1 = 0;
	long totalkod = 0; // ca�kowita d�ugo�� kod�w odpowiadaj�cych
	byte pusty = ' '; // znak domy�lny oznaczaj�cy pole w tablicy kt�re
						// nie zosta�o zaj�te przez znak z pliku, bo
						// nieekoniecznie musi by� w nim 255 znak�w
	byte wezel = '^'; // znak domy�lny dla w�z�a
	double ratio = 0.0;
	double average = 0.0;

	File plik;

	public Kodowanie() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(100, 100);
		JFileChooser jfc = new JFileChooser();
		int r = jfc.showOpenDialog(null);
		plik = jfc.getSelectedFile();
		System.out.println(plik.getAbsolutePath());
		setSize(400, 400);
		setVisible(true);
		setResizable(false);
		setLayout(null);
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		label5 = new JLabel();
		label1.setBounds(10, 30, 300, 60);
		label2.setBounds(10, 60, 300, 90);
		label3.setBounds(10, 90, 300, 120);
		label4.setBounds(10, 120, 300, 150);
		label5.setBounds(10, 150, 300, 180);
		add(label1);
		add(label2);
		add(label3);
		add(label4);
		add(label5);
	}

	public void entropia() {
		for (int i = 0; i < this.tab.length; i++)
			if ((char) this.tab[i].znak == (char) this.pusty)
				break;
			else {
				entropia += this.tab[i].pr
						* (Math.log(1 / this.tab[i].pr) / Math.log(2));
			}

	}

	public String liczKodArytm() {
		FileInputStream fis = null;
		DataInputStream dis = null;
		double u = 1;
		double l = 0;
		double utemp, ltemp;
		int znak = 0;
		int licznik = 0;
//		int i = 1;
		long totalKod = 0;
		String kod = "";

		try {
			fis = new FileInputStream(plik);
			dis = new DataInputStream(fis);
		} catch (Exception e) {

			return kod;
		}
		liczF();
//		i = 1;
		try {
			while ((znak = dis.readUnsignedByte()) != -1) {

				if (znak == 0) {
					ltemp = l + (u - l) * 0;
				} else {
					ltemp = l + (u - l) * fx_i[znak - 1];
				}
//				System.out.println(fx_i[znak - 1]);
//				System.out.println(ltemp);
				utemp = l + (u - l) * fx_i[znak];
				l = ltemp;
				u = utemp;
				while (true) {
					if (l < 0.5 && u <= 0.5) {
						l = 2 * l;
						u = 2 * u;
						kod += "0";
						for (int a = 0; a < licznik; a++)
							kod += "1";
						licznik = 0;
					} else if (l >= 0.5 && u > 0.5) {
						l = 2 * l - 1;
						u = 2 * u - 1;
						kod += "1";
						for (int a = 0; a < licznik; a++)
							kod += "0";
						licznik = 0;
					} else if (l >= 0.25 && u < 0.75 && u > 0.5 && l < 0.5) {
						l = 2 * l - 0.5;
						u = 2 * u - 0.5;
						licznik++;
					} else
						break;
			
			}
				
				System.out.println("L"+l+" U "+u);
			}
		} catch (Exception e) {
		}
		if (licznik > 0) {
			kod += "1";
			for (int a = 0; a < licznik; a++)
				kod += "0";
			totalKod += 1 + licznik;
		}
		
//		System.out.println("L"+l+" U "+u);
		
		System.out.println("KODDDDDDDDD\n\n\n\n\n"+kod+"\n\n\n\n\n");
		return kod;
	}

	public void czytaj() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(plik);
		DataInputStream dis = new DataInputStream(fis); // jakieś śmieszne
														// strumienie z
														// materiałów od dr
														// Zawady
		int ch;

		for (int i = 0; i < this.tablica.length; i++) {
			this.tablica[i] = new Wezel();
		}
		try {
			while ((ch = dis.readUnsignedByte()) != -1) {
				this.total++;
				this.tablica[ch].ile++;
				this.tablica[ch].zn = ch;
			}
		} catch (IOException e) {
		}

		for (int i = 0; i < this.tablica.length; i++) {
			this.tablica[i].pr = (double) this.tablica[i].ile / (this.total);
		}
	}

	public void znaki() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(plik);
		DataInputStream dis = new DataInputStream(fis); // jakie� �mieszne
														// strumienie z
														// materia��w od dr
														// Zawady
		byte ch;

		for (int i = 0; i < this.tab.length; i++) {
			this.tab[i] = new Wezel();
		}
		try {
			while ((ch = dis.readByte()) != -1) {
				this.total1++;
				for (int i = 0; i < 256; i++) {
					if (this.tab[i].znak == this.pusty) {
						this.tab[i].znak = ch;
						this.tab[i].ile = 1;
						break;
					}
					if ((char) this.tab[i].znak == (char) ch) {
						this.tab[i].ile++;
						break;
					}
				}
			}
		} catch (IOException e) {
		}

		for (int i = 0; i < this.tab.length; i++) {
			this.tab[i].pr = (double) this.tab[i].ile / this.total1;
		}
	}

	public void codeLength() {
		int howManyElements = 0;
		int howManyLeft = 0;
		double temp = 0.0;

		for (int i = 0; i < tab.length; i++) {
			if ((char) this.tab[i].znak == (char) this.pusty)
				break;
			else
				howManyElements++;
		}
		howManyLeft = howManyElements;
		while (howManyLeft > 1) {
			sortuj(howManyLeft);
			Wezel newWezel = new Wezel();
			newWezel.pr = this.tab[howManyLeft - 1].pr
					+ this.tab[howManyLeft - 2].pr;
			newWezel.ile = this.tab[howManyLeft - 1].ile
					+ this.tab[howManyLeft - 2].ile;
			temp += this.tab[howManyLeft - 1].pr + this.tab[howManyLeft - 2].pr;
			newWezel.znak = this.wezel;
			this.tab[(howManyLeft - 2)] = newWezel;
			howManyLeft--;
		}
		this.average = temp;

	}

	public void liczF() {
		double f_i = 0.0;

		for (int c = 0; c < 256; c++) {
			for (int i = 0; i <= c; i++) {
				fx_i[c] += this.tablica[i].pr;

			}
		}
	}

	public void sortuj(int ileZostalo) {
		Wezel klucz = new Wezel();
		int i, j;

		for (i = 1; i < ileZostalo; i++) {
			klucz = this.tab[i];
			j = i - 1;
			while ((j >= 0) && (tab[j].ile < klucz.ile)) {
				tab[j + 1] = tab[j];
				j = j - 1;
				tab[j + 1] = klucz;
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Kodowanie kod = new Kodowanie();
		kod.znaki();
		kod.czytaj();
		kod.entropia();
		kod.codeLength();
		kod.label1.setText("Śr dł kodu arytmetycznego: "
				+ (double) kod.liczKodArytm().length() / kod.total);
		kod.label2.setText("Śr dł kodu Huffmana: " + kod.average);
		kod.label3.setText("Entropia: " + kod.entropia);
		kod.label4.setText("Ratio: " + (double) kod.average / kod.entropia);
		kod.label5.setText("Compress ratio: " + (double) kod.average / 8);
		System.out.println();
		System.out.println(kod.average);
		System.out.println(kod.entropia);
	}
}

class Wezel {
	public byte znak;
	public int zn;
	public long ile;
	public double pr;
	Wezel lewy;
	Wezel prawy;

	Wezel() {
		this.znak = ' ';
		this.ile = 0;
		this.pr = 0.0;
		this.lewy = null;
		this.prawy = null;
	}
}