![](https://i.imgur.com/R2yKOuR.png)

# SUDOKUH
[![forthebadge made-with-java](https://forthebadge.com/images/badges/made-with-java.svg)](https://java.com/)

SUDOKUH ist ein einfaches Sudoku-Spiel für verschiedene Größen (2x2, 3x3, 4x4) und ein Sudoku-Löser für normales Sudoku (in verschiedenen Größen: 2x2, 3x3, 4x4), Str8ts und Killer (für 3x3).

SUDOKUH wurde erstellt von [Luca Kellermann](https://github.com/Lukellmann), [Fabian Heinl](https://github.com/WHYZNSoftware) und [Philipp Kremling](https://github.com/KremlingP ).

Bitte beachten Sie, dass Sie Java 16 benötigen, um das Programm korrekt ausführen zu können.

## Einstieg
Zum Starten muss nur die SUDOKUH.jar ausgeführt werden, das Startmenü öffnet sich automatisch. Hier können Sie zwischen den verschiedenen Kategorien wählen: dem Sudoku-Spiel, dem Sudoku-Löser für ein normales Sudoku, dem Sudoku-Löser für Str8ts und dem Sudoku-Löser für Killer. Mit einem Klick auf eine der Kategorien sehen Sie ein Menü, in dem Sie zwischen den drei Größen (2x2, 3x3, 4x4) wählen können. Ein Klick auf "Start" öffnet ein neues Fenster, in dem das Spiel-Overlay zu sehen ist. Im Startmenü gibt es auch eine Einstellungsschaltfläche. Wenn Sie darauf klicken, erhalten Sie ein neues Menü und Sie können das Erscheinungsbild auswählen (Standard- oder DarkMode), Auto Step (das bedeutet, ob die nächste Zelle beim Eingeben einer Zahl automatisch ausgewählt werden soll), Hervorhebung (dies bedeutet, ob Zellen, die potenziell Konflikte zur aktuell ausgewählten Zell beinhalten, hervorgehoben werden sollen) und das Tipplimit (wie viele Tipps Sie im Spiel-Modus haben möchten). Ein Klick auf den Zurück-Button bringt Sie zurück zum Home-Menü und die Einstellungen werden automatisch gespeichert.

## Das Ingame-Menü
Nach Auswahl einer Kategorie gelangt man in das Ingame-Menü, das im Allgemeinen aus drei Elementen besteht: In der Mitte befindet sich das Sudoku-Feld. Wenn Sie auf eine Zelle klicken, können Sie diese Zelle zur Eingabe auswählen. Wenn die Hervorhebungsoption aktiviert ist, können Sie auch alle möglichen widersprüchlichen Zellen hervorgehoben sehen. Sie können im Sudoku-Menü auch mit den Pfeiltasten auf Ihrer Tastatur navigieren.

Am rechten Rand befindet sich das Schaltflächen-Menü: Hier sehen Sie alle Schaltflächen, die Ihnen zur Verfügung stehen: Für jede Kategorie wären das die Zahlen und ein Löschen-Button. Jede Kategorie hat auch ein paar zusätzliche Schaltflächen (siehe unten). Die meisten Buttons können auch durch Eingabe einer entsprechenden Taste auf der Tastatur verwendet werden (Zahlen für die Zifferntasten, zurück für die Löschtaste). Wenn Auto-Step aktiviert ist, wird die nächste Zelle automatisch ausgewählt, nachdem ein (Zahlen-)Wert eingegeben wurde.

Oben befindet sich das Informationsfeld: Hier sehen Sie relevante Informationen wie die Spielzeit oder ob die Lösung richtig ist oder nicht (je nach Kategorie).

In der rechten oberen Ecke befindet sich auch der Hauptmenü-Button sowie der Erneut-Spielen-Button. Wenn Sie auf den Hauptmenü-Button klicken, gelangen Sie zurück zum Hauptmenü. Mit Klick auf den Erneut-Spielen-Button wird derselbe Spielmodus mit denselben Einstellungen erneut geöffnet. In beiden Fällen gehen alle in den Spielen durchgeführten Aktionen verloren.

## Kategorien
### Sudoku spielen
Achtung: Die Auswahl des 4x4-Sudoku zum Spielen dauert sehr lange! Bitte seien Sie sich dessen bewusst.

Beim Spielen von Sudoku wird die Spielzeit im Informationsfeld angezeigt. Sobald das Sudoku vollständig gelöst ist, stoppt der Timer und die gesamte Spielzeit wird angezeigt. Durch Klicken auf die Hauptmenü- oder Erneut-Spielen-Schaltfläche wird der Timer angehalten.

Zum Spielen gibt es ein paar zusätzliche Schaltflächen: Zunächst die Tipp-Schaltfläche: Wenn Sie darauf klicken, wird die richtige Nummer für die ausgewählte Zelle angezeigt. Die Zelle wird nun als vordefiniert angesehen und kann nicht mehr geändert werden. Wie viele Tipps Sie zur Verfügung haben, hängt von den Einstellungen im Hauptmenü ab (Standard: 3). Sobald Ihnen die Tipps ausgehen, wechselt die Schaltfläche auf rot und hat keine Funktion mehr. (Tastaturäquivalent: T)

Zusätzlich gibt es den Validieren-Button: Ein Klick darauf zwingt das Programm, Ihre Lösung zu überprüfen, ob sie richtig ist oder nicht. Das Ergebnis wird für fünf Sekunden im Informationsfeld angezeigt, danach wird wieder die Spielzeit angezeigt. Der Validierungsprozess wird automatisch eingeleitet, wenn Sie eine Zahl eingeben, sodass Sie normalerweise nicht auf diese Schaltfläche klicken müssen. (Tastaturäquivalent: E)

Schließlich gibt es noch den Notiz-Button: Mit Klick darauf wird der Button grün, was symbolisiert, dass der Notiz-Modus aktiv ist: Wenn Sie jetzt eine Zahl eingeben, wird diese klein in der Zelle angezeigt. Hier können Sie jede der angegebenen Zahlen als Notiz für Sie eingeben: Das Ergebnis wird dadurch nicht beeinflusst. Wenn Sie eine in eine Zelle eingetragene Notiz löschen möchten, können Sie dieselbe Nummer nochmals drücken: Sie wird aus Ihren Notizen entfernt. Sie können alle Notizen mit dem Löschen-Button löschen und mit einer Eingabe überschreiben, indem Sie den Notizmodus ausschalten (erneut auf die Schaltfläche Notiz klicken) und dann eine Zahl eingeben. (Tastaturäquivalent: N)

### Sudoku lösen
In dieser Kategorie können Sie ein ungelöstes Sudoku eingeben und der Algorithmus wird es für Sie lösen. Daher gibt es neben den normalen Zahlen-Buttons und dem Löschen-Button auch einen Lösen-Button. Wenn Sie darauf klicken, wird das Sudoku mit übereinstimmenden Werten gefüllt. Alle von Ihnen gesetzten Eingaben werden als vordefiniert angezeigt (können aber geändert werden). Wenn das Sudoku nicht lösbar ist, wird ein Text im Informationsfeld angezeigt. (Tastaturäquivalent: ENTER)

### Str8ts lösen
Str8ts ist eine spezielle Variante von Sudoku: Hier gibt es keine Unterraster und nur ein großes Feld: Einzige Bedingung ist also, dass nicht dieselbe Zahl in derselben Zeile oder Spalte stehen darf. Außerdem können gesperrte Felder (normalerweise schwarz dargestellt) existieren, die keine Nummern enthalten dürfen oder die darin enthaltenen vordefinierten Nummern können nicht geändert werden. Auch darf es in horizontal oder vertikal zusammenhängenden weißen Feldern nur nahtlose Ziffernfolgen, also Straßen, geben. Für die Trennung zwischen schwarzen und weißen Feldern gibt es einen Farbwechsel-Button: Ein Klick darauf ändert die Farbe der aktuell ausgewählten Zelle. Durch erneutes Anklicken wird sie wieder geändert. (Tastaturäquivalent: F)

Zum Lösen des Sudokus gibt es wieder einen Lösen-Button. Wenn Sie darauf klicken, wird das Sudoku mit übereinstimmenden Werten gefüllt. Wenn das Sudoku nicht lösbar ist, wird ein Text im Informationsfeld angezeigt. (Tastaturäquivalent: ENTER)

### Killer lösen
Killer ist auch eine besondere Variante von Sudoku: Neben den normalen Sudoku-Regeln gibt es hier auch Summenfelder: Das heißt, eine oder mehrere Zellen werden zusammengefaßt und mit einer Nummer versehen. Die Summe aller Werte dieser Gruppe muss dieser Zahl entsprechen. Umgekehrt gibt es auch Sudokus, bei denen Sie gar keine Werte vordefiniert haben, sondern nur die Summen und Gruppen (meist auf Schwierigkeitsstufe schwer).

Neben den normalen Nummern-Buttons und dem Löschen-Button gibt es auch einen Verbinden-Button. Wenn Sie darauf klicken, wird dieser grün und signalisiert, dass der Verbindungs-Modus aktiviert ist: Die aktuell angeklickte Zelle wird automatisch als vordefiniert angezeigt. Jetzt können Sie alle Nachbarn dieser Zelle und Nachbarn dieser Zellen auswählen usw., um eine Gruppe zu bilden. Alle Gruppenmitglieder haben die vordefinierte Farbe. Durch erneutes Klicken auf ein Gruppenmitglied wird die Zelle aus der Gruppe entfernt. Sie können der Gruppe keine Zellen hinzufügen, die sich bereits in einer anderen Gruppe befinden. Wenn Sie mit der Auswahl aller Gruppenmitglieder fertig sind, klicken Sie erneut auf die Verbinden-Schaltfläche: Sie sehen ein neues Fenster, in dem Sie die Summe der Gruppe eingeben können. Wenn Sie dies bestätigen, wird die Gruppe mit der Summe in der ersten ausgewählten Zelle angezeigt. (Tastaturäquivalent: G)

Wenn Sie den Verknüpfungsvorgang beim Auswählen von Zellen abbrechen möchten, verwenden Sie bitte die Schaltfläche zum Aufheben der Verknüpfung. (Tastaturäquivalent: L)

Um eine ganze Gruppe zu entfernen, gibt es die Schaltfläche zum Aufheben der Verknüpfung. Wenn Sie eine Zelle ausgewählt haben, die Mitglied einer Gruppe ist, und auf diese Schaltfläche klicken, wird die gesamte Gruppe entfernt. (Tastaturäquivalent: L)

Wenn Sie nur einzelne Zellen von einer Gruppe entfernen oder einer Gruppe hinzufügen möchten (und diese nicht vollständig entfernen möchten), können Sie auf die Schaltfläche Bearbeiten klicken, während eine Zelle, die Mitglied einer Gruppe ist, ausgewählt ist. Nun ist der Bearbeitungsmodus aktiviert: Sie können Zellen für diese Gruppe an- oder abwählen und beim Ausschalten des Bearbeitungsmodus durch erneutes Klicken auf den Bearbeiten-Button können Sie der Gruppe eine neue Summe hinzufügen. Sie müssen Zellen im Bearbeitungsmodus nicht (de)selektieren, Sie können ihn auch nur verwenden, um eine neue Summe auszuwählen.
Bitte beachten Sie: Der Link-Modus und der Edit-Modus können nicht gleichzeitig aktiviert sein. (Tastaturäquivalent: B)

Zum Lösen des Sudokus gibt es wieder einen Lösen-Button. Wenn Sie darauf klicken, wird das Sudoku mit übereinstimmenden Werten gefüllt. Wenn das Sudoku nicht lösbar ist, wird ein Text im Informationsfeld angezeigt. (Tastaturäquivalent: ENTER) 