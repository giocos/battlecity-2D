# BATTLE CITY

1. Introduzione:
Battle City è un videogioco arcade che rispecchia lo stile NES realizzato con una grafica 2D, utilizzando le swing fornite dalla libreria di Java 8.
Il player è rappresentato da un carrarmato che si scontrerà con altri carrarmati nemici con il solo scopo di difendere la sua fortezza.

2. Modalita’ di gioco:
Il gioco prevede due modalità differenti:
• Nella carriera di gioco è possibile decidere se intraprendere la modalità single o multiplayer. L’obiettivo del giocatore è quello di difendere la propria “base”, rappresentata da una bandiera, dall’ assedio dei carrarmati nemici. La carriera è composta da un totale di 24 livelli, ognuno dei quali viene sbloccato solo in caso di vittoria nel livello precedente salvando i dati correnti del player. In ogni stage si ottiene un punteggio totale pari al numero e alla tipologia di nemici eliminati e ad ogni power up catturato, calcolandone un punteggio totale (hi-score) che in caso di superamento verrà aggiornato e visualizzato nel menu di gioco.
• Nella modalità online si è deciso volutamente di poter far connettere un massimo di due player, in modo tale da poter giocare in alleanza, contro i nemici.
Una volta selezionata la modalità network è possibile creare o accedere ad una stanza (lobby) di gioco, costituita da una chat per scambiare messaggi.
• Oltre alle modalità precedenti il gioco consente la creazione di mappe personalizzate attraverso l’utilizzo di un editor intelligente, che controlla se la mappa creata soddisfa o meno i requisiti di gioco.
Le difficoltà di gioco possono essere scelte tra easy, normal e hard, dove quest’ultimo è stato realizzato con l’intelligente algoritmo A-star, che non renderà così semplice la vostra esperienza di gioco.

3. Specifiche dettagliate:
Ogni giocatore ha un numero di colpi (rockets) che variano in base al livello del carrarmato. Le vite del carrarmato sono 3, quando si muore, si ritorna automaticamente al livello 1 e si perde una vita ripartendo dalla posizione di partenza. Le vite perse possono essere recuperate attraverso dei potenziamenti. Quando si muore, si ritorna automaticamente al livello 1.

![image](https://drive.google.com/open?id=1bkwf7YxQrnwD_ycADgBbqPsq_n70AHjc)

Ogni nemico ha le proprie caratteristiche, i quali possono essere lampeggianti ( all’uccisione rilasciano cadere un powerUp random ) o normali che non rilasciano nessun powerUp. Inoltre anche i nemici posso catturare il potenziamento HELMET.

![Alt text](https://www.dropbox.com/s/tca59hcsfcu7oxa/1.png?dl=0)

Ogni potenziamento può essere preso solo dai Player, escluso Helmet che invece può anche essere catturato dagli Enemy.

![Alt text](https://www.dropbox.com/s/tca59hcsfcu7oxa/1.png?dl=0)

Ogni mappa può essere costituita dai seguenti oggetti

![Alt text](https://www.dropbox.com/s/tca59hcsfcu7oxa/1.png?dl=0)


