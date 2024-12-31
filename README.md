# RationalRange
(WIP) Honest Rational number in java.

This number system allows you to know how much you know; if a number can't be defined precisely, it will be defined imprecisely in a controlled way. This way accuracy can be preserved while sacrificing precision.

Epsilon is the smallest between zero and one, and epsilon can grow to any size. In many rational cases, however, epsilon is exactly zero. 

With the addition of upper bounds and lower bounds, if a number can be calculated exactly, it will be exact, but if it cannot, its epsilon will grow. The bounds represent an area where the value is, but the value is actually a single point. The range is not a range where all possible values are valid.

A bound is not really an exact value, but rather a limit; it can approach its value from either direction. This way, 'approaches zero' can be neatly distinguished from 'can be zero'.

Because bounds represent unknowns as to the exact value, when bounds are compared, if they overlap at all it will result in some ambiguity, which is why this project also has quantified booleans. If two bounds are the same and are perfect, it is 50/50.

### The Good:
- If an integer can be represented, it can be perfectly divided by any other integer that can be represented. 
- If an operation yields a value whose upper bound and lower bound are the same (and act to include rather than exclude their value), the result is exactly that value.
- It should also be able to handle the dreaded 0.1 + 0.2 very easily 
(no decimals in the from string constructor yet though)

### The Bad:
- Unlike with floats, this number system can't represent any number larger than its largest integer.
- For now, the numerator and denomenator use ints so the precision of non-perfect values is limited.

### The Weird:
- The upper bound can be lower than the lower bound. This is caused when the reciprocal is taken of a value which may be positive or may be negative. It is intended behavior.
- in the case of an included zero it results in a very nice "no value" behavior, the upper bound being negative unbounded, and the lower being positive unbounded, which basically means "definitely not any known number"
- zero over zero is undefined; the RationalRange type only represents a single value, one who's exact location may be in doubt, and not an inequality where all the values are acceptable.


