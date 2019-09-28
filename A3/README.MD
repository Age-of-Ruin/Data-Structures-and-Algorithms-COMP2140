# Stacks and Postfix Expressions
This program utilizes a custom stack (implemented as a linked list) to process postfix expressions (ie where the operators are placed at the end of the expression) into infix expressions (ie standard mathematical expressions) and evalute it.

Eg.

1 3 2 × + 6 14 3 4 + / − +   (POSTFIX)

1 (3 × 2) + 6 14 3 4 + / − +

(1 + 3 × 2) 6 14 3 4 + / − +

(1 + 3 × 2) 6 14 (3 + 4) / − +

(1 + 3 × 2) 6 (14/(3 + 4)) − +

(1 + 3 × 2) (6 − 14/(3 + 4)) +

(1 + 3 × 2) + (6 − 14/(3 + 4))   (INFIX)

= 11