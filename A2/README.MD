# Linked Lists
This program implements a doubly-linked list used to store books in order of their popularity/rank (simulating a computer system used in a library). It reads a text file containing the books to be tracked as well as various actions performed on those books like:

• Add ('A'): Add a new book to the library.
• Query ('Q'): Get more information about a book for a library patron.
• Hold ('H'): Place a hold on a book (used when somebody else has borrowed the book, or the book is at a different location than the library patron).
• Borrow ('B'): Borrow a book.
• Return ('R'): Return a book that was previously borrowed.

Each time the book is involved in a library command it is either added to the list and assigned a rank/popularity of 1, or the rank is incremented by 1 if the book already exists.